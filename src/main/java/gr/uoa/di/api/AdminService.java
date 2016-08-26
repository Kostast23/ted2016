package gr.uoa.di.api;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.exception.user.UserNotFoundException;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @RequestMapping(value = "/not_validated", method = RequestMethod.GET)
    public List<UserEntity> notValidated() {
        return userRepo.findByValidatedFalse();
    }

    @RequestMapping(value = "/not_validated/{userId}", method = RequestMethod.GET)
    public UserEntity notValidatedUser(@PathVariable int userId) {
        UserEntity user = userRepo.findOneById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @RequestMapping(value = "/validated", method = RequestMethod.GET)
    public List<UserEntity> validated() {
        return userRepo.findByValidatedTrue();
    }

    @RequestMapping(value = "/validate/{userId}", method = RequestMethod.GET)
    public void validateUser(@PathVariable int userId) {
        UserEntity user = userRepo.findOneById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        } else if (user.getValidated()) {

        }
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

    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(text.getBytes("UTF-8"));

        return new BigInteger(1, crypt.digest()).toString(16);
    }
}
