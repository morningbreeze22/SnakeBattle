package com.snakebattle.backend.service.user.bot;

import com.snakebattle.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

public interface UpdateService {
   Map<String,String> update(Map<String,String> data);
}
