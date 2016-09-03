package gr.uoa.di.service;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.user.UserLoginDto;
import gr.uoa.di.dto.user.UserRegisterDto;
import gr.uoa.di.dto.user.UserResponseDto;
import gr.uoa.di.exception.user.*;
import gr.uoa.di.mapper.UserMapper;
import gr.uoa.di.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserMapper userMapper;

    @Value("${secret_key}")
    private String secretKey;

    public void register(UserRegisterDto dto)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (!dto.getPassword().equals(dto.getPassword2())) {
            throw new UserService.PasswordMatchingException();
        } else if (dto.getPassword().length() < 8) {
            throw new UserService.PasswordLengthException();
        } else if (!dto.getEmail().contains("@")) {
            throw new UserService.EmailException();
        } else if (userRepo.findOneByUsername(dto.getUsername()) != null) {
            throw new UserAlreadyExistsException();
        }
        UserEntity user = userMapper.mapUserRegisterDtoToUserEntity(dto);
        String salt = Long.toHexString(new Random().nextLong());
        String encPass = sha1(sha1(dto.getPassword()) + salt);
        user.setPassword(encPass);
        user.setSalt(salt);
        userRepo.save(user);
    }

    public Map<String, String> login(UserLoginDto request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserEntity user = getUserEntity(request.getUsername());
        String encPass = sha1(sha1(request.getPassword()) + user.getSalt());
        if (!encPass.equals(user.getPassword())) {
            throw new UserLoginException();
        } else if (!user.getValidated()) {
            throw new UserNotValidatedException();
        }
        return Collections.singletonMap("jwt", createJwt(user));
    }

    public UserResponseDto getUser(int id) {
        return userMapper.mapUserEntityToUserResponseDto(getUserEntity(id));
    }

    public Page<UserResponseDto> getUsers(Pageable pageable) {
        return userMapper.mapUserEntityPageToUserResponseDtoPage(userRepo.findAll(pageable));
    }

    public Page<UserResponseDto> getNotValidatedUsers(Pageable pageable) {
        return userMapper.mapUserEntityPageToUserResponseDtoPage(userRepo.findByValidatedFalse(pageable));
    }

    public void validateUser(int userId) {
        UserEntity user = getUserEntity(userId);
        if (user.getValidated()) {
            throw new UserAlreadyValidatedException();
        }
        user.setValidated(true);
        userRepo.save(user);
    }

    public void deleteUser(int userId) {
        UserEntity user = getUserEntity(userId);
        userRepo.delete(user);
    }

    private UserEntity getUserEntity(int userId) {
        UserEntity user = userRepo.findOneById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private UserEntity getUserEntity(String username) {
        UserEntity user = userRepo.findOneByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
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

    private String createJwt(UserEntity user) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user.getUsername());
        claims.put("admin", user.getAdmin() != null && user.getAdmin());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject("TED")
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(text.getBytes("UTF-8"));

        return new BigInteger(1, crypt.digest()).toString(16);
    }
}
