package com.snakebattle.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snakebattle.backend.mapper.UserMapper;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Service
public class GetRanklistServiceImpl implements GetRanklistService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page, 3);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for(User user:users){
            user.setPassword("");  // do not return password to frontend
        }

        resp.put("users",users);
        resp.put("user_count", userMapper.selectCount(null));
        return resp;
    }
}
