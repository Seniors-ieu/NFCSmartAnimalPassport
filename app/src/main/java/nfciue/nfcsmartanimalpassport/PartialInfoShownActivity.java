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
import nfciue.utilities.MyFileWriter;

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
        //Write tag data string to device for being able to read it as last read tag data.
        MyFileWriter.writeLastReadTagDataToDevice(dataFromNFCTag);

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

                                if(password.equals(parsedDataFromNFCTag[45])){
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
