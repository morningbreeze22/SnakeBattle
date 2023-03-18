package com.snakebattle.botrunningsystem.service.impl;

import com.snakebattle.botrunningsystem.service.BotRunningService;
import com.snakebattle.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input, Integer opponent) {
        botPool.addBot(userId,botCode,input, opponent);
        return "success add bot";
    }
}
