package com.snakebattle.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snakebattle.backend.mapper.BotMapper;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.impl.utils.UserUtil;
import com.snakebattle.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        User user = UserUtil.getUser();

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

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        if(botMapper.selectCount(queryWrapper)>=10){
            map.put("error_message","Max bot number is 10");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,now, now);

        botMapper.insert(bot);
        map.put("error_message","success");

        return map;
    }

}
