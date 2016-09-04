package gr.uoa.di.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final SimpleDateFormat xmlDateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(text.getBytes("UTF-8"));

        return new BigInteger(1, crypt.digest()).toString(16);
    }

    public static Integer parseUSDollars(String usd) {
        if (usd == null)
            return null;

        if (usd.startsWith("$")) {
            usd = usd.substring(1);
        }
        float price = Float.parseFloat(usd);
        return (int) price;
    }

    public static Date parseXMLDate(String xmlDate) {
        if (xmlDate == null)
            return null;
        try {
            return xmlDateFormat.parse(xmlDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toXMLDate(Date date) {
        if (date == null)
            return null;
        return xmlDateFormat.format(date);
    }
}
