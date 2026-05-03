package com.saucedemo.helperUtilities.faker;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakerHelper {

    private static final Faker faker = new Faker(new Locale("en-US"));

    public FakerHelper() {
    }

    public static String generateUsername() {
        return faker.name().username().replaceAll("[^a-zA-Z0-9]", "") + faker.number().digits(8);
    }

    public static String generatePassword() {
        // Generate a strong password with letters, numbers, and special characters
        return "Password@" + faker.number().digits(8);
    }

    public static String generateFullName() {
        return faker.name().fullName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    public String generateFaxNumber() {

        return faker.phoneNumber().phoneNumber();
    }

    public String generateCompanyName() {
        return faker.company().name();
    }

    public String generateAddress() {
        return faker.address().streetAddress();
    }

    public String generateSecondaryAddress() {
        return faker.address().secondaryAddress();
    }

    public String generateCity() {
        return faker.address().city();
    }

    public String generateZipCode() {
        return faker.address().zipCode();
    }

    public String generateState() {
        return faker.address().state();
    }

    public String generateCountry() {
        return faker.address().country();
    }
}
