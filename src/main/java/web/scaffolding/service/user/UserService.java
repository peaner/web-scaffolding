package web.scaffolding.service.user;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.scaffolding.core.exception.ExceptionCodeEnum;
import web.scaffolding.core.exception.ZwtException;
import web.scaffolding.core.page.PageUtils;
import web.scaffolding.core.utils.ContentCleanUtils;
import web.scaffolding.dao.UserRepository;
import web.scaffolding.dao.mapper.UserMapper;
import web.scaffolding.domain.QUser;
import web.scaffolding.domain.User;
import web.scaffolding.service.vo.UserVO;
import web.scaffolding.web.controller.formbean.UserCreateFormBean;
import web.scaffolding.web.controller.formbean.UserSearchFormBean;
import web.scaffolding.core.page.PageWrapper;
import web.scaffolding.core.utils.BeanCopyUtils;
import web.scaffolding.core.utils.QuerydslUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // mybatis
    @Autowired
    private UserMapper userMapper;

    public PageWrapper<UserVO> page(Pageable pageable, UserSearchFormBean formBean, Long userId) {
        String mobile = formBean.getMobile();
        String nickname = formBean.getNickname();
        String username = formBean.getUsername();

        BooleanExpression expression = QuerydslUtils.and(null, QUser.user.deleted.eq(false));
        Optional<User> byId = userRepository.findById(userId);

        if (byId.isPresent()) {
            User user = byId.get();
            if (!user.isManager()) {
                expression = QuerydslUtils.and(expression, QUser.user.manager.eq(false));
            }
        }
        if (StringUtils.isNotBlank(mobile)) {
            expression = QuerydslUtils.and(expression, QUser.user.mobile.like(QuerydslUtils.buildLikeParam(mobile.trim())));
        }
        if (StringUtils.isNotBlank(nickname)) {
            expression = QuerydslUtils.and(expression, QUser.user.nickname.like(QuerydslUtils.buildLikeParam(nickname.trim())));
        }
        if (StringUtils.isNotBlank(username)) {
            expression = QuerydslUtils.and(expression, QUser.user.username.like(QuerydslUtils.buildLikeParam(username.trim())));
        }

        Page<User> page = userRepository.findAll(expression, pageable);
        return new PageWrapper<>(page.map(this::retrieveUserVO));
    }


    public UserVO getById(Long userId) {
        User user = userRepository.getOne(userId);
        return retrieveUserVO(user);
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserVO retrieveUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = BeanCopyUtils.copy(user, UserVO.class);
        if (user.isManager()) {
            userVO.setUserType(0);
        } else {
            userVO.setUserType(1);
        }
        return userVO;
    }

    public void disable(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            user.setDisabled(!user.isDisabled());
            userRepository.save(user);
        }
    }

    @Transactional
    public void update(UserCreateFormBean formBean) throws ZwtException {
        String username = ContentCleanUtils.clean(formBean.getUsername());
        String nickname = ContentCleanUtils.clean(formBean.getNickname());
        String mobile = formBean.getMobile();
        Integer userType = formBean.getUserType();
        QUser qUser = QUser.user;
        if (StringUtils.isBlank(username) || StringUtils.isBlank(nickname) || StringUtils.isBlank(mobile)) {
            throw new ZwtException(ExceptionCodeEnum.REQUEST_ERROR);
        }
        Predicate predicate = qUser.id.eq(formBean.getId());
        Optional<User> op = userRepository.findOne(predicate);
        if (op.isPresent()) {
            User user = op.get();
            if (!user.getUsername().equalsIgnoreCase(username)) {
                QUser qUsers = QUser.user;
                Predicate predicates = qUsers.username.eq(username);
                Optional<User> ops = userRepository.findOne(predicates);
                if (ops.isPresent()) {
                    throw new ZwtException(ExceptionCodeEnum.USER_EXIST_ERROR);
                }
            }
            if (StringUtils.isNotEmpty(mobile)) {
                user.setMobile(mobile);
            }
            if (StringUtils.isNotEmpty(nickname)) {
                user.setNickname(nickname);
            }
            if (StringUtils.isNotEmpty(username)) {
                Predicate namePre = qUser.username.eq(username);
                if (!userRepository.findOne(namePre).isPresent()) {
                    user.setUsername(username);
                }
            }
            if (userType == 0) {
                user.setManager(true);
            } else {
                user.setManager(false);
            }
            userRepository.save(user);
        }
    }

    @Transactional
    public void disableUser(Long userId, boolean operation) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            if (!operation) {
                user.setDisabled(false);
                user.setDeleted(false);
            } else {
                user.setDisabled(true);
                user.setDeleted(true);
            }
            userRepository.save(user);
        }
    }

    @Transactional
    public void addUser(UserCreateFormBean formBean) throws ZwtException {
        String username = ContentCleanUtils.clean(formBean.getUsername());
        String nickname = ContentCleanUtils.clean(formBean.getNickname());
        String mobile = formBean.getMobile();
        String password = encoder.encode(formBean.getPasswd());
        Integer userType = formBean.getUserType();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(nickname) || StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
            throw new ZwtException(ExceptionCodeEnum.REQUEST_ERROR);
        }
        QUser qUser = QUser.user;
        Predicate predicate = qUser.username.eq(username);
        Optional<User> op = userRepository.findOne(predicate);
        if (op.isPresent()) {
            throw new ZwtException(ExceptionCodeEnum.USER_EXIST_ERROR);
        }
        User user = new User();
        user.setNickname(nickname);
        user.setUsername(username);
        user.setPasswd(password);
        user.setMobile(mobile);
        if (userType == 0) {
            user.setManager(true);
        } else {
            user.setManager(false);
        }
        user.setDisabled(false);
        user.setDeleted(false);
        user.setCreateTime(new Date());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) throws ZwtException {
        QUser qUser = QUser.user;
        Predicate predicate = qUser.deleted.eq(false)
                .and(qUser.id.eq(userId));
        Optional<User> op = userRepository.findOne(predicate);
        if (op.isPresent()) {
            User user = op.get();
            user.setId(userId);
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new ZwtException(ExceptionCodeEnum.USER_NOT_FOUND);
        }
    }

    public PageUtils<UserVO> userList(Pageable pageable, UserSearchFormBean userSearchFormBean) {
        List<User> users = userMapper.getAll();
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : users) {
            userVOList.add(BeanCopyUtils.copy(user, UserVO.class));
        }
        return new PageUtils(pageable, userVOList, userVOList.size());
    }
}
