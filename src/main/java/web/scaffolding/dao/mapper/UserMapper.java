package web.scaffolding.dao.mapper;

import web.scaffolding.domain.User;

import java.util.List;


/**
 * @Author: lilongzhou
 * @Description:
 * @Date: Created in 下午5:48 2018/8/30
 */
public interface UserMapper {

    void update(User user);

    //List<User> getAll(Criteria criteria);

    User getById(Long id);

    void save(User user);

    List<User> getAll();
}
