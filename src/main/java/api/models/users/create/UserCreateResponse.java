package api.models.users.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponse {

    private Long id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String sex;
    private Double money;
}
