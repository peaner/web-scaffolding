package web.scaffolding.service.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserVO {

    private long id;

    private Date createTime;

    private String username;

    private String nickname;

    private String mobile;

    private boolean disabled = false;

    private boolean manager = false;

    // 用户类型 0 超级管理员 1 操作管理员
    private Integer userType = 1;

}
