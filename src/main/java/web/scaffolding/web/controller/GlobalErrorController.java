package web.scaffolding.web.controller;

import com.google.common.collect.Maps;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class GlobalErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public Map<String, Object> index() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("success", false);
        map.put("code", 404);
        map.put("msg", "RESOURCE_NOT_FOUND");
        return map;
    }
}
