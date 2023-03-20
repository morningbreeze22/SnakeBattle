package com.snakebattle.backend.controller.user.bot;

import com.snakebattle.backend.pojo.Bot;
import com.snakebattle.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {
    @Autowired
    private GetListService getListService;

    @GetMapping("/api/user/bot/getlist/")
    public List<Bot> getList(){
        return getListService.getlist();
    }
}
