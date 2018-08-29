package web.scaffolding.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 可登陆网站的用户
 */
@Table(name = "user")
@Entity
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //用户名
    @Column(unique = true)
    private String username;

    private String nickname;

    //密码
    private String passwd;

    //手机号
    private String mobile;

    private Date createTime;

    @Column(columnDefinition = "tinyint(4) default 0")
    private boolean disabled = false;

    @Column(columnDefinition = "tinyint(4) default 0")
    private boolean manager = false;

    @Column(columnDefinition = "tinyint(4) default 0")
    private boolean deleted = false;
}
