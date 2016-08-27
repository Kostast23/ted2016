package gr.uoa.di.api;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    private UserService userService;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(value = "/users/not_validated", method = RequestMethod.GET)
    public List<UserEntity> getNotValidatedUsers() {
        return userService.getNotValidatedUsers();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public UserEntity getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @RequestMapping(value = "/users/{userId}/validate", method = RequestMethod.GET)
    public void validateUser(@PathVariable int userId) {
        userService.validateUser(userId);
    }
}
