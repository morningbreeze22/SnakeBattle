package com.snakebattle.backend.service.impl.user.bot;

import com.snakebattle.backend.mapper.BotMapper;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.impl.utils.UserUtil;
import com.snakebattle.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        User user = UserUtil.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String,String> map = new HashMap<>();

        if(title ==null || title.length()==0){
            map.put("error_message","title cannot be null");
            return map;
        }

        if(title.length()>100){
            map.put("error_message","max length of title is 100");
            return map;
        }

        if(description!=null && description.length()>300){
            map.put("error_message","max length of description is 300");
            return map;
        }

        if(description==null || description.length()==0){
            description = "This is description.";
        }

        if(content==null || content.length()==0){
            map.put("error_message","code cannot be null");
            return map;
        }

        if(content.length()>10000){
            map.put("error_message","max length of code is 10000");
            return map;
        }

        Bot bot = botMapper.selectById(bot_id);
        if(bot==null){
            map.put("error_message","bot does not exists");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","you cannot update this bot");
        }

        Bot new_bot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getCreateTime(),
                new Date()
        );

        botMapper.updateById(new_bot);

        map.put("error_message","success");

        return map;

    }
}
