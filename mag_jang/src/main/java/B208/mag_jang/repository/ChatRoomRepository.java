package B208.mag_jang.repository;

import B208.mag_jang.domain.ChatRoomDTO;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRooms(){
        // 채팅방을 최근 순으로 반환
        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);
        return result;
    }

    public ChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

//    public ChatRoomDTO createChatRoomDTO(String name){
//        ChatRoomDTO room = ChatRoomDTO.create(name);
//        chatRoomDTOMap.put(room.getRoomId(), room);
//
//        return room;
//    }
}
