package B208.mag_jang.domain;

import org.springframework.web.socket.WebSocketSession;

import java.util.*;


public class ChatRoomDTO {
    private String roomId;
    private List<String> nicknames;

    public ChatRoomDTO(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getNicknames() {
        return this.nicknames;
    }

    public void addNickname(String nickname){
        if(this.nicknames==null) {
            this.nicknames = new ArrayList<>();
        }
        if(!this.nicknames.contains(nickname)) this.nicknames.add(nickname);
    }

    public void removeNickname(String nickname) {
        if(this.nicknames==null) {
            System.out.println("ChatRoomDTO : " + nickname + "의 quit 요청, nicknames가 null 입니다.");
            return;
        }
        this.nicknames.remove(nickname);
        System.out.println(nickname +""+ this.nicknames);
    }
//    public static ChatRoomDTO create(String name){
//        ChatRoomDTO room = new ChatRoomDTO();
//        room.roomId = UUID.randomUUID().toString();
//        room.name = name;
//        return room;
//    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "ChatRoomDTO{" +
                "roomId='" + roomId + '\'' +
                '}';
    }

}
