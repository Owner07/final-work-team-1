package api.models.houses.update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseUpdateResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("floorCount")
    @Expose
    private int floorCount;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("parkingPlaces")
    @Expose
    private List<ParkingPlace> parkingPlaces;

    @SerializedName("lodgers")
    @Expose
    private List<Object> lodgers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingPlace {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("isWarm")
        @Expose
        private boolean isWarm;

        @SerializedName("isCovered")
        @Expose
        private boolean isCovered;

        @SerializedName("placesCount")
        @Expose
        private int placesCount;
    }
}