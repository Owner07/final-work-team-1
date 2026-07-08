package api.models.cars.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDbEntity {
    private int id;
    private int engineTypeId;
    private String mark;
    private String model;
    private BigDecimal price;
    private Integer personId;
}