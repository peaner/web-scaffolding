package web.scaffolding.web.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import web.scaffolding.core.utils.EnhanceSecurityUtils;
import web.scaffolding.service.user.UserService;
import web.scaffolding.service.vo.UserVO;

@ControllerAdvice
public class AccountControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserVO account() {
        Long userId = EnhanceSecurityUtils.retrieveEnhanceUser().getUserId();
        if (userId == null) {
            return new UserVO();
        }
        UserVO userVO = userService.getById(userId);
        return userVO == null ? new UserVO() : userVO;
    }
}
