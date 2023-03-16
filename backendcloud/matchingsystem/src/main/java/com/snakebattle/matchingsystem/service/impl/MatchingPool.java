package com.snakebattle.matchingsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{

    // manually do thread safety
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;

    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating){
        lock.lock();
        try{
            players.add(new Player(userId,rating,0));
        } finally{
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();
            for(Player player:players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally{
            lock.unlock();
        }
    }

    // increase waiting time for all users in matching pool
    private void increaseWaitingTime(){
        for(Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

    private void matchPlayers(){
        boolean[] used = new boolean[players.size()];
        for(int i=0;i<players.size();i++){
            if(used[i]) continue;
            for(int j=i+1;j<players.size();j++){
                if(used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if(checkMatch(a,b)){
                    used[i] = used[j] = true;
                    sendResult(a,b);
                    break;
                }
            }
        }
        // delete matched players
        List<Player> newPlayers = new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    // rule for matching players based on waiting time and rating
    private boolean checkMatch(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating()-b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(),b.getWaitingTime());
        return ratingDelta<=waitingTime*10;
    }

    private void sendResult(Player a, Player b){
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();

        data.add("a_id",a.getUserId().toString());
        data.add("b_id",b.getUserId().toString());

        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
