package com.snakebattle.backend.service.impl.user.bot;

import com.snakebattle.backend.mapper.BotMapper;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.impl.utils.UserUtil;
import com.snakebattle.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        User user = UserUtil.getUser();
        int bot_id = Integer.parseInt(data.get("bot_id"));

        Bot bot = botMapper.selectById(bot_id);

        Map<String,String> map = new HashMap<>();

        if(bot==null){
            map.put("error_message","this bot does not exists");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","this bot does not belongs to you!");
            return map;
        }

        botMapper.deleteById(bot_id);

        map.put("error_message","success");
        return map;
    }
}
