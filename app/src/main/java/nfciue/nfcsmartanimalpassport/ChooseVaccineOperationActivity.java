package nfciue.nfcsmartanimalpassport;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseVaccineOperationActivity extends AppCompatActivity {

    Button buttonVaccine;
    Button buttonOperation;
    int operationCode=-1 ;
    int vaccineCode=-1;
    String pswFromVaccine;
    String commentForOperation="";

    String psw;
    BottomNavigationView navigationView;
    ExpandableListView expListView;
    ArrayList<String> groups;
    HashMap<String, ArrayList<String>> items;
    ArrayList<ArrayList<String>> data;  //stays empty, no data needed for this exp list view, but we want to use same adapter with the exp list in info shown.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vaccine_operation);
        final Context context = this;


        expListView = (ExpandableListView) findViewById(R.id.exp_list_choose_vaccine_op);
        navigationView = findViewById(R.id.navigationChooseVaccineOp);

        groups = new ArrayList<String>();
        items = new HashMap<String, ArrayList<String>>();
        data = new ArrayList<ArrayList<String>>();

        groups.add("Aşı Ekle");
        groups.add("Operasyon Ekle");

        ArrayList<String> vaccines = new ArrayList<String>();
        vaccines.add("Theileria");
        vaccines.add("Escherichia Coli");
        vaccines.add("Brusella");
        vaccines.add("Mantar");
        vaccines.add("Alum");
        vaccines.add("Pasteurella");

        ArrayList<String> operations = new ArrayList<String>();
        operations.add("Kan Analizi");
        operations.add("Pansuman");
        operations.add("Medikal Muayene");
        operations.add("Fiziksel Muayene");
        operations.add("Medikal Operasyon");
        operations.add("Doğum");

        items.put(groups.get(0), vaccines);
        items.put(groups.get(1), operations);

        ExpandableListAdapter adapter = new ExpListAdapter(this, groups, items,data);
        expListView.setAdapter(adapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                switch(i){
                    case 0:{
                        switch(i1){
                            case 0: Toast.makeText(ChooseVaccineOperationActivity.this,"heyheyehey",Toast.LENGTH_SHORT).show();
                                    vaccineCode=1; break;
                            case 1: vaccineCode=2; break;
                            case 2: vaccineCode=3; break;
                            case 3: vaccineCode=4; break;
                            case 4: vaccineCode=5; break;
                            case 5: vaccineCode=6; break;
                        }
                    }  break;
                    case 1:{
                        switch(i1){
                            case 0: Toast.makeText(ChooseVaccineOperationActivity.this,"heyheyeheyoperation",Toast.LENGTH_SHORT).show();
                                    operationCode=1; break;
                            case 1: operationCode=2; break;
                            case 2: operationCode=3; break;
                            case 3: operationCode=4; break;
                            case 4: operationCode=5; break;
                            case 5: operationCode=6; break;
                        }
                    }
                }

                final Dialog dialogComment = new Dialog(context);
                dialogComment.setContentView(R.layout.add_comment);
                dialogComment.setTitle("Not eklemek ister misiniz ?");
                final EditText editTextComment = dialogComment.findViewById(R.id.editTextComment);

                Button buttonOkComment = dialogComment.findViewById(R.id.buttonOk);
                buttonOkComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        commentForOperation = editTextComment.getText().toString();

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_pincode);
                        dialog.setTitle("Şifre Doğrulanmalı");
                        final EditText editTextPsw = dialog.findViewById(R.id.confirmPassword);

                        Button buttonOk = dialog.findViewById(R.id.buttonOK);
                        buttonOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String password = editTextPsw.getText().toString();

                                Intent ChooseVaccineOpIntent = new Intent(ChooseVaccineOperationActivity.this, WriteToTagActivity.class);
                                ChooseVaccineOpIntent.putExtra("password", password);
                                ChooseVaccineOpIntent.putExtra("operationCode", operationCode);
                                ChooseVaccineOpIntent.putExtra("vaccineCode", vaccineCode);
                                ChooseVaccineOpIntent.putExtra("opComment", commentForOperation);
                                ChooseVaccineOpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ChooseVaccineOpIntent);

                            }
                        });
                        dialog.show();

                    }
                });
                dialogComment.show();







                return true;
            }
        });
        Log.e("adapter","set");


        navigationView = findViewById(R.id.navigationChooseVaccineOp);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_backToMain:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent ExpListIntent = new Intent(ChooseVaccineOperationActivity.this, SignedInMainActivity.class);
                            ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ExpListIntent);
                        }
                        else{
                            Intent InfoShownIntent = new Intent(ChooseVaccineOperationActivity.this, MainActivity.class);
                            startActivity(InfoShownIntent);
                        } break;
                    case R.id.action_profile:
                        break;

                }

                return true;

            }});

    }


}
