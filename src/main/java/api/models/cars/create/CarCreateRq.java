package api.models.cars.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarCreateRq {

    private String engineType;
    private String mark;
    private String model;
    private int price;
}
