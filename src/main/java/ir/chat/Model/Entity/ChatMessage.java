package ir.chat.Model.Entity;

import com.google.gson.Gson;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType messageType;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
