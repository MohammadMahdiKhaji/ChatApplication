package ir.chat.Model.Utils;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/letterEndPoint", configurator = LetterSocketConfiguration.class)
public class LetterSocket {

    private static HashMap<HttpSession, Session> hashMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Socket Opened");
        HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
        hashMap.put(httpSession, session);
        System.out.println("Session :"+httpSession.getId() );
        System.out.println("Socket : "+session.getId());
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Socket Closed");
        ( (HttpSession) session.getUserProperties().get(HttpSession.class.getName())).invalidate();
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("Message : " + message);
    }

    @OnError
    public void onError(Throwable e){
        System.out.println("Error");
    }

    public static void send(HttpSession httpSession, String message) throws IOException {
        hashMap.get(httpSession).getBasicRemote().sendText(message);
    }

    public static void broadCast(String jsonText) throws IOException {
        for (Map.Entry<HttpSession, Session> httpSessionSessionEntry : hashMap.entrySet()) {
            httpSessionSessionEntry.getValue().getBasicRemote().sendText(jsonText);
        }
    }
}
