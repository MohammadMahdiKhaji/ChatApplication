package ir.chat.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ir.chat.Model.Entity.ChatMessage;
import ir.chat.Model.Entity.MessageType;
import ir.chat.Model.Utils.LetterSocket;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet(urlPatterns = "/app/*", name = "app")
public class ChatController extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse rsp) {
        String requestURI = req.getRequestURI();
        if (requestURI.endsWith("/chat.sendMessage")) {
            sendMessage(req, rsp);
        } else if (requestURI.endsWith("/chat.addUser")) {
            addUser(req, rsp);
        }
    }
    @SneakyThrows
    public void sendMessage(HttpServletRequest req, HttpServletResponse rsp) {
        InputStream inputStream = req.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonText = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonText.append(line);
        }
        reader.close();
        inputStream.close();
//        Gson gson = new Gson();
//        ChatMessage data = gson.fromJson(String.valueOf(jsonText), ChatMessage.class);
        System.out.println(jsonText.toString());
//        System.out.println(data.toString());
        LetterSocket.broadCast(jsonText.toString());
    }

    @SneakyThrows
    public void addUser(HttpServletRequest req, HttpServletResponse rsp) {
//        {"sender":"username","type":"JOIN"}
        InputStream inputStream = req.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonText = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonText.append(line);
        }
        reader.close();
        inputStream.close();
//        Gson gson = new Gson();
//        ChatMessage data = gson.fromJson(String.valueOf(jsonText), ChatMessage.class);
        System.out.println(jsonText.toString());
//        System.out.println(data.toString());
//        req.getSession().setAttribute("username",data.getSender());
    }
}
