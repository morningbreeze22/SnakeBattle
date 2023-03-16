package com.snakebattle.matchingsystem.controller;

import com.snakebattle.matchingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    @PostMapping("/player/add/")
    public String addPlayer(@RequestBody MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(data.getFirst("user_id"));
        Integer rating = Integer.parseInt(data.getFirst("rating"));

        return matchingService.addPlayer(userId,rating);
    }

    @PostMapping("/player/remove")
    public String removePlayer(@RequestBody MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(data.getFirst("user_id"));
        return matchingService.removePlayer(userId);
    }
}
