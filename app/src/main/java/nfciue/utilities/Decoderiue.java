package nfciue.utilities;

import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DateFormat;
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
        if(asciiText.equals("!!!!")) {
            return "YOK.";
        } else {
            String hex = asciiToHex(asciiText);
            Log.e("ULAK-HEX", hex);
            long dec = hexToLong(hex);
            Log.e("ULAK-DEC", String.valueOf(dec));
            String date = unixToDate(dec);
            Log.e("ULAK-DATE", date);
            return date;
        }
    }

    // Function for encoding unix time from current time.
    public static String dateToUnixTimeText() {
        // Get current time.
        long unixTime = System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTime);
    }

    // Function for encoding ascii date from current time. It is used when a new operation/vaccine info is added to tag.
    public static String dateToAscii() {
        // Get current time.
        long unixTime = System.currentTimeMillis() / 1000L;
        // Get hexadecimal string from a decimal number.
        String hexString = Long.toHexString(unixTime);
        if(hexString.length() % 2 == 1) {    // If hex string's character number is odd, it makes it even.  // This is not applicable for date but will be useful for tcNO or other numbers.
            hexString = "0" + hexString;
        }
        String strAscii = "";
        for(int i=0; i < hexString.length(); i+=2) {
            String s = hexString.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            strAscii = strAscii + (char) decimal;
        }
        return strAscii;
    }

    // Function for decoding city ascii code to city name.
    public static String cityAsciiToName(String code) {
        if(code.equals("35")) return "İzmir";
        else if(code.equals("34")) return "İstanbul";
        else if(code.equals("06")) return "Ankara";
        else return "Henüz yok";
    }

    // Function for decoding country ascii code to country name.
    public static String countryAsciiToName(String code) {
        if(code.equals("TR")) return "Türkiye";
        else if(code.equals("US")) return "Amarika";
        else if(code.equals("NT")) return "Hollanda";
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
        String[] parsedData = new String[57];
        //Toast.makeText(this, "Size: " + dataOnNFCTag.length(), Toast.LENGTH_LONG).show();
        parsedData[0] = dataOnNFCTag.substring(3, 14);  // Animal ID - 1
        parsedData[1] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(14, 24))); // Animal Birthdate - 2
        parsedData[2] = Decoderiue.genderDecoder(dataOnNFCTag.substring(24, 25)); // Animal Gender - 3
        parsedData[3] = Decoderiue.breedDecoder(dataOnNFCTag.substring(25, 29)); // Animal Breed - 4
        parsedData[4] = dataOnNFCTag.substring(29, 40); // Animal MotherID - 5
        parsedData[5] = dataOnNFCTag.substring(40, 50); // Animal birth farm no - 6
        parsedData[6] = dataOnNFCTag.substring(50, 60); // Animal current farm no - 7
        parsedData[7] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(60, 70))); // Animal farm change data - 8
        parsedData[8] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(70, 72)); // Animal export country code - 9
        parsedData[9] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(72, 82))); // Animal export date - 10
        parsedData[10] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(82, 92))); // Animal death date - 11
        parsedData[11] = zeroSubstractorFromStrings(dataOnNFCTag.substring(92, 132)); // Animal death place - 12
        parsedData[12] = treeMainVaccineChecker(dataOnNFCTag.substring(132, 133)); // Animal alum vaccine - 13 - şap aşısı
        parsedData[13] = treeMainVaccineChecker(dataOnNFCTag.substring(133, 134)); // Animal  - 14 - sığır vebası aşısı
        parsedData[14] = treeMainVaccineChecker(dataOnNFCTag.substring(134, 135)); // Animal  - 15 - theileria annulata aşısı
        parsedData[15] = treeMainVaccineChecker(dataOnNFCTag.substring(135, 136)); // Animal e. coli aşısı-
        parsedData[16] = treeMainVaccineChecker(dataOnNFCTag.substring(136, 137)); // Animal buzağı septisemi serumu aşısı
        parsedData[17] = treeMainVaccineChecker(dataOnNFCTag.substring(137, 138)); // Animal brucellosis vaccine
        parsedData[18] = treeMainVaccineChecker(dataOnNFCTag.substring(138, 139)); // Animal iktero-hemglobinuri aşısı
        parsedData[19] = treeMainVaccineChecker(dataOnNFCTag.substring(139, 140)); // Animal botilismus aşısı
        parsedData[20] = treeMainVaccineChecker(dataOnNFCTag.substring(140, 141)); // Animal yanıkara aşısı
        parsedData[21] = treeMainVaccineChecker(dataOnNFCTag.substring(141, 142)); // Animal anthrax aşısı
        parsedData[22] = treeMainVaccineChecker(dataOnNFCTag.substring(142, 143)); // Animal leptosipira aşısı
        parsedData[23] = zeroSubstractorFromStrings(dataOnNFCTag.substring(143, 183));   // Slaughter house name - 17
        parsedData[24] = zeroSubstractorFromStrings(dataOnNFCTag.substring(183, 223));  // Slaughter house address - 18
        parsedData[25] = dataOnNFCTag.substring(223, 233);  // Slaughter house licence number - 19
        parsedData[26] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(233, 243)));  // Slaughter date - 20
        parsedData[27] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(243, 245));  // Farm country code - 21
        parsedData[28] = Decoderiue.cityAsciiToName(dataOnNFCTag.substring(245, 247));  // Farm city code - 22
        parsedData[29] = zeroSubstractorFromStrings(dataOnNFCTag.substring(247, 279));  // Owner name + surname - 23 + 24
        parsedData[30] = dataOnNFCTag.substring(279, 290);  // Owner TC no - 25
        parsedData[31] = zeroSubstractorFromStrings(dataOnNFCTag.substring(290, 330));  // Owner residence address - 26
        parsedData[32] = zeroSubstractorFromStrings(dataOnNFCTag.substring(330, 370));  // Farm address - 27
        parsedData[33] = dataOnNFCTag.substring(370, 392);  // Farm geo coordinates - 28
        parsedData[34] = dataOnNFCTag.substring(392, 402);  // Farm phone number - 29
        parsedData[35] = dataOnNFCTag.substring(402, 412);  // Farm fax number - 30
        parsedData[36] = zeroSubstractorFromStrings(dataOnNFCTag.substring(412, 460));  // Farm email address - 31
        parsedData[37] = dataOnNFCTag.substring(460, 471);  // Operator ID - 32
        parsedData[38] = dataOnNFCTag.substring(471, 472);  // Operation type - 33
        parsedData[39] = dataOnNFCTag.substring(472, 520);  // Esign of owner - 34
        parsedData[40] = dataOnNFCTag.substring(520, 560);  // Name of institution - 35
        parsedData[41] = dataOnNFCTag.substring(560, 608);  // Esign of director - 36
        parsedData[42] = dataOnNFCTag.substring(608, 618);  // Creation date of passport - 37
        parsedData[43] = dataOnNFCTag.substring(618, 630);  // Barcode number - 38
        parsedData[44] = dataOnNFCTag.substring(630, 640);  // Timestamp - 39
        parsedData[45] = dataOnNFCTag.substring(640, 644);  // Pincode
        parsedData[46] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(644, 654)));  // Last operation date.
        parsedData[47] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(654, 664)));  // Last şap aşısı tarihi.
        parsedData[48] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(664, 674)));  // Last sığır vebası aşısı tarihi.
        parsedData[49] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(674, 684)));  // Last Theileria Annulata aşısı tarihi.
        parsedData[50] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(684, 694)));  // Last e.coli aşısı tarihi.
        parsedData[51] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(694, 704)));  // Last buzağı septisemi serumu aşısı tarihi.
        parsedData[52] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(704, 714)));  // Last brucellosis aşısı tarihi.
        parsedData[53] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(714, 724)));  // Last iktero-hemglobinuri aşısı tarihi.
        parsedData[54] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(724, 734)));  // Last botulismus aşısı tarihi.
        parsedData[55] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(734, 744)));  // Last yanıkara aşısı tarihi.
        parsedData[56] = Decoderiue.unixToDate(Long.parseLong(dataOnNFCTag.substring(744, 754)));  // Last leptosipira aşısı tarihi.

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
        Log.e("ISDONE:" , isDone);
        if(isDone.equals("T")) {
            return "Yapıldı.";
        } else if(isDone.equals("F")) {
            return "Yapılmadı.";
        } else {
            return "NoInfo";
        }
    }

    public static String zeroSubstractorFromStrings(String text) {
        return text.substring(text.lastIndexOf('$')+1);
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
            return "Medikal Muayene";
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

    public static String vetPasswordComplementer(String vetPassword) {
        int lengthOfVetPassword = vetPassword.length();
        int neededChars = 16 - lengthOfVetPassword;

        for(int i = 0; i < neededChars; i++) {
            vetPassword += "a";
        }

        return vetPassword;
    }

    public static Date getDateFromDateString(String dateString) {
        Date date = null;
        try{
            DateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("tr"));
            date = format.parse(dateString);

        } catch (Exception e) {
            Log.e("ERROR", "In function getDateFromDateString: " + e.getMessage());
        }
        finally {
            return date;
        }
    }
}
