package com.saucedemo.helperUtilities.emailGen;


import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.helperUtilities.number_StringGen.NumberGeneratorUtils;
import com.saucedemo.helperUtilities.number_StringGen.TextGeneratorUtils;
import org.apache.log4j.Logger;

import java.util.Random;

public class EmailGeneratorUtils {

    private static final Logger log = LoggerHelper.getLogger(EmailGeneratorUtils.class);
    private static final Random random = new Random();

    public EmailGeneratorUtils() {
    }

    // -5 is here to take into account @ and .com
    public static String generateEmailWithNumbersAndLength(int numberOfChars) {
        log.info("Generating email with numbers and length: " + numberOfChars);
        // // test.log(Status.INFO, "Generating email with numbers and length: " + numberOfChars);
        String generatedEmail = NumberGeneratorUtils.generateNumberWithLength(numberOfChars / 2) + "@" + NumberGeneratorUtils.generateNumberWithLength((numberOfChars / 2) - 5) + "gmail.com";
        log.info("Generated email: " + generatedEmail);
        return generatedEmail;
    }

    public static String generateEmailWithTextAndLengthLessThanFiveChars(int numberOfChars, String caseType) {
        log.info("Generating email with text, length less than five chars: " + numberOfChars + ", and case type: " + caseType);
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars / 2, caseType) + "@" + TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars / 2, caseType);
        log.info("Generated email: " + generatedEmail);
        return generatedEmail;
    }

    public static String generateEmailWithNumbersAndLengthLessThanFiveChars(int numberOfChars) {
        log.info("Generating email with numbers and length less than five chars: " + numberOfChars);
        String generatedEmail = NumberGeneratorUtils.generateNumberWithLength(numberOfChars / 2) + "@" + NumberGeneratorUtils.generateNumberWithLength((numberOfChars / 2));
        log.info("Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with a mix of numbers and text, with a specified length.
     *
     * @param numberOfChars The total desired length of the email (excluding ".com").
     * @param caseType      The case type for the text part ("upper", "lower", or "mixed").
     * @return A generated email string.
     */
    public static String generateEmailWithMixedCharsAndLength(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and length: " + numberOfChars + " and case type: " + caseType);
        // // test.log(Status.INFO, "Generating email with mixed chars and length: " + numberOfChars + "
        // and case type: " + caseType);
        int halfLength = numberOfChars / 2;
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(halfLength / 2, caseType) + // Text part
                NumberGeneratorUtils.generateNumberWithLength(halfLength / 2) + // Number part
                "@" + TextGeneratorUtils.generateStringWithLengthAndCase((halfLength - 5) / 2, caseType) + // Text part
                NumberGeneratorUtils.generateNumberWithLength((halfLength - 5) / 2) + // Number part
                ".com";
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    public String generateEmailWithTextAndLength(int numberOfChars, String caseType) {
        log.info("Generating email with text, length: " + numberOfChars + ", and case type: " + caseType);
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars / 2, caseType) + "@" + TextGeneratorUtils.generateStringWithLengthAndCase((numberOfChars / 2) - 5, caseType) + ".com";
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with only text, using a random domain.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @param caseType      The case type for the text ("upper", "lower", or "mixed").
     * @return A generated email string.
     */
    public String generateEmailWithTextAndRandomDomain(int numberOfChars, String caseType) {
        log.info("Generating email with text and random domain, length: " + numberOfChars + " and case type: " + caseType);
        // // test.log(Status.INFO, "Generating email with text and random domain, length: " +
        // numberOfChars + " and case type: " + caseType);
        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars, caseType)).append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with only numbers, using a random domain.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @return A generated email string.
     */
    public String generateEmailWithNumbersAndRandomDomain(int numberOfChars) {
        log.info("Generating email with numbers and random domain, length: " + numberOfChars);
        // // test.log(Status.INFO, "Generating email with numbers and random domain, length: " +
        // numberOfChars);
        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        String generatedEmail = email.append(NumberGeneratorUtils.generateNumberWithLength(numberOfChars)).append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with a mix of numbers and text, using a random domain.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @param caseType      The case type for the text part ("upper", "lower", or "mixed").
     * @return A generated email string.
     */
    public String generateEmailWithMixedCharsAndRandomDomain(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and random domain, length: " + numberOfChars + " and case type: " + caseType);
        // // test.log(Status.INFO, "Generating email with mixed chars and random domain, length: " +
        // numberOfChars + " and case type: " + caseType);
        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength, caseType)) // Text part
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength)) // Number part
                .append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with a fixed domain and a mix of numbers and text.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @param caseType      The case type for the text part ("upper", "lower", or "mixed").
     * @param domain        The fixed domain to use (e.g., "mycompany.com").
     * @return A generated email string.
     */
    public String generateEmailWithMixedCharsAndFixedDomain(int numberOfChars, String caseType, String domain) {
        log.info("Generating email with mixed chars and fixed domain, length: " + numberOfChars + " and case type: " + caseType + " and domain: " + domain);
        // // test.log(Status.INFO, "Generating email with mixed chars and fixed domain, length: " +
        // numberOfChars + " and case type: " + caseType + " and domain: " + domain);
        StringBuilder email = new StringBuilder();
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength, caseType)) // Text part
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength)) // Number part
                .append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with only text and a fixed domain.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @param caseType      The case type for the text ("upper", "lower", or "mixed").
     * @param domain        The fixed domain to use (e.g., "mycompany.com").
     * @return A generated email string.
     */
    public String generateEmailWithTextAndFixedDomain(int numberOfChars, String caseType, String domain) {
        log.info("Generating email with text and fixed domain, length: " + numberOfChars + " and case type: " + caseType + " and domain: " + domain);
        // // test.log(Status.INFO, "Generating email with text and fixed domain, length: " +
        // numberOfChars + " and case type: " + caseType + " and domain: " + domain);
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars, caseType) + "@" + domain;
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with only numbers and a fixed domain.
     *
     * @param numberOfChars The total desired length of the email (excluding the domain).
     * @param domain        The fixed domain to use (e.g., "mycompany.com").
     * @return A generated email string.
     */
    public String generateEmailWithNumbersAndFixedDomain(int numberOfChars, String domain) {
        log.info("Generating email with numbers and fixed domain, length: " + numberOfChars + " and domain: " + domain);
        // // test.log(Status.INFO, "Generating email with numbers and fixed domain, length: " +
        // numberOfChars + " and domain: " + domain);
        String generatedEmail = NumberGeneratorUtils.generateNumberWithLength(numberOfChars) + "@" + domain;
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }

    /**
     * Generates an email with a mix of numbers and text, with a specified length, and a mix of
     * subdomains.
     *
     * @param numberOfChars The total desired length of the email (excluding ".com").
     * @param caseType      The case type for the text part ("upper", "lower", or "mixed").
     * @return A generated email string.
     */
    public String generateEmailWithMixedCharsAndSubdomain(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and subdomain, length: " + numberOfChars + " and case type: " + caseType);
        // // test.log(Status.INFO, "Generating email with mixed chars and subdomain, length: " +
        // numberOfChars + " and case type: " + caseType);
        StringBuilder email = new StringBuilder();
        String[] subdomains = {"info", "support", "sales", "test", "user"};
        String subdomain = subdomains[random.nextInt(subdomains.length)];
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength / 2, caseType)) // Text part
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength / 2)) // Number part
                .append("@").append(subdomain).append(".com").toString();
        log.info("Generated email: " + generatedEmail);
        // // test.log(Status.INFO, "Generated email: " + generatedEmail);
        return generatedEmail;
    }
}
