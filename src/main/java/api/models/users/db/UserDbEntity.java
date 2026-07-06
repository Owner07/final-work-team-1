package api.models.users.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDbEntity {

    private Integer id;
    private Integer age;
    private String firstName;
    private BigDecimal money;
    private String secondName;
    private Boolean sex;
    private Integer houseId;
}