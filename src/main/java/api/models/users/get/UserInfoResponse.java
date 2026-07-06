package api.models.users.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import api.models.cars.get.CarGetRs;
import java.util.List;

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
    private List<CarGetRs> cars;
}