package hexlet.code.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @JsonFormat(pattern = "dd.MM.yyyy, HH:mm")
    private LocalDateTime createdAt;
}
