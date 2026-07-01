package api.models.houses.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseCreateRequest {

    @SerializedName("floorCount")
    @Expose
    private int floorCount;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("parkingPlaces")
    @Expose
    private List<ParkingPlace> parkingPlaces;

    @SerializedName("price")
    @Expose
    private int price;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingPlace {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("isCovered")
        @Expose
        private boolean isCovered;

        @SerializedName("isWarm")
        @Expose
        private boolean isWarm;

        @SerializedName("placesCount")
        @Expose
        private int placesCount;
    }
}