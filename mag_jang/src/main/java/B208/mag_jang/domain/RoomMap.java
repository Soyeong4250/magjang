package B208.mag_jang.domain;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoomMap {
    private Map<String, ChatRoomDTO> rooms = new HashMap<>();

    public void addNickname(String roomId, String writer) {
        if(rooms.get(roomId)==null) rooms.put(roomId, new ChatRoomDTO(roomId));
        rooms.get(roomId).addNickname(writer);
    }

    public void removeNickname(String roomId, String writer) {
        if(rooms.get(roomId)==null) {
            System.out.println("RoomMap : " + writer + "의 quit 요청, " + roomId + "가 null 입니다.");
            return;
        }
        rooms.get(roomId).removeNickname(writer);
    }

    public List<String> getNicknames(String roomId) {
        return rooms.get(roomId).getNicknames();
    }

    public void removeChatRoomDTO(String roomId){
        rooms.remove(roomId);
    }
}
