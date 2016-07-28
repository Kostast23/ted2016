package gr.uoa.di;

import gr.uoa.di.dto.UserDto;
import gr.uoa.di.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(value = "/hello")
    public String hello() {
        UserDto dto = new UserDto();
        dto.setUsername("hello!");
        userRepo.save(dto);
        return "hiii";
    }

}
