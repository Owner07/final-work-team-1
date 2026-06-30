package models.users.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String sex;
    private Double money;

}