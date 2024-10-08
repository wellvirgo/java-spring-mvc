package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        String text = this.userService.handle();
        model.addAttribute("title", text);
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String displayUsers(Model model) {
        List<User> userList = this.userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin/user/table-user";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User userById = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("user", userById);
        System.out.println(userById);
        return "admin/user/user-detail";
    }

    @RequestMapping("/admin/user/create")
    public String createUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUser(Model model, @ModelAttribute User newUser) {
        User user = this.userService.handleSaveUser(newUser);
        System.out.println(user);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        model.addAttribute("currentUser", this.userService.getUserById(id));
        model.addAttribute("id", id);
        return "/admin/user/user-update";
    }

    @PostMapping("/admin/user/update")
    public String postMethodName(@ModelAttribute User currentUser) {
        User userUpdated = this.userService.getUserById(currentUser.getId());
        if (currentUser != null) {
            userUpdated.setFullname(currentUser.getFullname());
            userUpdated.setAddress(currentUser.getAddress());
            userUpdated.setPhone(currentUser.getPhone());
            this.userService.handleSaveUser(userUpdated);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id, @ModelAttribute User currentUser) {
        currentUser = this.userService.getUserById(id);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("id", id);
        return "/admin/user/delete-user";
    }

    @PostMapping("/admin/user/delete")
    public String deleteUser(@ModelAttribute User currentUser) {
        this.userService.deleteUser(currentUser.getId());
        return "redirect:/admin/user";
    }

}
