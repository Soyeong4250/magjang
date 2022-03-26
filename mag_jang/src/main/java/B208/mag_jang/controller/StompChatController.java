package B208.mag_jang.controller;

import B208.mag_jang.domain.ChatMessageDTO;
import B208.mag_jang.domain.ChatRoomDTO;
import B208.mag_jang.domain.GameDTO;
import B208.mag_jang.domain.RoomMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pub")
@CrossOrigin
public class StompChatController {

    private final SimpMessagingTemplate template;
    private final RoomMap roomMap;
    @Autowired
    private SimpUserRegistry simpUserRegistry;


    // Client가 SEND할 수 있는 경로
    // stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    // "/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        // room에 유저아이디를 넣음
        roomMap.addNickname(message.getRoomId(), message.getWriter());
        template.convertAndSend("/sub/chat/enter/" + message.getRoomId(), message);
        template.convertAndSend("/sub/chat/players/" + message.getRoomId(), roomMap.getNicknames(message.getRoomId()));

    }


    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        System.out.println(message.getWriter() + " : " + message.getMessage());
        System.out.println("/sub/chat/room/" + message.getRoomId());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    // 귓속말 기능
    // "pub/chat/whisper"
    @MessageMapping(value = "/chat/whisper")
    public void whisper(ChatMessageDTO message){
        System.out.println("귓속말 : " + message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId() + "/" + message.getReader(), message);
    }

    // stomp.disconnect 시 quit 메세지 발송
    // "pub/chat/quit"
    @MessageMapping(value = "/chat/quit")
    public void quit(ChatMessageDTO message){
        template.convertAndSend("/sub/chat/quit/" + message.getRoomId(), message);
        
        // room에서 유저아이디 삭제 - ㅇ
        roomMap.removeNickname(message.getRoomId(), message.getWriter());
        template.convertAndSend("/sub/chat/players/" + message.getRoomId(), roomMap.getNicknames(message.getRoomId()));
    }

}
