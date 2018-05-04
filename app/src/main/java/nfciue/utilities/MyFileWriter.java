package nfciue.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by anil on 4.05.2018.
 */

public class MyFileWriter {
    public static void writeLocalObjectToDevice(String jsonObjectString) {
        //Get the text file
        //File fileEvents = new File(context.getFilesDir().getAbsolutePath() + "/NFC/encrypted.txt");
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"



        try {
            File sdcard = Environment.getExternalStorageDirectory();

            //Get the text file
            File file = new File(sdcard+"/NFC/","updates.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(jsonObjectString);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            Log.e("ULAK", e.getMessage());

        }
        finally {

        }
    }
}
