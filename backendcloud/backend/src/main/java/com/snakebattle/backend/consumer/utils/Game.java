package com.snakebattle.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.snakebattle.backend.consumer.WebSocketServer;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.Record;
import com.snakebattle.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    final private Player playerA;
    final private Player playerB;

    // 0-UP
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing";
    private String loser = ""; // all/A/B

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer aBotId = -1;
        Integer bBotId = -1;
        String aBotCode ="";
        String bBotCode="";
        if(botA!=null){
            aBotId = botA.getId();
            aBotCode = botA.getContent();
        }
        if(botB!=null){
            bBotId = botB.getId();
            bBotCode = botB.getContent();
        }


        playerA = new Player(idA, aBotId, aBotCode, rows-2, 1, new ArrayList<>());
        playerB = new Player(idB, bBotId, bBotCode, 1, cols-2, new ArrayList<>());
    }

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }

    public void setNextStepA(Integer nextStepA){
        lock.lock();

        try{
            this.nextStepA = nextStepA;
        } finally{
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB){
        lock.lock();

        try{
            this.nextStepB = nextStepB;
        } finally{
            lock.unlock();
        }

    }

    public int[][] getGameMap() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean generateMao() {  // try to generate a map
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (generateMao())
                break;
        }
    }

    private String getInput(Player player){
        // encode game info as string
        // format: assume a is player, b is opponent
        // gamemap#a.sx#a.sy#(a.steps)#b.sx#b.sy#(b.steps)

        Player myself, opponent;

        if(playerA.getId().equals(player.getId())){
            myself = playerA;
            opponent = playerB;
        } else{
            myself = playerB;
            opponent = playerA;
        }

        return getMapString() + "#" + myself.getSx() + "#" + myself.getSy() + "#(" + myself.getStepsString() + ")#" +
                opponent.getSx() + "#" + opponent.getSy() + "#(" + opponent.getStepsString() + ")";


    }

    private void sendBotCode(Player player, Player opponent){
        if(player.getBotId().equals(-1)) return;    // do not neet to execute code
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        data.add("opponent_id",opponent.getId().toString());
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    // wait for next step
    private boolean nextStep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA, playerB);
        sendBotCode(playerB, playerA);

        for(int i=0;i<50;i++) {
            try {
                Thread.sleep(100);

                // lock
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // send result to 2 clients
    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private void sendAllMessage(String message){
        if(WebSocketServer.users.get(playerA.getId())!=null) WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId())!=null) WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    // send move to 2 clients
    private void sendMove(){
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally{
            lock.unlock();
        }

    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n-1);

        // collision with wall
        if(g[cell.x][cell.y]==1) return false;

        // collision with snake
        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y==cell.y){
                return false;
            }
        }

        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y==cell.y){
                return false;
            }
        }

        return true;
    }

    // detect collision
    private void judge(){
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA,cellsB);
        boolean validB = checkValid(cellsB,cellsA);

        if(!validB || !validA){
            status = "finished";

            if(!validB && !validA){
                loser = "all";
            } else if(!validA){
                loser = "A";
            } else{
                loser = "B";
            }


        }
    }

    private String getMapString(){
        StringBuilder res = new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    // save data to record table
    private void saveToDatabase(){

        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if("A".equals(loser)){
            ratingA -= 3;
            ratingB += 5;
        } else if("B".equals(loser)){
            ratingA += 5;
            ratingB -= 3;
        }

        updateUserRating(playerA, ratingA);
        updateUserRating(playerB, ratingB);



        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);

    }

    private void updateUserRating(Player player, Integer rating){
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    @Override
    public void run() {
        // assume one game will not have more than 1000 steps
        for(int i=0;i<1000;i++){
            if(nextStep()){

                judge();
                if(status.equals("playing")){
                    sendMove();
                } else{
                    sendResult();
                    break;
                }

            }else{
                status = "finished";
                lock.lock();
                try{
                    if(nextStepA==null && nextStepB==null){
                        this.loser = "all";
                    } else if(nextStepA==null){
                        this.loser = "A";
                    } else{
                        this.loser = "B";
                    }
                } finally{
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
