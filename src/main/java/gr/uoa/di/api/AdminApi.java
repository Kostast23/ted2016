package gr.uoa.di.api;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.exception.user.UserNotFoundException;
import gr.uoa.di.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    private UserRepository userRepo;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserEntity> getUsers() {
        return userRepo.findAll();
    }

    @RequestMapping(value = "/users/not_validated", method = RequestMethod.GET)
    public List<UserEntity> getNotValidatedUsers() {
        return userRepo.findByValidatedFalse();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public UserEntity getUser(@PathVariable int userId) {
        UserEntity user = userRepo.findOneById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @RequestMapping(value = "/users/{userId}/validate", method = RequestMethod.GET)
    public void validateUser(@PathVariable int userId) {
        UserEntity user = userRepo.findOneById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        } else if (user.getValidated()) {

        }
        user.setValidated(true);
        userRepo.save(user);
    }
}
