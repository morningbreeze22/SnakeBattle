package com.snakebattle.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snakebattle.backend.mapper.BotMapper;
import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.impl.utils.UserUtil;
import com.snakebattle.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {

    @Autowired
    private BotMapper botMapper;
    @Override
    public List<Bot> getlist() {
        User user = UserUtil.getUser();

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());

        return botMapper.selectList(queryWrapper);


    }
}
