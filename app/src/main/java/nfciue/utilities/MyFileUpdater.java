package nfciue.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by anil on 18.05.2018.
 */

public class MyFileUpdater {
    public static void removeLine(final int lineIndex) throws IOException {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard+"/NFC/","updates.txt");

        final List<String> lines = new LinkedList<>();
        final Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
        while(reader.hasNextLine())
            lines.add(reader.nextLine());
        Log.e("LINESS", String.valueOf(lines.size()));
        reader.close();
        assert lineIndex >= 0 && lineIndex <= lines.size() - 1;
        lines.remove(lineIndex);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for(final String line : lines)
            writer.write(line + "\n");
        writer.flush();
        writer.close();
    }

    public static void removeLastReadTagData() {
        try {
            File sdcard = Environment.getExternalStorageDirectory();

            //Get the text file
            File file = new File(sdcard+"/NFC/","lastReadTagData.txt");
            FileWriter writer = new FileWriter(file, false);
            writer.append("");
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
