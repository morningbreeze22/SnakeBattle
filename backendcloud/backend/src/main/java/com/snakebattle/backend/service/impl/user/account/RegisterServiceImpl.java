package com.snakebattle.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snakebattle.backend.mapper.UserMapper;
import com.snakebattle.backend.pojo.User;
import com.snakebattle.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String,String> map = new HashMap<>();
        if(username==null){
            map.put("error_message","user name can not be empty!");
            return map;
        }

        if(password==null || confirmedPassword==null){
            map.put("error_message","password cannot be empty!");
            return map;
        }

        username = username.trim();
        if(username.length()==0){
            map.put("error_message","user name cannot be spaces");
            return map;
        }

        if(username.length()>100){
            map.put("error_message","user name must be shorter than 100");
            return map;
        }

        if(password.length()==0 || confirmedPassword.length()==0){
            map.put("error_message","password cannot be empty");
            return map;
        }

        if(password.length()>100 || confirmedPassword.length()>100){
            map.put("error_message","password must be shorter than 100");
            return map;
        }

        if(!password.equals(confirmedPassword)){
            map.put("error_message","2 password do not match");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",username);
        List<User> users = userMapper.selectList(queryWrapper);

        if(!users.isEmpty()){
            map.put("error_message","user already exists");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/63733_lg_269b992244.jpg";

        User user = new User(null,username,encodePassword,photo, 1500);
        userMapper.insert(user);

        map.put("error_message","success");
        return map;


    }
}
