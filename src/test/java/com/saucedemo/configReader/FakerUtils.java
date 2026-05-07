package com.saucedemo.configreader;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public final class FakerUtils {

    private static final long FAKER_SEED = Long.getLong("faker.seed", 42L);
    private static final Faker FAKER = new Faker(new Random(FAKER_SEED));

    private FakerUtils() {
    }

    public static String resolveToken(String token) {
        String normalized = token == null ? "" : token.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "email", "random_email" -> generateRandomEmail();
            case "password", "random_password" -> generateRandomPassword();
            case "name", "random_name" -> generateRandomName();
            case "username", "random_username", "invalid_username" -> generateRandomUsername();
            case "invalid_password", "wrong_password" -> generateInvalidPasswordForSauceDemo();
            default -> token == null ? "" : token;
        };
    }

    public static long generateRandomNumber() {
        return FAKER.number().randomNumber(10, true);
    }

    public static String generateRandomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String generateRandomPassword() {
        return FAKER.internet().password(8, 14, true, true, true);
    }

    public static String generateRandomName() {
        return FAKER.name().fullName();
    }

    public static String generateRandomUsername() {
        return "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String generateInvalidPasswordForSauceDemo() {
        return "wrong_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
