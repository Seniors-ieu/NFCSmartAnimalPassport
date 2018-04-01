package nfciue.utilities;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anil on 1.04.2018.
 */

public class Decoderiue {
    // Function for decoding ascii chars to hex string.
    public  static String asciiToHex(String arg) {
        try {
            Log.e("asciiToHex", String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8"))));
            String hexText = utf8TextToHex(arg);
            Log.e("utf8AsciiToHex", hexText);
            return hexText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "aaaaaaaa";
        }
    }

    // Function for utf-8 ascii text to hex. // Now it works perfectly. Mifail, 01/04/2018 15:16
    public static String utf8TextToHex(String ascii) {
        char[] chars = ascii.toCharArray();
        String hexText = "";
        for(int i = 0; i < chars.length; i++) {
            //System.out.println(Integer.valueOf(chars[i]));
            try {
                hexText = hexText + Integer.toHexString(chars[i] | 0x10000).substring(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hexText;
    }

    // Function for converting hex string do decimal.
    public static long hexToLong(String hex) {
        Log.e("hexToLong", String.valueOf(Long.parseLong(hex, 16)));
        return Long.parseLong(hex, 16);
    }

    // Function for converting unixTime to Date string.
    public static String unixToDate(long unixTime) {
        Log.e("unixToDate", String.valueOf(unixTime));
        Date time = new Date((long)unixTime*1000);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("tr"));
        return sdf.format(time);
    }

    // Function for decoding ascii date to string date.
    public static String asciiToDateText(String asciiText) {
        String hex = asciiToHex(asciiText);
        Log.e("ULAK-HEX", hex);
        long dec = hexToLong(hex);
        Log.e("ULAK-DEC", String.valueOf(dec));
        String date = unixToDate(dec);
        Log.e("ULAK-DATE", date);
        return date;
    }

    // Function for decoding city ascii code to city name.
    public static String cityAsciiToName(String code) {
        if(code.equals("A")) return "İzmir";
        else if(code.equals("B")) return "İstanbul";
        else if(code.equals("B")) return "Ankara";
        else return "Henüz yok";
    }

    // Function for decoding country ascii code to country name.
    public static String countryAsciiToName(String code) {
        if(code.equals("!")) return "Türkiye";
        else if(code.equals("A")) return "Amarika";
        else if(code.equals("B")) return "Hollanda";
        else return "Henüz yok";
    }

    // Function for decoding ascii tcNo to real tcNo.
    public static String asciiTCNoToRealTC(String TCNo) {
        String hex = asciiToHex(TCNo);
        long dec = hexToLong(hex);
        return String.valueOf(dec);
    }
}
