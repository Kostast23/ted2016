package gr.uoa.di.api;

import gr.uoa.di.dto.user.UserLoginDto;
import gr.uoa.di.dto.user.UserRegisterDto;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserApi {

    @Autowired
    private UserService userService;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/{username}")
    public void getUser(@PathVariable String username) {
        //userService.getUser(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody UserRegisterDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userService.register(request);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody UserLoginDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.login(request);
    }
}
