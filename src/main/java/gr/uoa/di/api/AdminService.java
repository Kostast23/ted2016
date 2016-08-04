package gr.uoa.di.api;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminService {

    @Autowired
    private UserRepository userRepo;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/awaitingValidation", method = RequestMethod.GET)
    public List<UserEntity> awaitingValidation() {
        return userRepo.findByValidatedFalse();
    }


    @RequestMapping(value = "/validateUser/{userId}", method = RequestMethod.GET)
    public void validateUser(@PathVariable long userId) {
        UserEntity user = userRepo.findOneById(userId);
        user.setValidated(true);
        userRepo.save(user);
    }


    @Autowired
    ItemRepository repo2;
    @Autowired
    CategoryRepository repo3;
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test() {
        ItemEntity a = new ItemEntity();
        CategoryEntity c = new CategoryEntity();
        c.setName("helo");
        a.setName("werld!!");
        a.setOwner(userRepo.findOneByUsername("admin"));
        a.setCategories(Collections.singletonList(c));
        repo3.save(c);
        repo2.save(a);
        return "ok!";
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public class NotValidatedException extends AuthenticationException {
        public NotValidatedException() {
            super("User has not yet been validated, plase try again later");
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class LoginException extends AuthenticationException {
        public LoginException() {
            super("Invalid credentials");
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class EmailException extends AuthenticationException {
        public EmailException() {
            super("Invalid email");
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class PasswordMatchingException extends AuthenticationException {
        public PasswordMatchingException() {
            super("Passwords don't match");
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class PasswordLengthException extends AuthenticationException {
        public PasswordLengthException() {
            super("Password is too short");
        }
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    public class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException() {
            super("User already exists");
        }
    }

    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(text.getBytes("UTF-8"));

        return new BigInteger(1, crypt.digest()).toString(16);
    }
}
