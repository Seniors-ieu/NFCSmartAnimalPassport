package nfciue.nfcsmartanimalpassport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import nfciue.utilities.LocalObject;
import nfciue.utilities.MyFileReader;

public class NotSentUpdatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_sent_updates);

        LocalObject[] notSentUpdateObjects = MyFileReader.readNotSentUpdates();
        int numberOfNotSentUpdates = notSentUpdateObjects.length;
        String[] listElements = new String[numberOfNotSentUpdates];

        for(int i = 0; i < numberOfNotSentUpdates; i++) {
            String listElement = "Hayvan ID: " + notSentUpdateObjects[i].getAnimalIdForUpdate()
                    + "\nTarih: " + notSentUpdateObjects[i].getDate() + "\nOperasyon Notu: " + notSentUpdateObjects[i].getOpComment()
                    + "\nOperasyon: " + notSentUpdateObjects[i].getOpNameFromPrevActivity() + "\nAşı adı: " + notSentUpdateObjects[i].getVaccineNameFromPrevActivity();
            listElements[i] = listElement;
        }
        ListAdapter notSentUpdatesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listElements);
        ListView notSentUpdatesListView = (ListView) findViewById(R.id.notSentUpdatesList);
        notSentUpdatesListView.setAdapter(notSentUpdatesListAdapter);
    }
}
