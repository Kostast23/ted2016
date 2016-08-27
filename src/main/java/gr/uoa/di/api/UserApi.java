package gr.uoa.di.api;

import gr.uoa.di.dto.LoginDto;
import gr.uoa.di.dto.RegisterDto;
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody RegisterDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userService.register(request);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody LoginDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.login(request);
    }
}
