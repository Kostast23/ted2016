package gr.uoa.di.api;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.LoginDto;
import gr.uoa.di.dto.RegisterDto;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
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
        if (!request.getPassword().equals(request.getPassword2())) {
            throw new PasswordMatchingException();
        } else if (request.getPassword().length() < 8) {
            throw new PasswordLengthException();
        } else if (!request.getEmail().contains("@")) {
            throw new EmailException();
        } else if (userRepo.findOneByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException();
        }
        UserEntity dto = new UserEntity();
        dto.setUsername(request.getUsername());
        String salt = Long.toHexString(new Random().nextLong());
        String encPass = sha1(sha1(request.getPassword()) + salt);
        dto.setPassword(encPass);
        dto.setSalt(salt);
        dto.setName(request.getName());
        dto.setSurname(request.getSurname());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getTelephone());
        dto.setLocation(request.getAddress());
        dto.setCountry(request.getCountry());
        dto.setLat(request.getLatitude());
        dto.setLon(request.getLongitude());
        dto.setAfm(request.getAfm());
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
        } else if (!user.getValidated()) {
            throw new NotValidatedException();
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
