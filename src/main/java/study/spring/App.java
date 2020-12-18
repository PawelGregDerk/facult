package study.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import study.spring.dto.ImageDTO;
import study.spring.dto.UserDTO;
import study.spring.entity.User;
import study.spring.service.ImgService;
import study.spring.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.FEBRUARY;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableJpaRepositories("study.spring.repository")
public class App {
    @Autowired
    private UserService uService;
    @Autowired
    private ImgService iService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String email = "111@11.11";
        if (uService.getUser(email) == null) {
            Date date = new GregorianCalendar(2014, FEBRUARY, 11).getTime();
            UserDTO userDTO = new UserDTO(
                    "Вован Сидорович Щербатый",
                    email,
                    passwordEncoder.encode("111"),
                    true,
                    date,
                    "professor"
            );
            User u = uService.createAdmin(userDTO);
            Integer id = u.getId();
            iService.addPhoto(id, new ImageDTO());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}