package nfciue.utilities;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

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

    public static LocalObject[] readNotSentUpdates() {
        //This array is for storing objects that will be sent to Firebase.
        LocalObject[] notSentUpdates = null;
        //Get the text file
        //File fileEvents = new File(context.getFilesDir().getAbsolutePath() + "/NFC/encrypted.txt");
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard+"/NFC/","updates.txt");

        try {
            //This buffered reader will just read line number to initialize the LocalObject array. // To know how many objects there will be.
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
            }
            br.close();

            //Create LocalObject array.
            notSentUpdates = new LocalObject[lineNumber];
            //This buffered reader is for reading all json objects line by line.
            BufferedReader br2 = new BufferedReader(new java.io.FileReader(file));
            int objectCounter = 0;
            while ((line = br2.readLine()) != null) {
                //Last char is always a ",". We need to delete it.
                line = line.substring(0, (line.length() - 1));
                Gson gson = new Gson();
                //Create LocalObject from json object that we read from line.
                LocalObject obj = gson.fromJson(line.toString(), LocalObject.class);
                //Make that object an element of LocalObject array.
                notSentUpdates[objectCounter] = obj;
                objectCounter++;
            }
        }
        catch (IOException e) {
            Log.e("ULAK", e.getMessage());
            //You'll need to add proper error handling here
        }
        finally {
            //you now have the file content in the text variable and you can use it
            //based on you needs
            return notSentUpdates;
        }

    }
}
