package nfciue.nfcsmartanimalpassport;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import nfciue.utilities.Decoderiue;
//todo: this activity is not needed anymore, will be deleted before due date.
public class InfoShownActivity extends AppCompatActivity {
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
    TextView multiLineVaccines;
    TextView multiLineOperations;
    Button updateTag;
    Button backToMenu;

    private String dataFromNFCTag;
    private String[] parsedDataFromNFCTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown);

        Log.e("ULAK", "InfoShownActivity works...");
        // Initialization of UI elements.
        textViewAnimalID = (TextView) findViewById(R.id.textViewAnimalID);
        textViewBirthDate = (TextView) findViewById(R.id.textViewBirthDate);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        textViewBreed = (TextView) findViewById(R.id.textViewBreed);
        textViewMotherID = (TextView) findViewById(R.id.textViewMotherID);
        textViewBirthFarmNo = (TextView) findViewById(R.id.textViewBirthFarmNo);
        textViewCurrentFarmNo = (TextView) findViewById(R.id.textViewCurrentFarmNo);
        textViewFarmChangeDate = (TextView) findViewById(R.id.textViewFarmChangeDate);
        textViewOwnerTc = (TextView) findViewById(R.id.textViewOwnerTc);
        textViewOwnerNameLastName = (TextView) findViewById(R.id.textViewOwnerNameLastName);
        textViewOwnerAdress = (TextView) findViewById(R.id.textViewOwnerAdress);
        textViewFarmGeoCoordinates = (TextView) findViewById(R.id.textViewFarmGeoCoordinates);
        textViewFarmCountryCode = (TextView) findViewById(R.id.textViewFarmCountryCode);
        textViewFarmCityCode = (TextView) findViewById(R.id.textViewFarmCityCode);
        textViewFarmAddress = (TextView) findViewById(R.id.textViewFarmAddress);
        textViewFarmPhone = (TextView) findViewById(R.id.textViewFarmPhone);
        textViewFarmFax = (TextView) findViewById(R.id.textViewFarmFax);
        textViewFarmEMail = (TextView) findViewById(R.id.textViewFarmEMail);
        textViewExportCountryCode = (TextView) findViewById(R.id.textViewExportCountryCode);
        textViewExportDate = (TextView) findViewById(R.id.textViewExportDate);
        textViewSlaughterHouseName = (TextView) findViewById(R.id.textViewSlaughterHouseName);
        textViewSlaughterHouseAddress = (TextView) findViewById(R.id.textViewSlaughterHouseAddress);
        textViewSlaughterHouseLicenceNumber = (TextView) findViewById(R.id.textViewSlaughterHouseLicenceNumber);
        textViewSlaughterDate = (TextView) findViewById(R.id.textViewSlaughterDate);
        textViewDeathPlace = (TextView) findViewById(R.id.textViewDeathPlace);
        textViewDeathDate = (TextView) findViewById(R.id.textViewDeathDate);
        multiLineVaccines = (TextView) findViewById(R.id.multiLineVaccines);
        multiLineOperations = (TextView) findViewById(R.id.multiLineOperations);
        updateTag= findViewById(R.id.buttonToWriteActivity);
        backToMenu= findViewById(R.id.buttonToMainActivity);

        // Getting data on NFC tag from ReadActivity intent.
        dataFromNFCTag = getIntent().getStringExtra("dataFromPartialInfoShownActivityIntent");

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
            textViewMotherID.setText(parsedDataFromNFCTag[4]);
            textViewBirthFarmNo.setText(parsedDataFromNFCTag[5]);
            textViewCurrentFarmNo.setText(parsedDataFromNFCTag[6]);
            textViewFarmChangeDate.setText(parsedDataFromNFCTag[7]);
            textViewOwnerTc.setText(parsedDataFromNFCTag[25]);
            textViewOwnerNameLastName.setText(parsedDataFromNFCTag[24]);
            textViewOwnerAdress.setText(parsedDataFromNFCTag[26]);
            textViewFarmGeoCoordinates.setText(parsedDataFromNFCTag[28]);
            textViewFarmCountryCode.setText(parsedDataFromNFCTag[22]);
            textViewFarmCityCode.setText(parsedDataFromNFCTag[23]);
            textViewFarmAddress.setText(parsedDataFromNFCTag[27]);
            textViewFarmPhone.setText(parsedDataFromNFCTag[29]);
            textViewFarmFax.setText(parsedDataFromNFCTag[30]);
            textViewFarmEMail.setText(parsedDataFromNFCTag[31]);
            textViewExportCountryCode.setText(parsedDataFromNFCTag[8]);
            textViewExportDate.setText(parsedDataFromNFCTag[9]);
            textViewSlaughterHouseName.setText(parsedDataFromNFCTag[16]);
            textViewSlaughterHouseAddress.setText(parsedDataFromNFCTag[17] + parsedDataFromNFCTag[18] + parsedDataFromNFCTag[19]);
            textViewSlaughterHouseLicenceNumber.setText(parsedDataFromNFCTag[20]);
            textViewSlaughterDate.setText(parsedDataFromNFCTag[21]);
            textViewDeathPlace.setText(parsedDataFromNFCTag[11]);
            textViewDeathDate.setText(parsedDataFromNFCTag[10]);
            multiLineVaccines.setText("Alum Vaccine: " + parsedDataFromNFCTag[12] + "\nTarih:" + parsedDataFromNFCTag[43] + "\nBrucellosis Vaccine: " + parsedDataFromNFCTag[13] + "\nTarih: " + parsedDataFromNFCTag[44] + "\nPasturella Vaccine: " + parsedDataFromNFCTag[14]);
            multiLineOperations.setText(Decoderiue.operationChooser(parsedDataFromNFCTag[33]) + "\nTarih:" + parsedDataFromNFCTag[41]);
        } catch (Exception e) {
            Toast.makeText(this, "change " + e.getMessage() + " Changing texts", Toast.LENGTH_LONG).show();
        }
        updateTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    Intent InfoShownIntent = new Intent(InfoShownActivity.this, ChooseVaccineOperationActivity.class);
                    startActivity(InfoShownIntent);
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(InfoShownActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("You are not authorized to write tag! Please Log in ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent InfoShownIntent = new Intent(InfoShownActivity.this, MainActivity.class);
                                    startActivity(InfoShownIntent);
                                }
                            });
                    alertDialog.show();

                }
            }
        });
        backToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    Intent ExpListIntent = new Intent(InfoShownActivity.this, InfoShownInExpandedListActivity.class);
                    ExpListIntent.putExtra("dataFromReadActivityIntent", dataFromNFCTag);
                    ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ExpListIntent);
                }
                else{
                    Intent InfoShownIntent = new Intent(InfoShownActivity.this, MainActivity.class);
                    startActivity(InfoShownIntent);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

    }

}
