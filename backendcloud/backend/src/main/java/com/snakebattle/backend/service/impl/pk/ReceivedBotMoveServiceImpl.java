package com.snakebattle.backend.service.impl.pk;

import com.snakebattle.backend.consumer.WebSocketServer;
import com.snakebattle.backend.consumer.utils.Game;
import com.snakebattle.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceivedBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String ReceiveBotMove(Integer userId, Integer direction) {
        System.out.println("received bot move" + userId);

        if(WebSocketServer.users.get(userId)!=null){
            Game game = WebSocketServer.users.get(userId).game;
            if(game!=null){

                if(game.getPlayerA().getId().equals(userId)){
                    game.setNextStepA(direction); // move according to user input
                } else if(game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }

            }
        }

        return "receive bot move";
    }
}
