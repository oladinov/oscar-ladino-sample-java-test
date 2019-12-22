package com.ladino.simpletest.util;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static Double generateRandomAmount() {
        return ThreadLocalRandom.current().nextDouble(0.01d, 999.99d);
    }
}

