package gr.uoa.di.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final SimpleDateFormat xmlDateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
    private static final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    static {
        formatter.setMaximumFractionDigits(2);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
    }

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
        float price = 0;
        try {
            price = formatter.parse(usd).floatValue() * 100;
            return (int) price;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toUSDollars(Integer usd) {
        if (usd == null)
            return null;
        double usdF = usd;
        usdF /= 100;
        String res = "$" + formatter.format(usdF);
        return res;
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
