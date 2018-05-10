package nfciue.nfcsmartanimalpassport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import nfciue.utilities.Decoderiue;

public class PartialInfoShownActivity extends AppCompatActivity {
    final Context context = this;

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
        setContentView(R.layout.activity_partial_info_shown);
        expListView = (ExpandableListView) findViewById(R.id.exp_list);
        groups = new ArrayList<String>();
        items = new HashMap<String, ArrayList<String>>();
        data = new ArrayList<ArrayList<String>>();

        // Getting data on NFC tag from ReadActivity intent.
        dataFromNFCTag = getIntent().getStringExtra("dataFromReadActivityIntent");

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) //if user logged in, show detailed info without asking pincode
        {
            Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, InfoShownInExpandedListActivity.class);
            InfoShownIntent.putExtra("dataFromPartialInfoShownActivityIntent", dataFromNFCTag);
            InfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(InfoShownIntent);
        }

        // Parsing data.
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

        groups.add("Hayvan Bilgileri");

        ArrayList<String> animalInfo = new ArrayList<String>();
        animalInfo.add("Pasaport Numarası :");
        animalInfo.add("Doğum Tarihi :");
        animalInfo.add("Cinsiyeti :");
        animalInfo.add("Cinsi :");
        animalInfo.add("Anne Pasaport Numarası :");

        items.put(groups.get(0), animalInfo);

       /* Button viewMoreButton = (Button) findViewById(R.id.buttonViewMore);
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput1 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput1);
                final EditText userInput2 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput2);
                final EditText userInput3 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput3);
                final EditText userInput4 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput4);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());


                                        int digit1 = Integer.parseInt(userInput1.getText().toString());
                                        int digit2 = Integer.parseInt(userInput2.getText().toString());
                                        int digit3 = Integer.parseInt(userInput3.getText().toString());
                                        int digit4 = Integer.parseInt(userInput4.getText().toString());

                                        if(digit1 == Integer.parseInt(String.valueOf(parsedDataFromNFCTag[40].charAt(0))) && digit2 == Integer.parseInt(String.valueOf(parsedDataFromNFCTag[40].charAt(1))) && digit3 == Integer.parseInt(String.valueOf(parsedDataFromNFCTag[40].charAt(2))) && digit4 == Integer.parseInt(String.valueOf(parsedDataFromNFCTag[40].charAt(3)))) {
                                            Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, InfoShownActivity.class);
                                            InfoShownIntent.putExtra("dataFromPartialInfoShownActivityIntent", dataFromNFCTag);
                                            InfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(InfoShownIntent);
                                        } else {
                                            Toast.makeText(context, "Pin code is wrong, please try again...", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });*/

        adapter = new ExpListAdapter(this, groups, items,data);
        expListView.setAdapter(adapter);


        navigationView = findViewById(R.id.navigationInfoShown);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_backToMain:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent ExpListIntent = new Intent(PartialInfoShownActivity.this, SignedInMainActivity.class);
                            ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ExpListIntent);
                        }
                        else{
                            Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, MainActivity.class);
                            startActivity(InfoShownIntent);
                        } break;
                    case R.id.action_prev_tag_info:
                        //todo: get a prev tag data and pass it to info_shown_in_expandable or partial_info_shown accordig to user authentication
                        break;
                    case R.id.action_view_more:    // can convert to show more..
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_pincode);
                        dialog.setTitle("Lütfen Pinkodunuzu Giriniz");
                        final EditText editTextPsw = dialog.findViewById(R.id.confirmPassword);

                        Button buttonOk = dialog.findViewById(R.id.buttonOK);
                        buttonOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String password = editTextPsw.getText().toString();

                                if(password.equals(parsedDataFromNFCTag[40])){
                                    Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, InfoShownInExpandedListActivity.class);
                                    InfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    InfoShownIntent.putExtra("dataFromPartialInfoShownActivityIntent",dataFromNFCTag);
                                    startActivity(InfoShownIntent);
                                }else{
                                    Toast.makeText(context,"Wrong Pincode,Please Try Again",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }



                            }
                        });
                        dialog.show();break;
                }
                return true;

            }});
    }







}
