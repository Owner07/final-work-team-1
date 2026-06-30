package ui.dto;

import java.util.Random;

public class CarFactory {

    private static final String[] ENGINE_TYPES = {"Gasoline", "Diesel", "Electric", "CNG", "PHEV", "Hydrogenic"};
    private static final String[] MARKS = {"Toyota", "BMW", "Audi", "Volvo", "Tesla", "Nissan", "Skoda", "Renault"};
    private static final String[] MODELS = {"Camry", "X5", "A4", "S60", "Model X", "Qashqai", "Octavia", "Kodiaq"};

    public static Car getCar() {
        Random random = new Random();
        return new Car(
                ENGINE_TYPES[random.nextInt(ENGINE_TYPES.length)],
                MARKS[random.nextInt(MARKS.length)],
                MODELS[random.nextInt(MODELS.length)],
                String.valueOf(random.nextInt(100000) + 1000)
        );
    }
}