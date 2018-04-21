package nfciue.nfcsmartanimalpassport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import nfciue.utilities.Decoderiue;

public class InfoShownInExpandedListActivity extends AppCompatActivity {
    ExpandableListView expListView;
    ArrayList<String> groups;
    HashMap<String, ArrayList<String>> items;
    ArrayList<ArrayList<String>> data;
    ExpListAdapter adapter;
    private String dataFromNFCTag;
    private String[] parsedDataFromNFCTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_in_expanded_list);
        expListView = (ExpandableListView) findViewById(R.id.exp_list);
        groups = new ArrayList<String>();
        items = new HashMap<String, ArrayList<String>>();
        data = new ArrayList<ArrayList<String>>();

        dataFromNFCTag = getIntent().getStringExtra("dataFromReadActivityIntent");
        try {
            parsedDataFromNFCTag = Decoderiue.parseNFCData(dataFromNFCTag);
        } catch (Exception e) {
            Toast.makeText(this, "parse: " + e.getMessage() + " Parsing data", Toast.LENGTH_LONG).show();
        }


        ArrayList<String> hayvan= new ArrayList<String>();
        hayvan.add(parsedDataFromNFCTag[0]);
        hayvan.add(parsedDataFromNFCTag[1]);
        hayvan.add(parsedDataFromNFCTag[2]);
        hayvan.add(parsedDataFromNFCTag[3]);
        hayvan.add(parsedDataFromNFCTag[4]);
        hayvan.add(parsedDataFromNFCTag[5]);
        hayvan.add(parsedDataFromNFCTag[8]);
        hayvan.add(parsedDataFromNFCTag[9]);
        data.add(hayvan);

        ArrayList<String> sahip= new ArrayList<String>();
        sahip.add(parsedDataFromNFCTag[25]);
        sahip.add(parsedDataFromNFCTag[24]);
        sahip.add(parsedDataFromNFCTag[26]);
        data.add(sahip);

        ArrayList<String> farm= new ArrayList<String>();
        farm.add(parsedDataFromNFCTag[6]);
        farm.add(parsedDataFromNFCTag[7]);
        farm.add(parsedDataFromNFCTag[28]);
        farm.add(parsedDataFromNFCTag[22]);
        farm.add(parsedDataFromNFCTag[23]);
        farm.add(parsedDataFromNFCTag[27]);
        farm.add(parsedDataFromNFCTag[29]);
        farm.add(parsedDataFromNFCTag[30]);
        farm.add(parsedDataFromNFCTag[31]);
        data.add(farm);

        ArrayList<String> health= new ArrayList<String>();
        health.add("Vaccines");
        health.add("Operations");
        data.add(health);

        manageGroupsAndItems();


        adapter = new ExpListAdapter(this, groups, items,data);
        expListView.setAdapter(adapter);
        Log.e("adapter","set");
    }
    public void manageGroupsAndItems(){



        groups.add("Animal Information");
        groups.add("Owner Information");
        groups.add("Current Farm Information");
        groups.add("Health Information");
        groups.add("Slaughter - dead Information"); // hayvan ölmediyse gösterilmesin ?

        ArrayList<String> animalInfo = new ArrayList<String>();
        animalInfo.add("Animal ID");
        animalInfo.add("Birthdate");
        animalInfo.add("Gender");
        animalInfo.add("Breed");
        animalInfo.add("Mother ID");
        animalInfo.add("Birth Farm No:");
        animalInfo.add("Export Country Code");
        animalInfo.add("Export Date");


        ArrayList<String> ownerInfo = new ArrayList<String>();
        ownerInfo.add("Owner TC");
        ownerInfo.add("Owner Name");
        ownerInfo.add("Owner Residance Address");

        ArrayList<String> farmInfo = new ArrayList<String>();
        farmInfo.add("Current Farm No");
        farmInfo.add("Farm Change Date");
        farmInfo.add("Farm Coordinates");
        farmInfo.add("Country Code");
        farmInfo.add("City Code");
        farmInfo.add("Address");
        farmInfo.add("Phone Number");
        farmInfo.add("Fax");
        farmInfo.add("E-mail");

        ArrayList<String> healthInfo= new ArrayList<String>();
        healthInfo.add("Vaccines");
        healthInfo.add("Operations");




        items.put(groups.get(0), animalInfo);
        items.put(groups.get(1), ownerInfo);
        items.put(groups.get(2), farmInfo);
        items.put(groups.get(3), healthInfo);
        items.put(groups.get(4), new ArrayList<String>());



    }



}

