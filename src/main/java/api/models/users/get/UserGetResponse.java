package api.models.users.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGetResponse {

    private Integer id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String sex;
    private Double money;
}