package web.scaffolding.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.scaffolding.core.exception.ZwtException;
import web.scaffolding.web.advice.aop.DuplicateSubmitToken;
import web.scaffolding.core.page.PageWrapper;
import web.scaffolding.core.utils.EnhanceSecurityUtils;
import web.scaffolding.service.user.CustomUserDetail;
import web.scaffolding.service.user.UserService;
import web.scaffolding.service.vo.UserVO;
import web.scaffolding.web.controller.formbean.UserCreateFormBean;
import web.scaffolding.web.controller.formbean.UserSearchFormBean;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String user(@PageableDefault Pageable pageable, @ModelAttribute UserSearchFormBean formBean, Model model) {
        CustomUserDetail customUserDetail = EnhanceSecurityUtils.retrieveEnhanceUser();
        PageWrapper<UserVO> page = userService.page(pageable, formBean, customUserDetail.getUserId());
        model.addAttribute("page", page);
        model.addAttribute("query", formBean);
        return "user/user";
    }


    @GetMapping("/user/add-user")
    public String addUser() {
        return "user/add_user";
    }

    @GetMapping("/user/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserVO userVO = userService.getById(id);
        model.addAttribute("editUser", userVO);
        return "user/edit_user";
    }

    @DuplicateSubmitToken
    @PostMapping("/users")
    public String addUser(UserCreateFormBean formBean, Model model) {
        try {
            userService.addUser(formBean);
            return "redirect:/user";
        } catch (ZwtException e) {
            log.error(e.getMessage(), e);
            model.addAttribute("msg",e.getMessage());
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            model.addAttribute("msg",t.getMessage());
        }
        return "user/add_user";

    }

    @DuplicateSubmitToken
    @PutMapping("/users")
    public String updateUser(UserCreateFormBean formBean, Model model) {
        try {
            userService.update(formBean);
            return "redirect:/user";
        } catch (ZwtException e) {
            log.error(e.getMessage(), e);
            model.addAttribute("msg", e.getMessage());
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            model.addAttribute("msg", t.getMessage());
        }
        return "user/edit_user";
    }

    @PostMapping("/users/ajax/{userId}/{operation}")
    public String disableUser(@PathVariable Long userId, @PathVariable boolean operation) {
        userService.disableUser(userId, operation);
        return "redirect:/user";
    }

    @DuplicateSubmitToken
    @PostMapping("/user/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return "redirect:/user";
        } catch (ZwtException e) {
            log.error(e.getMessage(), e);
            return "redirect:/user";
        }
    }
}
