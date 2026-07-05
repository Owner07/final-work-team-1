package api.models.houses.get;

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
public class HouseGetResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("floorCount")
    @Expose
    private int floorCount;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("parkingPlaces")
    @Expose
    private List<ParkingPlace> parkingPlaces;

    @SerializedName("lodgers")
    @Expose
    private List<Lodger> lodgers;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Lodger {

        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("firstName")
        @Expose
        private String firstName;

        @SerializedName("secondName")
        @Expose
        private String secondName;

        @SerializedName("age")
        @Expose
        private int age;

        @SerializedName("sex")
        @Expose
        private String sex;

        @SerializedName("money")
        @Expose
        private double money;
    }
}