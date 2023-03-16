package com.snakebattle.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snakebattle.backend.pojo.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RecordMapper extends BaseMapper<Record> {
}
