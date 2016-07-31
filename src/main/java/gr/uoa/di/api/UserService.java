package gr.uoa.di.api;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.LoginDto;
import gr.uoa.di.dto.RegisterDto;
import gr.uoa.di.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody RegisterDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (userRepo.findOneByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException();
        }
        UserEntity dto = new UserEntity();
        dto.setUsername(request.getUsername());
        String salt = Long.toHexString(new Random().nextLong());
        String encPass = sha1(sha1(request.getPassword()) + salt);
        dto.setPassword(encPass);
        dto.setSalt(salt);
        userRepo.save(dto);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody LoginDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserEntity user = userRepo.findOneByUsername(request.getUsername());
        if (user == null) {
            throw new LoginException();
        }

        String encPass = sha1(sha1(request.getPassword()) + user.getSalt());
        if (!encPass.equals(user.getPassword())) {
            throw new LoginException();
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        Map<String, Object> claims = new HashMap(Collections.singletonMap("user", request.getUsername()));
        if (user.getAdmin() != null && user.getAdmin()) {
            claims.put("admin", true);
        }

        String jws = Jwts.builder()
                .setClaims(claims)
                .setSubject("TED")
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return Collections.singletonMap("jwt", jws);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test() {
        return "okay";
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class LoginException extends AuthenticationException {
        public LoginException() {
            super("Invalid credentials");
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
