package com.hellodk.wb_hotsearch.Controller;

import cn.hutool.core.map.MapUtil;
import com.hellodk.wb_hotsearch.Bean.Result;
import com.hellodk.wb_hotsearch.Service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: hellodk
 * @description main controller
 * @date: 10/18/2021 4:38 PM
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {

    @Autowired
    private MainService mainService;

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Result getData(@RequestParam Map<String, Object> map) {
        return mainService.getContent(map);
    }

//    @ResponseBody
//    @RequestMapping(value = "/addData", method = RequestMethod.GET)
//    public Result addData() {
//        return mainService.pushNjHotSearchToTelegram();
//    }
}
