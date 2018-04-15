package nfciue.nfcsmartanimalpassport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nfciue.utilities.Decoderiue;

public class PartialInfoShownActivity extends AppCompatActivity {
    final Context context = this;

    // Declaration of UI elements.
    TextView textViewAnimalID;
    TextView textViewBirthDate;
    TextView textViewGender;
    TextView textViewBreed;
    TextView textViewMotherID;
    TextView textViewBirthFarmNo;
    TextView textViewCurrentFarmNo;
    TextView textViewFarmChangeDate;
    TextView textViewOwnerTc;
    TextView textViewOwnerNameLastName;
    TextView textViewOwnerAdress;
    TextView textViewFarmGeoCoordinates;
    TextView textViewFarmCountryCode;
    TextView textViewFarmCityCode;
    TextView textViewFarmAddress;
    TextView textViewFarmPhone;
    TextView textViewFarmFax;
    TextView textViewFarmEMail;
    TextView textViewExportCountryCode;
    TextView textViewExportDate;
    TextView textViewSlaughterHouseName;
    TextView textViewSlaughterHouseAddress;
    TextView textViewSlaughterHouseLicenceNumber;
    TextView textViewSlaughterDate;
    TextView textViewDeathPlace;
    TextView textViewDeathDate;
    EditText multiLineVaccines;

    private String dataFromNFCTag;
    private String[] parsedDataFromNFCTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_info_shown);

        // Initialization of UI elements.
        textViewAnimalID = (TextView) findViewById(R.id.textViewAnimalID);
        textViewBirthDate = (TextView) findViewById(R.id.textViewBirthDate);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        textViewBreed = (TextView) findViewById(R.id.textViewBreed);

        // Getting data on NFC tag from ReadActivity intent.
        dataFromNFCTag = getIntent().getStringExtra("dataFromReadActivityIntent");

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, InfoShownActivity.class);
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

        try {
            textViewAnimalID.setText(parsedDataFromNFCTag[0]);
            textViewBirthDate.setText(parsedDataFromNFCTag[1]);
            textViewGender.setText(parsedDataFromNFCTag[2]);
            textViewBreed.setText(parsedDataFromNFCTag[3]);
        } catch (Exception e) {
            Toast.makeText(this, "change " + e.getMessage() + " Changing texts", Toast.LENGTH_LONG).show();
        }

        Button viewMoreButton = (Button) findViewById(R.id.buttonViewMore);
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
        });
    }







}
