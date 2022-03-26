package B208.mag_jang.controller;

import B208.mag_jang.domain.ChatRoomDTO;
import B208.mag_jang.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
public class RoomController {

    private final ChatRoomRepository repository;

    //채팅방 목록 조회 - rest
    @GetMapping(value = "/rooms")
    public ResponseEntity<List<ChatRoomDTO>> rooms(){
        List<ChatRoomDTO> list = repository.findAllRooms();
        if(list.size()==0){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(list);
        }
    }


    //채팅방 개설 - rest
//    @PostMapping(value = "/room")
//    public ResponseEntity<ChatRoomDTO> create(@RequestParam String name){
//        ChatRoomDTO room = repository.createChatRoomDTO(name);
//        if(room == null){
//            return ResponseEntity.noContent().build();
//        }else{
//            return ResponseEntity.ok(room);
//        }
//    }

    //채팅방 조회 - rest
    @GetMapping("/room")
    public ResponseEntity<ChatRoomDTO> getRoom(String roomId){
        ChatRoomDTO room = repository.findRoomById(roomId);
        if(room == null){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(room);
        }
    }
}
