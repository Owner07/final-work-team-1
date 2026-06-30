package utils;

import com.github.javafaker.Faker;
import api.models.users.create.UserCreateRequest;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class UserTestDataFactory {

    private static final Faker faker = new Faker(new Locale("en"));

    public static UserCreateRequest createValidUser() {
        return UserCreateRequest.builder()
                .id(System.currentTimeMillis())
                .firstName(faker.name().firstName())
                .secondName(faker.name().lastName())
                .age(ThreadLocalRandom.current().nextInt(18, 80))
                .sex(randomSex())
                .money((double) ThreadLocalRandom.current().nextInt(1_000, 100_000))
                .build();
    }

    private static String randomSex() {
        return ThreadLocalRandom.current().nextBoolean() ? "MALE" : "FEMALE";
    }
}