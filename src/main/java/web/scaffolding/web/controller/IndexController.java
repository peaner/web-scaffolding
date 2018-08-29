package web.scaffolding.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: lilongzhou
 * @Description:
 * @Date: Created in 16:00 2018/8/10
 */
@Controller
@Slf4j
public class IndexController {


    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public String index() {
        return "login";
    }


}
