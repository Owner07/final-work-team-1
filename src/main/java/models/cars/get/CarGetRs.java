package models.cars.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarGetRs {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("engineType")
    @Expose
    public String engineType;
    @SerializedName("mark")
    @Expose
    public String mark;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("price")
    @Expose
    public Integer price;
}
