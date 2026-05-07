package com.saucedemo.helperutilities.emailgen;


import com.saucedemo.helperutilities.logger.LoggerHelper;
import com.saucedemo.helperutilities.numberstringgen.NumberGeneratorUtils;
import com.saucedemo.helperutilities.numberstringgen.TextGeneratorUtils;
import org.apache.log4j.Logger;

import java.util.Random;

public class EmailGeneratorUtils {

    private static final Logger log = LoggerHelper.getLogger(EmailGeneratorUtils.class);
    private static final Random random = new Random();

    public EmailGeneratorUtils() {
    }


    public static String generateEmailWithNumbersAndLength(int numberOfChars) {
        log.info("Generating email with numbers and length: " + numberOfChars);

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


    public static String generateEmailWithMixedCharsAndLength(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and length: " + numberOfChars + " and case type: " + caseType);


        int halfLength = numberOfChars / 2;
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(halfLength / 2, caseType) +
                NumberGeneratorUtils.generateNumberWithLength(halfLength / 2) +
                "@" + TextGeneratorUtils.generateStringWithLengthAndCase((halfLength - 5) / 2, caseType) +
                NumberGeneratorUtils.generateNumberWithLength((halfLength - 5) / 2) +
                ".com";
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }

    public String generateEmailWithTextAndLength(int numberOfChars, String caseType) {
        log.info("Generating email with text, length: " + numberOfChars + ", and case type: " + caseType);
        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars / 2, caseType) + "@" + TextGeneratorUtils.generateStringWithLengthAndCase((numberOfChars / 2) - 5, caseType) + ".com";
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithTextAndRandomDomain(int numberOfChars, String caseType) {
        log.info("Generating email with text and random domain, length: " + numberOfChars + " and case type: " + caseType);


        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars, caseType)).append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithNumbersAndRandomDomain(int numberOfChars) {
        log.info("Generating email with numbers and random domain, length: " + numberOfChars);


        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        String generatedEmail = email.append(NumberGeneratorUtils.generateNumberWithLength(numberOfChars)).append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithMixedCharsAndRandomDomain(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and random domain, length: " + numberOfChars + " and case type: " + caseType);


        StringBuilder email = new StringBuilder();
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "test.net"};
        String domain = domains[random.nextInt(domains.length)];
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength, caseType))
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength))
                .append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithMixedCharsAndFixedDomain(int numberOfChars, String caseType, String domain) {
        log.info("Generating email with mixed chars and fixed domain, length: " + numberOfChars + " and case type: " + caseType + " and domain: " + domain);


        StringBuilder email = new StringBuilder();
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength, caseType))
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength))
                .append("@").append(domain).toString();
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithTextAndFixedDomain(int numberOfChars, String caseType, String domain) {
        log.info("Generating email with text and fixed domain, length: " + numberOfChars + " and case type: " + caseType + " and domain: " + domain);


        String generatedEmail = TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars, caseType) + "@" + domain;
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithNumbersAndFixedDomain(int numberOfChars, String domain) {
        log.info("Generating email with numbers and fixed domain, length: " + numberOfChars + " and domain: " + domain);


        String generatedEmail = NumberGeneratorUtils.generateNumberWithLength(numberOfChars) + "@" + domain;
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }


    public String generateEmailWithMixedCharsAndSubdomain(int numberOfChars, String caseType) {
        log.info("Generating email with mixed chars and subdomain, length: " + numberOfChars + " and case type: " + caseType);


        StringBuilder email = new StringBuilder();
        String[] subdomains = {"info", "support", "sales", "test", "user"};
        String subdomain = subdomains[random.nextInt(subdomains.length)];
        int halfLength = numberOfChars / 2;
        String generatedEmail = email.append(TextGeneratorUtils.generateStringWithLengthAndCase(halfLength / 2, caseType))
                .append(NumberGeneratorUtils.generateNumberWithLength(halfLength / 2))
                .append("@").append(subdomain).append(".com").toString();
        log.info("Generated email: " + generatedEmail);

        return generatedEmail;
    }
}
