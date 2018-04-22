package nfciue.nfcsmartanimalpassport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_in_expanded_list);
        expListView = (ExpandableListView) findViewById(R.id.exp_list);
        groups = new ArrayList<String>();
        items = new HashMap<String, ArrayList<String>>();
        data = new ArrayList<ArrayList<String>>();

        dataFromNFCTag = getIntent().getStringExtra("dataFromPartialInfoShownActivityIntent");
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

        health.add(parsedDataFromNFCTag[12] + "\nTarih:" + parsedDataFromNFCTag[43]);
        health.add( parsedDataFromNFCTag[14]); //+ "\nTarih:" + parsedDataFromNFCTag[45]); todo: stringe eklenmeli (Anıl)
        health.add(parsedDataFromNFCTag[13] + "\nTarih:" + parsedDataFromNFCTag[44]);
        health.add(Decoderiue.vaccineChooser(parsedDataFromNFCTag[15])+ "\nTarih:" + parsedDataFromNFCTag[42]);

        health.add(Decoderiue.operationChooser(parsedDataFromNFCTag[33]) + "\nTarih:" + parsedDataFromNFCTag[41]);
        health.add("");
        data.add(health);

        manageGroupsAndItems();


        adapter = new ExpListAdapter(this, groups, items,data);
        expListView.setAdapter(adapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                switch(i){
                    case 3:{
                        switch(i1){
                            case 5: Toast.makeText(InfoShownInExpandedListActivity.this,"heyheyehey",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });
        Log.e("adapter","set");


        navigationView = findViewById(R.id.navigationInfoShown);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId()){
                   case R.id.action_backToMain:
                       if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                           Intent ExpListIntent = new Intent(InfoShownInExpandedListActivity.this, SignedInMainActivity.class);
                           ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(ExpListIntent);
                       }
                       else{
                           Intent InfoShownIntent = new Intent(InfoShownInExpandedListActivity.this, MainActivity.class);
                           startActivity(InfoShownIntent);
                       } break;
                   case R.id.action_profile:
                       break;
                   case R.id.action_edit:
                       if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                           Intent ExpListIntent = new Intent(InfoShownInExpandedListActivity.this, ChooseVaccineOperationActivity.class);
                           ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(ExpListIntent);
                       }
                       else{
                           Toast.makeText(InfoShownInExpandedListActivity.this,"You should be logged in for this!",Toast.LENGTH_SHORT).show();
                       } break;
               }
               return true;

            }});

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
        healthInfo.add("Alum Vaccine");
        healthInfo.add("Pasteurella Vaccine");
        healthInfo.add("Brucellosis Vaccine");
        healthInfo.add("Other Vaccine");
        healthInfo.add("Operation");
        healthInfo.add("Click for more detail...");




        items.put(groups.get(0), animalInfo);
        items.put(groups.get(1), ownerInfo);
        items.put(groups.get(2), farmInfo);
        items.put(groups.get(3), healthInfo);
        items.put(groups.get(4), new ArrayList<String>());



    }
    @Override
    public void onBackPressed() {

    }

}

