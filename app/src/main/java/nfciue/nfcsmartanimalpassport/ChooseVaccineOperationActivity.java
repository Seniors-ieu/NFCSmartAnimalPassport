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

        groups.add("Vaccinate");
        groups.add("Operate");

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



                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_pincode);
                dialog.setTitle("Password Confirmation");
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
                        ChooseVaccineOpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ChooseVaccineOpIntent);

                    }
                });
                dialog.show();



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















       /* buttonVaccine = findViewById(R.id.buttonVaccine);
        buttonOperation = findViewById(R.id.buttonOp);

        buttonVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogVaccine = new Dialog(context);
                dialogVaccine.setContentView(R.layout.dialog_vaccine_type);
                dialogVaccine.setTitle("Please Choose Vaccine Type");

                final EditText editTextPsw1 = dialogVaccine.findViewById(R.id.editTextPsw1v);

                Spinner spinner = (Spinner) dialogVaccine.findViewById(R.id.spinnerVaccine);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.vaccines_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        vaccineCode = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here , todo: show alert dialog : you must choose a type!!
                    }

                });

                Button buttonOk = dialogVaccine.findViewById(R.id.buttonOkVaccine);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        pswFromVaccine = editTextPsw1.getText().toString();

                        Intent VaccineIntent = new Intent(ChooseVaccineOperationActivity.this, WriteToTagActivity.class);
                        VaccineIntent.putExtra("password", pswFromVaccine);
                        VaccineIntent.putExtra("vaccineCode", vaccineCode);
                        VaccineIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(VaccineIntent);

                    }
                });
                dialogVaccine.show();


            }
        });

        buttonOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_operation);
                dialog.setTitle("Please Choose Operation Type");


                final EditText editTextPsw1 = dialog.findViewById(R.id.editTextPsw1);

                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerOperation);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.operations_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        operationCode = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here , todo: show alert dialog : you must choose a type!!
                    }

                });

                Button buttonOk = dialog.findViewById(R.id.buttonOk);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        psw = editTextPsw1.getText().toString();
                        Log.e("ULAK-psw", psw);

                        Intent OperationIntent = new Intent(ChooseVaccineOperationActivity.this, WriteToTagActivity.class);
                        OperationIntent.putExtra("password", psw);
                        OperationIntent.putExtra("operationCode", operationCode);
                        OperationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(OperationIntent);

                    }
                });
                dialog.show();


            }
        });*/
    }


}
