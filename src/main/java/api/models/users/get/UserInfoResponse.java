package api.models.users.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Integer id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String sex;
    private Double money;
    private Integer house;

    // Временно, пока CarDto делает другой участник
    private List<Map<String, Object>> cars;
}