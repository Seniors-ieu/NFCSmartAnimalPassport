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
    EditText multiLineVaccines;

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
        multiLineVaccines = (EditText) findViewById(R.id.multiLineVaccines);

        // Getting data on NFC tag from ReadActivity intent.
        dataFromNFCTag = getIntent().getStringExtra("dataFromPartialInfoShownActivityIntent");

        // Parsing data.
        try {
            parsedDataFromNFCTag = parseNFCData(dataFromNFCTag);
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
        } catch (Exception e) {
            Toast.makeText(this, "change " + e.getMessage() + " Changing texts", Toast.LENGTH_LONG).show();
        }


    }

    // Function for parsing string on the NFC tag.
    private String[] parseNFCData(String dataOnNFCTag) {
        String[] parsedData = new String[41];
        Toast.makeText(this, "Size: " + dataOnNFCTag.length(), Toast.LENGTH_LONG).show();
        parsedData[0] = dataOnNFCTag.substring(3, 14);  // Animal ID - 1
        parsedData[1] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(14, 18)); // Animal Birthdate - 2
        parsedData[2] = dataOnNFCTag.substring(18, 19); // Animal Gender - 3
        parsedData[3] = dataOnNFCTag.substring(19, 23); // Animal Breed - 4
        parsedData[4] = dataOnNFCTag.substring(23, 34); // Animal MotherID - 5
        parsedData[5] = dataOnNFCTag.substring(34, 38); // Animal birth farm no - 6
        parsedData[6] = dataOnNFCTag.substring(38, 42); // Animal current farm no - 7
        parsedData[7] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(42, 46)); // Animal farm change data - 8
        parsedData[8] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(46, 47)); // Animal export country code - 9
        parsedData[9] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(47, 51)); // Animal export date - 10
        parsedData[10] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(51, 55)); // Animal death date - 11
        parsedData[11] = dataOnNFCTag.substring(55, 95); // Animal death place - 12
        parsedData[12] = dataOnNFCTag.substring(95, 96); // Animal alum vaccine - 13
        parsedData[13] = dataOnNFCTag.substring(96, 97); // Animal brucellosis vaccine - 14
        parsedData[14] = dataOnNFCTag.substring(97, 98); // Animal pasturella vaccine - 15
        parsedData[15] = dataOnNFCTag.substring(98, 99); // Animal other vaccine - 16
        parsedData[16] = dataOnNFCTag.substring(99, 139);   // Slaughter house name - 17
        parsedData[17] = dataOnNFCTag.substring(139, 154);  // Slaughter house address mahalle - 18
        parsedData[18] = dataOnNFCTag.substring(154, 164);  // Slaughter house address sokak - 18
        parsedData[19] = dataOnNFCTag.substring(164, 179);  // Slaughter house address remaining address info - 18
        parsedData[20] = dataOnNFCTag.substring(179, 183);  // Slaughter house licence number - 19
        parsedData[21] = Decoderiue.asciiToDateText(dataOnNFCTag.substring(183, 187));  // Slaughter date - 20
        parsedData[22] = Decoderiue.countryAsciiToName(dataOnNFCTag.substring(187, 188));  // Farm country code - 21
        parsedData[23] = Decoderiue.cityAsciiToName(dataOnNFCTag.substring(188, 189));  // Farm city code - 22
        parsedData[24] = dataOnNFCTag.substring(189, 205) + dataOnNFCTag.substring(205, 221);  // Owner name + surname - 23 + 24
        parsedData[25] = Decoderiue.asciiTCNoToRealTC(dataOnNFCTag.substring(221, 226));  // Owner TC no - 25
        parsedData[26] = dataOnNFCTag.substring(226, 266);  // Owner residence address - 26
        parsedData[27] = dataOnNFCTag.substring(266, 306);  // Farm address - 27
        parsedData[28] = dataOnNFCTag.substring(306, 312);  // Farm geo coordinates - 28
        parsedData[29] = dataOnNFCTag.substring(312, 317);  // Farm phone number - 29
        parsedData[30] = dataOnNFCTag.substring(317, 322);  // Farm fax number - 30
        parsedData[31] = dataOnNFCTag.substring(322, 370);  // Farm email address - 31
        parsedData[32] = dataOnNFCTag.substring(370, 375);  // Operator ID - 32
        parsedData[33] = dataOnNFCTag.substring(375, 376);  // Operation type - 33
        parsedData[34] = dataOnNFCTag.substring(376, 424);  // Esign of owner - 34
        parsedData[35] = dataOnNFCTag.substring(424, 464);  // Name of institution - 35
        parsedData[36] = dataOnNFCTag.substring(464, 512);  // Esign of director - 36
        parsedData[37] = dataOnNFCTag.substring(512, 516);  // Creation date of passport - 37
        parsedData[38] = dataOnNFCTag.substring(516, 528);  // Barcode number - 38
        parsedData[39] = dataOnNFCTag.substring(528, 532);  // Timestamp - 39
        parsedData[40] = dataOnNFCTag.substring(532, 536);  // Pincode


        return parsedData;
    }
}
