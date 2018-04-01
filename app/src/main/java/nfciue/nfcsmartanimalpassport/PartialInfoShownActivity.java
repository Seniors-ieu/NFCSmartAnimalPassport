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
//        textViewMotherID = (TextView) findViewById(R.id.textViewMotherID);
//        textViewBirthFarmNo = (TextView) findViewById(R.id.textViewBirthFarmNo);
//        textViewCurrentFarmNo = (TextView) findViewById(R.id.textViewCurrentFarmNo);
//        textViewFarmChangeDate = (TextView) findViewById(R.id.textViewFarmChangeDate);
//        textViewOwnerTc = (TextView) findViewById(R.id.textViewOwnerTc);
//        textViewOwnerNameLastName = (TextView) findViewById(R.id.textViewOwnerNameLastName);
//        textViewOwnerAdress = (TextView) findViewById(R.id.textViewOwnerAdress);
//        textViewFarmGeoCoordinates = (TextView) findViewById(R.id.textViewFarmGeoCoordinates);
//        textViewFarmCountryCode = (TextView) findViewById(R.id.textViewFarmCountryCode);
//        textViewFarmCityCode = (TextView) findViewById(R.id.textViewFarmCityCode);
//        textViewFarmAddress = (TextView) findViewById(R.id.textViewFarmAddress);
//        textViewFarmPhone = (TextView) findViewById(R.id.textViewFarmPhone);
//        textViewFarmFax = (TextView) findViewById(R.id.textViewFarmFax);
//        textViewFarmEMail = (TextView) findViewById(R.id.textViewFarmEMail);
//        textViewExportCountryCode = (TextView) findViewById(R.id.textViewExportCountryCode);
//        textViewExportDate = (TextView) findViewById(R.id.textViewExportDate);
//        textViewSlaughterHouseName = (TextView) findViewById(R.id.textViewSlaughterHouseName);
//        textViewSlaughterHouseAddress = (TextView) findViewById(R.id.textViewSlaughterHouseAddress);
//        textViewSlaughterHouseLicenceNumber = (TextView) findViewById(R.id.textViewSlaughterHouseLicenceNumber);
//        textViewSlaughterDate = (TextView) findViewById(R.id.textViewSlaughterDate);
//        textViewDeathPlace = (TextView) findViewById(R.id.textViewDeathPlace);
//        textViewDeathDate = (TextView) findViewById(R.id.textViewDeathDate);
//        multiLineVaccines = (EditText) findViewById(R.id.multiLineVaccines);

        // Getting data on NFC tag from ReadActivity intent.
        dataFromNFCTag = getIntent().getStringExtra("dataFromReadActivityIntent");

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
//            textViewMotherID.setText(parsedDataFromNFCTag[4]);
//            textViewBirthFarmNo.setText(parsedDataFromNFCTag[5]);
//            textViewCurrentFarmNo.setText(parsedDataFromNFCTag[6]);
//            textViewFarmChangeDate.setText(parsedDataFromNFCTag[7]);
//            textViewOwnerTc.setText(parsedDataFromNFCTag[25]);
//            textViewOwnerNameLastName.setText(parsedDataFromNFCTag[24]);
//            textViewOwnerAdress.setText(parsedDataFromNFCTag[26]);
//            textViewFarmGeoCoordinates.setText(parsedDataFromNFCTag[28]);
//            textViewFarmCountryCode.setText(parsedDataFromNFCTag[22]);
//            textViewFarmCityCode.setText(parsedDataFromNFCTag[23]);
//            textViewFarmAddress.setText(parsedDataFromNFCTag[27]);
//            textViewFarmPhone.setText(parsedDataFromNFCTag[29]);
//            textViewFarmFax.setText(parsedDataFromNFCTag[30]);
//            textViewFarmEMail.setText(parsedDataFromNFCTag[31]);
//            textViewExportCountryCode.setText(parsedDataFromNFCTag[8]);
//            textViewExportDate.setText(parsedDataFromNFCTag[9]);
//            textViewSlaughterHouseName.setText(parsedDataFromNFCTag[16]);
//            textViewSlaughterHouseAddress.setText(parsedDataFromNFCTag[17] + parsedDataFromNFCTag[18] + parsedDataFromNFCTag[19]);
//            textViewSlaughterHouseLicenceNumber.setText(parsedDataFromNFCTag[20]);
//            textViewSlaughterDate.setText(parsedDataFromNFCTag[21]);
//            textViewDeathPlace.setText(parsedDataFromNFCTag[11]);
//            textViewDeathDate.setText(parsedDataFromNFCTag[10]);



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

                                        if(digit1 == 9 && digit2 == 9 && digit3 == 9 && digit4 == 9) {
                                            Intent InfoShownIntent = new Intent(PartialInfoShownActivity.this, InfoShownActivity.class);
                                            InfoShownIntent.putExtra("dataFromPartialInfoShownActivityIntent", dataFromNFCTag);
                                            InfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(InfoShownIntent);
                                        } else {
                                            Toast.makeText(context, "Pin code is wrong, please try again...", Toast.LENGTH_LONG).show();
                                            // create alert dialog
                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                            // show it
                                            alertDialog.show();
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
