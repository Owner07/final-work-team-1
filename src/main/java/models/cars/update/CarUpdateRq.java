package models.cars.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarUpdateRq {

    private String engineType;
    private String mark;
    private String model;
    private int price;
}
