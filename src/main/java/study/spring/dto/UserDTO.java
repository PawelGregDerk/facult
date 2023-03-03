package study.spring.dto;

import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import study.spring.valid.user.CheckDateTime;
import study.spring.valid.user.ConfirmPassword;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor_ = {@NonNull})
@ConfirmPassword
public class UserDTO {
    private Integer id;
    @NonNull
    @NotBlank(message = "{not.empty}")
    @Pattern(regexp = "^\\S[a-zA-Zа-ёА-ЯЁ_\\s]+\\S+$", message = "{name.error}")
    @Size(min = 3, max = 50, message = "{user.size}")
    private String name;
    @NonNull
    @NotBlank(message = "{not.empty}")
    @Email(message = "{user.email.wrong}")
    private String email;
    @NonNull
    @NotBlank(message = "{not.empty}")
    private String password;
    private String confirmPassword;
    @NonNull
    private Boolean rate;
    @NonNull
    @NotNull(message = "{not.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CheckDateTime
    private Date experience;
    @NonNull
    @NotBlank(message = "{not.empty}")
    private String post;
    private Period workExperience;
    private ImageDTO image;
    private Set<CurseDTO> curses = new HashSet<>();
    private Collection<RoleDTO> roles;

    public void addCurse(CurseDTO curse) {
        curse.getUsers().add(this);
        curses.add(curse);
    }

    public void removeCurse(CurseDTO curse) {
        curses.remove(curse);
        curse.getUsers().remove(this);
    }
}
