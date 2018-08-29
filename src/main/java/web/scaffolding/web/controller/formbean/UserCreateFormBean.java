package web.scaffolding.web.controller.formbean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateFormBean {
    private Long id;

    private String username;

    private String nickname;

    private String mobile;

    private String passwd;

    // 用户类型 0 超级管理员 1 操作管理员
    private Integer userType = 1;

}
