package com.snakebattle.backend.service.impl.pk;

import com.snakebattle.backend.consumer.WebSocketServer;
import com.snakebattle.backend.consumer.utils.Game;
import com.snakebattle.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceivedBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String ReceiveBotMove(Integer userId, Integer direction, Integer opponentId) {
        System.out.println("received bot move" + userId + " " + direction);

        // if ai bot, find game by its opponent's id
        if(userId.equals(6)){
            if(WebSocketServer.users.get(opponentId)!=null){
                Game game = WebSocketServer.users.get(opponentId).game;
                if(game!=null){
                    if(game.getPlayerA().getId().equals(userId)){
                        game.setNextStepA(direction); // move according to user input
                    } else if(game.getPlayerB().getId().equals(userId)) {
                        game.setNextStepB(direction);
                    }

                }
            }
        }

        if(WebSocketServer.users.get(userId)!=null){
            Game game = WebSocketServer.users.get(userId).game;
            if(game!=null){
                System.out.println(game.getPlayerB().getId());

                if(game.getPlayerA().getId().equals(userId)){
                    game.setNextStepA(direction); // move according to user input
                    //System.out.println("set a");
                } else if(game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                    //System.out.println("set b");
                }

            }
        }

        return "receive bot move";
    }
}
