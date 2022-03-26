package B208.mag_jang.service;

import B208.mag_jang.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameService {
    private final int START_MONEY = 2000;
    private final String[] jobNames = {"언변", "창고", "선박", "로비", "정보", "인맥"};
    private final int[] MIN_DEAL_PLAYERS = {2, 2, 2};
    private final int[] MAX_DEAL_PLAYERS = {3, 3, 4};
    private final int[] MIN_DEAL_MONEY = {1000, 1500, 2000};
    private final int[] MAX_DEAL_MONEY = {2000, 2500, 3000};

    private final RoomMap roomMap;
    private final GameMap gameMap;
    private final AsyncService asyncService;

    // GameDTO 생성 후 GameMap<String roomId, GameDTO gameDTO>에 연결 - ㅇ
    // roomMap의 key 삭제 - ㅇ
    public boolean gameStart(String writer, String roomId) {
        if(roomMap.getNicknames(roomId).size() < 4){
            return false;
        }
        System.out.println(roomMap.getNicknames(roomId));
        gameMap.setNewGame(roomId);
        for(String player : roomMap.getNicknames(roomId)){
            gameMap.addPlayer(roomId, player);
        }

        gameMap.getGame(roomId).initGame(START_MONEY);

        if(gameMap.getGame(roomId) != null){
            roomMap.removeChatRoomDTO(roomId);
        }
        return true;
    }
    public List<Player> getNextJobs(String roomId){
        String[][] jobs = new String[gameMap.getGame(roomId).getPlayerListSize()][2];
        gameMap.getGame(roomId).setCurrJobs(new ArrayList<>());
        List<Player> tempList = new ArrayList<>();
//        tempList.addAll(gameMap.getGame(roomId).initJobs(initJobs(jobs)));
        for(Player player : gameMap.getGame(roomId).initJobs(initJobs(jobs))){
            Player currPlayer = new Player(player.getNickName());
            currPlayer.setJobs(player.getJobs());
            tempList.add(currPlayer);
        }
        return tempList;
    }

    // 모든 플레이어에게 능력을 두 개씩 부여하는 메서드
    // 규칙 1. 한 플레이어에게 동일한 능력 두 개를 줄 수 없음
    // 규칙 2. 완전히 겹치는 플레이어가 생길 수 있음
    // 규칙 3. 한 능력을 플레이어 수 (4, 5, 6) 당 (3, 3, 4) 개 초과하여 가질 수 없다.
    private String[][] initJobs(String[][] jobs) {
        Random random = new Random();
        outer : while (true){
            int[] jobArray = new int[6]; // {0, 0, 0, 0, 0, 0}
            for(int i = 0; i<jobs.length;i++){
                int first = random.nextInt(6);
                int second = random.nextInt(6);
                while (second == first){
                    first = random.nextInt(6);
                    second = random.nextInt(6);
                }
                jobArray[first]++;
                jobArray[second]++;
                System.out.println(i+1 + "번째 플레이어 : " + jobNames[first] + ", " + jobNames[second]);
                jobs[i][0] = jobNames[first];
                jobs[i][1] = jobNames[second];
            }
            for(int job : jobArray){
                if(job>jobs.length-(jobs.length/6)-1){
                    continue outer;
                }
            }
            break;
        }

        return jobs;
    }

    public GameDTO getGame(String roomId) {
        return gameMap.getGame(roomId);
    }

    // 첫 라운드 첫 턴에만 랜덤으로 Player 리스트를 반환
    // 이후에는 플레이어 돈의 내림차순으로 반환
    // -> 순위 발표 및 다음 라운드 순서 결정 시 활용
    public List<Player> initOrderWithMoney(String roomId) {
        int round = gameMap.getGame(roomId).getRound();
        int turn = gameMap.getGame(roomId).getTurn();
        List<Player> order = new ArrayList<>();

        order.addAll(gameMap.getGame(roomId).getPlayerList());
//        for(Player player : gameMap.getGame(roomId).getPlayerList()){
//            order.add(player);
//        }
        if(round==1 && turn==1){
            Collections.shuffle(order);
        }else{
            Collections.sort(order);
        }
        gameMap.getGame(roomId).setOrder(order);
        return order;

    }

    public List<String> initOrder(String roomId) {
        List<String> rankList = new ArrayList<>();
        for(Player player : initOrderWithMoney(roomId)){
            rankList.add(player.getNickName());
        }
        return rankList;
    }

    public DealDTO initDeal(String roomId){
        GameDTO game = gameMap.getGame(roomId);
        List<Player> playerList = game.getPlayerList();
        List<String> currJobs = game.getCurrJobs();
        Player broker = game.getOrder().get(game.getTurn()-1);
        System.out.println("currJobs : " + currJobs);
        Random random = new Random();
        int n = game.getPlayerListSize();

        int ranInt = Math.min(currJobs.size(), MIN_DEAL_PLAYERS[n-4] + random.nextInt(MAX_DEAL_PLAYERS[n-4]-MIN_DEAL_PLAYERS[n-4]+1));

        List<String> chosenJobs = choiceJobs(broker, ranInt, currJobs);
        System.out.println(ranInt + ", chosenJobs : " + chosenJobs); // -ㅇ

        int count = Math.max(2, countPlayer(broker, playerList, chosenJobs));
        DealDTO deal = new DealDTO(count, chosenJobs);
        deal.setDealMoney(MIN_DEAL_MONEY[game.getRound()-1] + random.nextInt(11)*100 + (count>3?300:(count>2?200:0))); //인원별 보너스
        game.setDeal(deal);
        System.out.println(deal);
        return deal;

    }
    // 브로커 : 1, 브로커 jobs : (a, b) , playerList : {1, 2, 3, 4, 5, 6}, chosenJobs : {a, b, c, d}
    private int countPlayer(Player broker, List<Player> playerList, List<String> chosenJobs) {
        List<Player> tempPlayerList = new ArrayList<>();
        List<String> tempChosenJobs = new ArrayList<>();
        tempPlayerList.addAll(playerList);
        tempChosenJobs.addAll(chosenJobs);
        System.out.println("broker : " + broker);
        int retVal = 1;
        for(Player player : tempPlayerList){
            if(player.getNickName().equals(broker.getNickName())){
                tempPlayerList.remove(player);
                if(tempChosenJobs.contains(broker.getJobs()[0])){
                    tempChosenJobs.remove(broker.getJobs()[0]);
                }
                if(tempChosenJobs.contains(broker.getJobs()[1])){
                    tempChosenJobs.remove(broker.getJobs()[1]);
                }
                break;
            }
        }
        Collections.shuffle(tempPlayerList);
        boolean isRemoved = false;
        for(Player player : tempPlayerList){
            if(tempChosenJobs.contains(player.getJobs()[0])){
                tempChosenJobs.remove(player.getJobs()[0]);
                isRemoved = true;
            }
            if(tempChosenJobs.contains(player.getJobs()[1])){
                tempChosenJobs.remove(player.getJobs()[1]);
                isRemoved = true;
            }
            if(isRemoved) retVal++;
            isRemoved = false;

            if(tempChosenJobs.isEmpty()) break;
        }

        return retVal;
    }

    //랜덤 뽑기
    public List<String> choiceJobs(Player broker, int n, List<String> currJobs){
        List<String> retList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        tempList.addAll(currJobs); // 깊은 복사인지 확인 필요
        Random random = new Random();
        String brokerJob = broker.getJobs()[random.nextInt(2)];
        tempList.remove(brokerJob);
        retList.add(brokerJob);
        while (retList.size() < n){
            int index = random.nextInt(tempList.size());
            retList.add(tempList.get(index));
            tempList.remove(tempList.get(index));
        }

        if(n==2 && retList.contains(broker.getJobs()[0]) && retList.contains(broker.getJobs()[1])){
            return choiceJobs(broker, n, currJobs);
        }

        return retList;
    }

    public void setDealAmount(String roomId, List<ArrayList<Object>> moneys) {
        Map<String, Integer> moneyMap = new HashMap<>();
        for(ArrayList<Object> money : moneys){
            moneyMap.put((String)money.get(0), (Integer)money.get(1));
        }
        gameMap.getGame(roomId).getDeal().setDealAmount(moneyMap);
    }

    public void startNext(String roomId) {
        gameMap.getGame(roomId).nextTurn();
    }
    public boolean isGameFinished(String roomId){
        return gameMap.getGame(roomId).isGameFinished();
    }


    public void updatePlayerMoney(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        Map<String, Integer> dealAmount = game.getDealAmount();
        for(Player player : game.getPlayerList()){
            if(dealAmount.containsKey(player.getNickName())){
                player.setMoney(player.getMoney()+dealAmount.get(player.getNickName()));
            }
        }
    }

    public int[][][] getLog(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        return game.getGameLog();
    }

    public List<String> getNicknames(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        List<String> nicknameList = new ArrayList<>();
        for(Player player : game.getPlayerList()){
            nicknameList.add(player.getNickName());
        }
        return nicknameList;
    }

    public void updateGameLog(String roomId) {
        GameDTO game = gameMap.getGame(roomId);

        for(int i = 0; i < game.getPlayerListSize(); i++){
            Player player = game.getPlayerList().get(i);
            if(game.getDealAmount().containsKey(player.getNickName()) && game.getRound() < 4){
                game.getGameLog()[game.getRound()-1][game.getTurn()-1][i] = game.getDealAmount().get(player.getNickName());
            }
        }

    }

    public List<String> getProGangPlayer(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        int max = 0;
        List<String> proGangList = new ArrayList<>();
        for(Player player : game.getPlayerList()){
            if(player.getGangAmount() == max && max > 0){
                proGangList.add(player.getNickName());
            }else if(player.getGangAmount() > max){
                max = player.getGangAmount();
                proGangList.clear();
                proGangList.add(player.getNickName());
            }
        }
        return proGangList;
    }

    public List<Player> getWinners(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        List<Player> winnerList = new ArrayList<>();
        int max = 0;
        for(Player player : game.getPlayerList()){
            if(player.getMoney() == max && max > 0){
                winnerList.add(player);
            }else if(player.getMoney() > max){
                max = player.getMoney();
                winnerList.clear();
                winnerList.add(player);
            }
        }
        return winnerList;
    }

    public Player getCurrBroker(String roomId) {
        GameDTO game = gameMap.getGame(roomId);
        return new Player(game.getOrder().get(game.getTurn()-1).getNickName());
    }
}