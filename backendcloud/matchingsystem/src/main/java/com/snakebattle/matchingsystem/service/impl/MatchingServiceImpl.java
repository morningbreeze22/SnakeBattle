package com.snakebattle.matchingsystem.service.impl;

import com.snakebattle.matchingsystem.service.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add user"+userId);
        matchingPool.addPlayer(userId,rating, botId);
        return "success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove user"+userId);
        matchingPool.removePlayer(userId);
        return "success remove";
    }
}
