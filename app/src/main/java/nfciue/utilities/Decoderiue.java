package nfciue.utilities;

import android.util.Log;
import android.widget.Toast;

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
            String hexText = utf8TextToHex(arg);
            Log.e("utf8AsciiToHex", hexText);
            return hexText;
        } catch (Exception e) {
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

    // Function for decoding ascii tcNo to real tcNo. This function can also be used for converting ascii to a long number.
    public static String asciiTCNoToRealTC(String TCNo) {
        String hex = asciiToHex(TCNo);
        long dec = hexToLong(hex);
        return String.valueOf(dec);
    }

    // Function for parsing string on the NFC tag.
    public static String[] parseNFCData(String dataOnNFCTag) {
        String[] parsedData = new String[41];
        //Toast.makeText(this, "Size: " + dataOnNFCTag.length(), Toast.LENGTH_LONG).show();
        parsedData[0] = dataOnNFCTag.substring(3, 14);  // Animal ID - 1
        parsedData[1] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(14, 18)); // Animal Birthdate - 2
        parsedData[2] = Decoderiue.genderDecoder(dataOnNFCTag.substring(18, 19)); // Animal Gender - 3
        parsedData[3] = Decoderiue.breedDecoder(dataOnNFCTag.substring(19, 23)); // Animal Breed - 4
        parsedData[4] = dataOnNFCTag.substring(23, 34); // Animal MotherID - 5
        parsedData[5] = asciiTCNoToRealTC(dataOnNFCTag.substring(34, 38)); // Animal birth farm no - 6
        parsedData[6] = asciiTCNoToRealTC(dataOnNFCTag.substring(38, 42)); // Animal current farm no - 7
        parsedData[7] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(42, 46)); // Animal farm change data - 8
        parsedData[8] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(46, 47)); // Animal export country code - 9
        parsedData[9] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(47, 51)); // Animal export date - 10
        parsedData[10] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(51, 55)); // Animal death date - 11
        parsedData[11] = zeroSubstractorFromStrings(dataOnNFCTag.substring(55, 95)); // Animal death place - 12
        parsedData[12] = treeMainVaccineChecker(dataOnNFCTag.substring(95, 96)); // Animal alum vaccine - 13
        parsedData[13] = treeMainVaccineChecker(dataOnNFCTag.substring(96, 97)); // Animal brucellosis vaccine - 14
        parsedData[14] = treeMainVaccineChecker(dataOnNFCTag.substring(97, 98)); // Animal pasturella vaccine - 15
        parsedData[15] = dataOnNFCTag.substring(98, 99); // Animal other vaccine - 16
        parsedData[16] = zeroSubstractorFromStrings(dataOnNFCTag.substring(99, 139));   // Slaughter house name - 17
        parsedData[17] = zeroSubstractorFromStrings(dataOnNFCTag.substring(139, 154));  // Slaughter house address mahalle - 18
        parsedData[18] = zeroSubstractorFromStrings(dataOnNFCTag.substring(154, 164));  // Slaughter house address sokak - 18
        parsedData[19] = zeroSubstractorFromStrings(dataOnNFCTag.substring(164, 179));  // Slaughter house address remaining address info - 18
        parsedData[20] = dataOnNFCTag.substring(179, 183);  // Slaughter house licence number - 19
        parsedData[21] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(183, 187));  // Slaughter date - 20
        parsedData[22] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(187, 188));  // Farm country code - 21
        parsedData[23] = Decoderiue.cityAsciiToName(dataOnNFCTag.substring(188, 189));  // Farm city code - 22
        parsedData[24] = zeroSubstractorFromStrings(dataOnNFCTag.substring(189, 205)) + zeroSubstractorFromStrings(dataOnNFCTag.substring(205, 221));  // Owner name + surname - 23 + 24
        parsedData[25] = Decoderiue.asciiTCNoToRealTC(dataOnNFCTag.substring(221, 226));  // Owner TC no - 25
        parsedData[26] = zeroSubstractorFromStrings(dataOnNFCTag.substring(226, 266));  // Owner residence address - 26
        parsedData[27] = zeroSubstractorFromStrings(dataOnNFCTag.substring(266, 306));  // Farm address - 27
        parsedData[28] = dataOnNFCTag.substring(306, 312);  // Farm geo coordinates - 28
        parsedData[29] = asciiTCNoToRealTC(dataOnNFCTag.substring(312, 317));  // Farm phone number - 29
        parsedData[30] = asciiTCNoToRealTC(dataOnNFCTag.substring(317, 322));  // Farm fax number - 30
        parsedData[31] = zeroSubstractorFromStrings(dataOnNFCTag.substring(322, 370));  // Farm email address - 31
        parsedData[32] = dataOnNFCTag.substring(370, 375);  // Operator ID - 32
        parsedData[33] = dataOnNFCTag.substring(375, 376);  // Operation type - 33
        parsedData[34] = dataOnNFCTag.substring(376, 424);  // Esign of owner - 34
        parsedData[35] = dataOnNFCTag.substring(424, 464);  // Name of institution - 35
        parsedData[36] = dataOnNFCTag.substring(464, 512);  // Esign of director - 36
        parsedData[37] = dataOnNFCTag.substring(512, 516);  // Creation date of passport - 37
        parsedData[38] = dataOnNFCTag.substring(516, 528);  // Barcode number - 38
        parsedData[39] = dataOnNFCTag.substring(528, 532);  // Timestamp - 39
        parsedData[40] = dataOnNFCTag.substring(532, 536);  // Pincode


        return parsedData;
    }

    public static String genderDecoder(String gender) {
        if(gender.equals("F")) {
            return "Dişi";
        } else if(gender.equals("M")) {
            return "Erkek";
        } else {
            return "NoInfo";
        }
    }

    public static String breedDecoder(String breed) {
        if(breed.equals("0033")) {
            return "Yerlikara";
        } else {
            return "Unknown breed";
        }
    }

    public static String treeMainVaccineChecker(String isDone) {
        if(isDone.equals("T")) {
            return "Yapıldı.";
        } else if(isDone.equals("F")) {
            return "Yapılmadı.";
        } else {
            return "NoInfo";
        }
    }

    public static String zeroSubstractorFromStrings(String text) {
        return text.substring(text.lastIndexOf('0')+1);
    }

    public static String vaccineChooser(String vaccineCode) {
        if(vaccineCode.equals("1")) {
            return "Theileria Annulata";
        } else if(vaccineCode.equals("2")) {
            return "Escherichia coli";
        } else if(vaccineCode.equals("3")) {
            return "Buzağı Septisemi serumu";
        } else if(vaccineCode.equals("4")) {
            return "İktero-hemglobinuri";
        } else if(vaccineCode.equals("5")) {
            return "Yanıkara";
        } else if(vaccineCode.equals("6")) {
            return "Anthrax";
        } else if(vaccineCode.equals("7")) {
            return "Leptosipira";
        } else {
            return "Other";
        }
    }

    public static String operationChooser(String vaccineCode) {
        if(vaccineCode.equals("1")) {
            return "Kan Analizi";
        } else if(vaccineCode.equals("2")) {
            return "Pansuman";
        } else if(vaccineCode.equals("3")) {
            return "Sağlık Kontrolü";
        } else if(vaccineCode.equals("4")) {
            return "Fiziksel Muayene";
        } else if(vaccineCode.equals("5")) {
            return "Tıbbi Operasyon";
        } else if(vaccineCode.equals("6")) {
            return "Doğum";
        } else {
            return "Other";
        }
    }
}
