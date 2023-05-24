package go.it.java_notepad.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import go.it.java_notepad.entity.User;
import go.it.java_notepad.entity.UserRole;
import go.it.java_notepad.service.NoteService;
import go.it.java_notepad.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;
    private final NoteService noteService;

    public AdminController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("author", noteService.author());
        model.addAttribute("isCurrentUserAdmin", userService.isCurrentUserAdmin());
        return "admin";
    }

    @PostMapping("/users/{userId}/block")
    public String blockUser(@PathVariable("userId") Long userId) {
        userService.blockUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/unblock")
    public String unblockUser(@PathVariable("userId") Long userId) {
        userService.unblockUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/updateRole")
    public String updateUserRole(@PathVariable("userId") Long userId, @RequestParam("role") UserRole role) {
        userService.updateUserRole(userId, role);
        return "redirect:/admin/users";
    }
}
