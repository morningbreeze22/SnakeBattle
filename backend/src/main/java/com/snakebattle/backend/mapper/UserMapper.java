package com.snakebattle.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snakebattle.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
