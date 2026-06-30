package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class NewHouse {
    @Builder.Default
    private String floors = "5";
    @Builder.Default
    private String price = "200";
    @Builder.Default
    private String hasWarmAnd = "1";
    @Builder.Default
    private String hasWarmNot = "2";
    @Builder.Default
    private String hasColdBut = "3";
    @Builder.Default
    private String hasColdNot = "4";
}
