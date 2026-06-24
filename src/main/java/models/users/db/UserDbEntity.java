package models.users.db;

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

    private Long id;
    private Integer age;
    private String firstName;
    private BigDecimal money;
    private String secondName;
    private Boolean sex;
    private Long houseId;
}