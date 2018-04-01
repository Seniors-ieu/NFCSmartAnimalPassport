package nfciue.nfcsmartanimalpassport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nfciue.utilities.Decoderiue;

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
            multiLineVaccines.setText("Alum Vaccine: " + parsedDataFromNFCTag[12] + "---" + "Brucellosis Vaccine: " + parsedDataFromNFCTag[13] + "---" + "Pasturella Vaccine: " + parsedDataFromNFCTag[14]);
            multiLineOperations.setText(Decoderiue.operationChooser(parsedDataFromNFCTag[33]) + "tarih");
        } catch (Exception e) {
            Toast.makeText(this, "change " + e.getMessage() + " Changing texts", Toast.LENGTH_LONG).show();
        }
    }
}
