package nfciue.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Created by anil on 24.04.2018.
 */

public class MyFileReader {
    public static String readEncryptedKeyFromFile() {
        //Get the text file
        //File fileEvents = new File(context.getFilesDir().getAbsolutePath() + "/NFC/encrypted.txt");
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard+"/NFC/","encrypted.txt");

        //Read text from file
        StringBuilder textFromFile = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                textFromFile.append(line);
                //textFromFile.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("ULAK", e.getMessage());
            //You'll need to add proper error handling here
        }
        finally {
            //you now have the file content in the text variable and you can use it
            //based on you needs
            Log.e("MYAPP",textFromFile.toString());
            return textFromFile.toString();
        }
    }
}
