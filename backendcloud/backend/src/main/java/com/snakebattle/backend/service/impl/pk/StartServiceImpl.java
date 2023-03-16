package com.snakebattle.backend.service.impl.pk;

import com.snakebattle.backend.consumer.WebSocketServer;
import com.snakebattle.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartServiceImpl implements StartGameService {
    @Override
    public String StartGame(Integer aId, Integer bId) {
        System.out.println("start game" + aId + " " + bId);
        WebSocketServer.startGame(aId,bId);
        return "start one game";
    }
}
