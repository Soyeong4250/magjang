package B208.mag_jang.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageDTO {
    private String roomId;
    private String writer;
    private String reader;
    private Object message;

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "roomId='" + roomId + '\'' +
                ", writer='" + writer + '\'' +
                ", reader='" + reader + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
