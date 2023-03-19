package com.snakebattle.backend.consumer;
import com.alibaba.fastjson2.JSONObject;
import com.snakebattle.backend.consumer.utils.Game;
import com.snakebattle.backend.consumer.utils.JwtAuthentication;
import com.snakebattle.backend.mapper.BotMapper;
import com.snakebattle.backend.mapper.RecordMapper;
import com.snakebattle.backend.mapper.UserMapper;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // not end with /
public class WebSocketServer {

    // used to map user id into websocket server instance
    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    // users ready to match, move to matching system
    //final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private Session session = null;
    private User user;

    public static UserMapper userMapper;
    public static RecordMapper recordMapper;

    private static BotMapper botMapper;

    public static RestTemplate restTemplate;

    public Game game = null;

    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";

    // need to have this function cause it is not a singleton pattern
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){WebSocketServer.recordMapper= recordMapper;}

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper){
        WebSocketServer.botMapper = botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("connected");

        // if assume token is user id
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if(this.user!=null){
            users.put(userId,this);
        }else{
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected");
        if(this.user!=null){
            users.remove(this.user.getId());
            //matchpool.remove(this.user);
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId){
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);


        Game game = new Game(13,14,20, a.getId(), botA, b.getId(), botB);
        game.createMap();

        // start a thread
        game.start();

        if(users.get(a.getId())!=null) users.get(a.getId()).game = game;
        if(users.get(b.getId())!=null)  users.get(b.getId()).game = game;


        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getGameMap());



        JSONObject respA = new JSONObject();
        respA.put("event","start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);
        respA.put("snake","0");
        if(users.get(a.getId())!=null) users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event","start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        respB.put("snake","1");
        if(users.get(b.getId())!=null) users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId){
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id", botId.toString());

        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching(){
        //matchpool.remove(this.user);
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            if(game.getPlayerA().getBotId().equals(-1)){
                game.setNextStepA(direction); // move according to user input
            }

        } else if(game.getPlayerB().getId().equals(user.getId())){
            if(game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        } else{
            System.out.println("cannot move");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        System.out.println("received");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching(data.getInteger("bot_id"));
        } else if ("stop-matching".equals(event)){
            stopMatching();
        } else if ("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
