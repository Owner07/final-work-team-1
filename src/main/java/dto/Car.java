package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Car {

    @Builder.Default
    String engine = "Electro";
    @Builder.Default
    String mark = "Tesla";
    @Builder.Default
    String model = "Model Y";
    @Builder.Default
    String price = "350780";
}
