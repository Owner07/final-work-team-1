package db.houses;

public class HousesSql {

    public static String selectHouseById(String id) {
        return "SELECT id, floor_count, price FROM public.house WHERE id = '" + id + "'";
    }
}