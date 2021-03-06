package gr.uoa.di.api;

import gr.uoa.di.dto.user.UserResponseDto;
import gr.uoa.di.service.AdminService;
import gr.uoa.di.service.SuggestionService;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private SuggestionService suggestionService;

    @Value("${secret_key}")
    private String secretKey;

    @RequestMapping(value = "/users/validated", method = RequestMethod.GET)
    public Page<UserResponseDto> getValidatedUsers(@RequestParam(required = false) String username, Pageable pageable) {
        return userService.getValidatedUsers(username, pageable);
    }

    @RequestMapping(value = "/users/not_validated", method = RequestMethod.GET)
    public Page<UserResponseDto> getNotValidatedUsers(@RequestParam(required = false) String username, Pageable pageable) {
        return userService.getNotValidatedUsers(username, pageable);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public UserResponseDto getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @RequestMapping(value = "/users/not_validated/{userId}/validate", method = RequestMethod.GET)
    public void validateUser(@PathVariable int userId) {
        userService.validateUser(userId);
    }

    @RequestMapping(value = "/users/not_validated/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "/uploadBackup", method = RequestMethod.POST)
    public void uploadBackup(@RequestParam("file") MultipartFile uploadFile) {
        adminService.restoreFile(uploadFile);
    }

    @ResponseBody
    @RequestMapping(value = "/dumpDatabase", method = RequestMethod.GET)
    public byte[] dumpDatabase() {
        return adminService.createXMLDump();
    }

    @RequestMapping(value = "/runAutosuggestions", method = RequestMethod.GET)
    public void runAutosuggestions() {
        suggestionService.runAutosuggestions();
    }
}
