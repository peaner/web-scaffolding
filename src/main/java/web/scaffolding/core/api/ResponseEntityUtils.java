package web.scaffolding.core.api;

import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import web.scaffolding.core.exception.ExceptionCodeEnum;

import java.util.Map;

public class ResponseEntityUtils {

    public static ResponseEntity<Object> buildSuccessEntity(Object info) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", ExceptionCodeEnum.SUCCESS.getCode());
        map.put("msg", ExceptionCodeEnum.SUCCESS.toString());
        map.put("info", info);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> buildFailEntity(String message, ExceptionCodeEnum code) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", code.getCode());
        map.put("msg", message);
        return new ResponseEntity<>(map, code.getHttpStatus());
    }

    public static ResponseEntity<Object> buildFailEntity(String message) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", ExceptionCodeEnum.SERVER_ERROR.getCode());
        map.put("msg", message);
        return new ResponseEntity<>(map, ExceptionCodeEnum.SERVER_ERROR.getHttpStatus());
    }
}
