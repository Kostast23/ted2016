package gr.uoa.di;

import gr.uoa.di.dto.UserDto;
import gr.uoa.di.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Rest {

    @Autowired
    private UserRepository userRepo;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody RegisterRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserDto dto = new UserDto();
        dto.setUsername(request.getUsername());
        String salt = Long.toHexString(new Random().nextLong());
        String encPass = UserDto.sha1(UserDto.sha1(request.getPassword()) + salt);
        dto.setPassword(encPass);
        dto.setSalt(salt);
        userRepo.save(dto);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody LoginRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserDto user = userRepo.findOneByUsername(request.getUsername());
        if (user == null) {
            throw new LoginException();
        }

        String encPass = UserDto.sha1(UserDto.sha1(request.getPassword()) + user.getSalt());
        if (!encPass.equals(user.getPassword())) {
            throw new LoginException();
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        String jws = Jwts.builder()
                .setClaims(new HashMap(Collections.singletonMap("user", request.getUsername())))
                .setSubject("TED")
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return Collections.singletonMap("jwt", jws);
    }

}
