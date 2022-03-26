package B208.mag_jang.controller;

import B208.mag_jang.domain.ChatMessageDTO;
import B208.mag_jang.domain.DealDTO;
import B208.mag_jang.domain.GameDTO;
import B208.mag_jang.domain.Player;
import B208.mag_jang.service.AsyncService;
import B208.mag_jang.service.GameService;
import B208.mag_jang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final SimpMessagingTemplate template;
    private final GameService gameService;
    private final UserService userService;
    private final AsyncService asyncService;

    // game시작 시에 room에 있는 유저아이디를 game으로 넘겨주며 roommap 삭제 - ㅇ
    // 프론트 : 메서드 만들어서 처리 "/sub/game/start/{roomId}"
    @MessageMapping(value = "/start")
    public void gameStart(ChatMessageDTO message) throws InterruptedException {

        if(gameService.gameStart(message.getWriter(), message.getRoomId())){
            // 1. 게임시작 메세지 전송
            System.out.println("gameStart : 게임을 시작합니다!");
            template.convertAndSend("/sub/game/start/" + message.getRoomId(), gameService.getGame(message.getRoomId())); // GameDTO 전송


            // 2. 3초 후 능력 생성
            // AsyncService의 @Async 메서드 호출, callback 등록을 통해 능력 생성
            asyncService.sleep(message.getRoomId(), 3000).addCallback((result) -> {
                System.out.println("callback returns : " + result);
                try {
                    initJobs(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, (e) -> {
                System.out.println("error");
            });
        }else{//게임 인원 부족
            System.out.println("gameStart Failed : 게임 인원이 부족합니다");
            template.convertAndSend("/sub/game/start/" + message.getRoomId(), (Object) null);
        }


    }

    // 플레이어 능력 생성 후 반환
    // 프론트 : 메서드 만들어서 처리 "/sub/game/jobs/{roomId}"
    public void initJobs(String roomId) throws InterruptedException {
        System.out.println("initJobs : "+roomId);
        template.convertAndSend("/sub/game/jobs/" + roomId, gameService.getNextJobs(roomId)); // 플레이어별 능력 리스트 전송

        // @Async 메서드 호출, callback 등록을 통해 처리
        asyncService.sleep(roomId, 3000).addCallback((result) -> {
            System.out.println("callback!! : " + result);
            try {
                initOrder(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, (e) -> {
            System.out.println("error");
        });
    }

    // 플레이어 순서를 생성 후 반환
    public void initOrder(String roomId) throws InterruptedException {
        System.out.println("initOrder : "+roomId);
        template.convertAndSend("/sub/game/order/" + roomId, gameService.initOrder(roomId));

        // @Async 메서드 호출, callback 등록을 통해 처리
        asyncService.sleep(roomId, 3000).addCallback((result) -> {
            System.out.println("callback!! : " + result);
            try {
                initDeal(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, (e) -> {
            System.out.println("error");
        });
    }

    public void sendCurrBroker(String roomId){
        //브로커 주기
        Player player = gameService.getCurrBroker(roomId);
//        Player player2 = new Player("김주호");
        template.convertAndSend("/sub/game/broker/" + roomId, player);
    }

    // 거래 조건 생성
    public void initDeal(String roomId) throws InterruptedException {
        sendCurrBroker(roomId);


        // @Async 메서드 호출, callback 등록을 통해 처리
        asyncService.sleep(roomId, 3000).addCallback((result) -> {
            System.out.println("callback!! : " + result);
            //3초 기다렸다가 딜 생성
            System.out.println("initDeal : "+roomId);
            DealDTO deal = new DealDTO();
            deal = gameService.initDeal(roomId);
            System.out.println(deal);
            System.out.println("/sub/game/deal/" + roomId);
            template.convertAndSend("/sub/game/deal/" + roomId, deal);
        }, (e) -> {
            System.out.println("error");
        });

    }
    
    // 함께 거래할 멤버 리스트를 받아 모두에게 전송 - 금액을 함께 적어서 전송할지 논의 필요
    // 프론트에서 {멤버 : 돈} 객체 발신 및 수신하도록 구현 -> 이러면 roomId는 어떻게 받지? -> 기존 ChatMessageDTO의 message를 Object로 바꾸고, 경우에 따라 처리...
    // 프론트 발신 : "/pub/game/choice/{roomId}", 수신 : "/sub/game/choice/{roomId}"
    // 요거 조금만 응용하면 한 명 한 명 클릭 보이게 할 수 있음 : 플레이어 클릭 시 메세지 + 금액 넣고 버튼 클릭 시 메세지

    @MessageMapping(value = "/choice")
    public void dealMemberChoice(ChatMessageDTO message) throws InterruptedException {
        String member = (String) message.getMessage();
        System.out.println("dealMemberChoice : 거래 멤버 선택 " + member);
        template.convertAndSend("/sub/game/choice/" + message.getRoomId(), member); // 선택된 멤버 전송
    }

    @MessageMapping(value = "/finalchoice")
    public void dealMemberChoiceComplete(ChatMessageDTO message) throws InterruptedException {
        System.out.println(message.getMessage());
        if(message.getMessage().equals("") || message.getMessage() == null){
            // 최종 멤버 결정 실패 시 투표를 건너뛰고 다음 턴으로 진행(마지막 턴이라면 라운드 +1)
            System.out.println("결정 실패");
            message.setMessage(new ArrayList<>());
            template.convertAndSend("/sub/game/finalchoice/" + message.getRoomId(), message); // {멤버 : 돈} 전송
//            template.convertAndSend("/sub/game/finalchoice/" + message.getRoomId(), (Object) ""); // {멤버 : 돈} 전송
            //컨트롤러 메서드 만들기
            startNext(message.getRoomId());
        }else{
        List<ArrayList<Object>> moneys = (ArrayList<ArrayList<Object>>)message.getMessage(); // [["player1", 300], ["player2", 200] ,,,]
        gameService.setDealAmount(message.getRoomId(), moneys);
        DealDTO deal = gameService.getGame(message.getRoomId()).getDeal();
        System.out.println("dealMemberChoice : 분배금 확인 " + deal);
        template.convertAndSend("/sub/game/finalchoice/" + message.getRoomId(), deal); // {멤버 : 돈} 전송
        }
    }

    @MessageMapping(value = "/vote")
    public void vote(ChatMessageDTO message) throws InterruptedException {
        System.out.println(message.getWriter() + " 투표 : " + (boolean)message.getMessage());
        DealDTO deal = gameService.getGame(message.getRoomId()).getDeal();
        deal.addVote(message.getWriter(), (boolean)message.getMessage());

        //깽판 횟수 ++
        if(!(boolean)message.getMessage()){
            for(Player player : gameService.getGame(message.getRoomId()).getPlayerList()){
                if(player.getNickName().equals(message.getWriter())){
                    player.setGangAmount(player.getGangAmount()+1);
                }
            }
        }

        List<String> player = new ArrayList<>();
        player.add(message.getWriter());
        template.convertAndSend("/sub/game/vote/" + message.getRoomId(), player); // 이 사람이 투표 완료했어요! - ㅇ

        if(deal.isVoteOK()){
            deal.calcMoney(gameService.getGame(message.getRoomId()).getRound());
            template.convertAndSend("/sub/game/finalvote/" + message.getRoomId(), deal); // {멤버 : 돈} 전송, 이를 파싱하여 자신의 돈 추적 - ㅇ

            gameService.updatePlayerMoney(message.getRoomId()); // 거래 결과를 플레이어별 돈에 반영
            gameService.updateGameLog(message.getRoomId());
            startNext(message.getRoomId()); // 다음 스텝 진행 turn++
        }

    }

    private void startNext(String roomId) throws InterruptedException {
        gameService.startNext(roomId);
        if(!gameService.isGameFinished(roomId)){
            if(gameService.getGame(roomId).getTurn() == 1){
                template.convertAndSend("/sub/game/rank/" + roomId, gameService.initOrder(roomId)); // List<String> 형의 순위 전송
                initJobs(roomId);
            }else{
                initDeal(roomId);
            }
        }else{
            finishGame(roomId);
        }
    }

    public void finishGame(String roomId){
        System.out.println("game finished!!!!");
        // 최종 순위와 로그 전송(로그 파싱 쉬우면 그냥 로그만 전송)
        List<Player> playerList = gameService.initOrderWithMoney(roomId);
        template.convertAndSend("/sub/game/finalrank/" + roomId, playerList); // List<Player> 형의 순위 전송
        template.convertAndSend("/sub/game/log/" + roomId, gameService.getNicknames(roomId)); // List<String> 형의 플레이어 리스트 전송
        template.convertAndSend("/sub/game/log/" + roomId, gameService.getLog(roomId)); // int[round][turn][playerIndex] 형의 3차원 배열로 전송

//        List<String> proGangPlayerList = gameService.getProGangPlayer(roomId);
//        template.convertAndSend("/sub/game/winner/" + roomId, gameService.getWinners(roomId)); // 우승자 리스트 전송
//        template.convertAndSend("/sub/game/progang/" + roomId, proGangPlayerList); // 프로깽판러 리스트 전송
        
        // 각 플레이어별 승점 5500 -> 550, 1등 5
//        for(Player player : playerList){
//            userService.setRankPoint(player.getNickName(), player.getMoney()/10);
//        }
//        // 우승 플레이어 가산점
//        for(Player player : gameService.getWinners(roomId)){
//            userService.setRankPoint(player.getNickName(), (int) (player.getMoney()/100));
//        }
//        // 프로깽판러
//        for(String nickname : proGangPlayerList){
//            userService.setProGangAmount(nickname);
//        }


    }
    // 게임 시작 : 게임 기본 정보를 반환 -initGame을 해서 반환 but 딱히 init할 일이 없다    - ㅇ
    // 3초 뒤, 플레이어 능력을 반환 -initJobs, 세 라운드 동안 출력해야 함              - ㅇ
    // 3초 뒤, 순서를 반환(프론트에서는 채팅창에 이를 출력)                             - ㅇ
    
    // 1라운드 1턴의 현재 브로커 및 거래 조건을 생성하여 반환 x 사람 수 x 라운드 수 - ㅇ
    // 프론트에 타이머가 구현되어 있으니까 백에서는 쓰레드를 쓰지 않고 요청만 받기 - ㅇ
    // -> 브로커가 거래 조건을 만족시켜서 요청을 보내거나, 혹은 시간이 초과되었다는 요청을 보냄 - ㅇ
    // 백에서 해당 요청을 받아 결과를 메세지로 전송 - ㅇ
    // 각 플레이어의 프론트에서 출력, 투표 UI 및 타이머 시작 -> 백에서는 투표 결과를 동기로 기다림 - ㅇ
    // 모든 플레이어가 투표를 완료하거나 시간이 초과되면 요청 - ㅇ
    // 시간이 초과되면 투표 거절로 인식하여 최종 결과를 전송 - ㅇ
    // 턴++ - ㅇ
    // 턴이 사람 수 만큼 진행된다면 라운드 ++ 및 턴 = 1, 순위 반환 - ㅇ
    // 이후부터는 2번째 줄부터 다시 진행 - ㅇ
    // ...
    // 마지막 라운드의 마지막 턴이 끝나면 최종 순위 반환 - ㅇ
    // 이때 각 플레이어별 점수, 업적 등의 정보를 DB에서 최신화하여 전송
    
    
    // + 네이버 로그인 합치기
    // + 게임 종료 or 다시 시작 -> 게임 종료 시 gameMap에서 gameDTO 삭제, roomMap에 room 생성(게임 준비 상태로)

    // 상위 랭킹 확인, 유저 정보 반환 부분
//        System.out.println(userService.getRank());
//        System.out.println(userService.getUser("김주호"));
}
