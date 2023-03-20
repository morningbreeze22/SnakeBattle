package com.snakebattle.backend.controller.user.bot;

import com.snakebattle.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @PostMapping("/api/user/bot/update/")
    public Map<String,String> update(@RequestBody Map<String,String> data){
        return updateService.update(data);
    }
}
