package nfciue.nfcsmartanimalpassport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.ndef.INdefMessage;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.ntag.INTag;
import com.nxp.nfclib.ntag.NTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nfciue.nfcaes.BackAES;
import nfciue.utilities.Decoderiue;
import nfciue.utilities.MyFileReader;

public class WriteToTagActivity extends AppCompatActivity {
    final Context context = this;

    byte[] myPass;
    String passwordOfVet;
    byte[] myPassword;
    String textToNFC;
    int operationCode;
    int vaccineCode;
    String animalIdForUpdate;
    String vaccineName="NA";
    String operationName="NA";
    String encryptedWriteProtectionKey;



    // The license key, got from TapLinx.
    private String m_strKey = "a8ecf9cd118701724d7a24c76c9495f0";

    // The TapLinx library instance.
    private NxpNfcLib m_libInstance = null;

    // Initialize the TapLinx library.
    private void initializeLibrary() {
        m_libInstance = NxpNfcLib.getInstance();
        m_libInstance.registerActivity(this, m_strKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_tag);


        initializeLibrary();    // Initialize library.


        // Taking parameters from previous intent.
        passwordOfVet = Decoderiue.vetPasswordComplementer(getIntent().getStringExtra("password")); // Vet password is complemented to 16 chars.
        operationCode = getIntent().getIntExtra("operationCode",-1);
        vaccineCode= getIntent().getIntExtra("vaccineCode",-1);




    }

    // Called if app becomes active.
    @Override
    protected void onResume() {
        m_libInstance.startForeGroundDispatch();
        super.onResume();
    }

    // Called if app becomes inactive.
    @Override
    protected void onPause() {
        m_libInstance.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("ULAK", "onNewIntent");
        cardLogic(intent);
        super.onNewIntent(intent);
    }

    private void cardLogic(final Intent intent) {
        CardType type = CardType.UnknownCard;
        try {
            type = m_libInstance.getCardType(intent);
            Log.d("ULAK", "Card type found: " + type.getTagName());
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        switch (type) {
            case NTag216:
                try {
                    ntagCardLogic(NTagFactory.getInstance().getNTAG216(m_libInstance.getCustomModules()));
                } catch (Throwable t) {
                    Log.e("ULAK", "Error in switch(NTag216) " + t.getMessage());
                }
                break;
            default:
                Log.e("ULAK", "An error occured when getting card logic.");
                break;
        }
    }

    private void ntagCardLogic(final INTag tag) {
        Log.e("ULAK", "ntagCardLogic'in içi");


        try {
            Log.e("ULAK", "ntagCardLogic11111");
            tag.getReader().connect();
            NTag213215216 ntag216 = (NTag213215216) tag;
            String decryptedPassword = "";

            try {
                encryptedWriteProtectionKey = MyFileReader.readEncryptedKeyFromFile();
                decryptedPassword = BackAES.decrypt(encryptedWriteProtectionKey, passwordOfVet, 0);  // It will take encrypted text(write protection key) and vet's password as parameter.
                Log.e("DECRYPTED PASS", decryptedPassword);
            } catch(Exception e) {
                Log.e("DECRYPTED PASS2", e.getMessage());
            }

            // It(write protection key) will be decrypted to a string above and then parsed(casted) to a byte array.
            //byte[] myPassword = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4};
            byte[] myPassword = new byte[]{(byte) Integer.parseInt(String.valueOf(decryptedPassword.charAt(0))), (byte) Integer.parseInt(String.valueOf(decryptedPassword.charAt(1))), (byte) Integer.parseInt(String.valueOf(decryptedPassword.charAt(2))), (byte) Integer.parseInt(String.valueOf(decryptedPassword.charAt(3)))};
            // Default value of ack.
            byte[] myAck = new byte[] {(byte) 0x00, (byte) 0x00};


            INdefMessage nm = ntag216.readNDEF();  //read process begins
            NdefMessageWrapper nmw = new NdefMessageWrapper(nm.toByteArray());

            String dataFromNFCTag = new String(nmw.getRecords()[0].getPayload()); // tag data
            animalIdForUpdate = dataFromNFCTag.substring(3,14); //Firebase update yapan activitye gönderilecek..

            String parsedDataFromNFC = dataFromNFCTag.substring(3);   // her yazma operasyonunda tagin başına gelen dil kodunu atmak için..

            // read codes end..


            String beforeOperationPart = parsedDataFromNFC.substring(0, 372);     // spliting tag data into two pieces to insert operation type..

            String afterOperationPart = parsedDataFromNFC.substring(373);



            Log.e("1",beforeOperationPart);
            Log.e("2",afterOperationPart);



            if(operationCode==1)
            {

                beforeOperationPart = beforeOperationPart.concat("1");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==2)
            {
                beforeOperationPart = beforeOperationPart.concat("2");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==3)
            {
                beforeOperationPart = beforeOperationPart.concat("3");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==4)
            {
                beforeOperationPart = beforeOperationPart.concat("4");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==5)
            {
                beforeOperationPart = beforeOperationPart.concat("5");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==6)
            {
                beforeOperationPart = beforeOperationPart.concat("6");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            else if(operationCode==7)
            {
                beforeOperationPart = beforeOperationPart.concat("7");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 533) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(537);
            }
            String beforeOther;
            String afterOther;
            if(vaccineCode!=-1){


                if(vaccineCode==1)    //Theileria Aşısı , other
                {
                    beforeOther = parsedDataFromNFC.substring(0, 95);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(96);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("1");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 537) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(541);
                }
                else if(vaccineCode==2)  //Escherichia Coli Aşısı , other
                {
                    beforeOther = parsedDataFromNFC.substring(0, 95);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(96);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("2");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 537) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(541);
                }
                else if(vaccineCode==3) //Brusella Aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 93);     // spliting tag data into two pieces to insert brusella vaccine true..
                    Log.e("ilkyarıBrusella",beforeOther);
                    afterOther = parsedDataFromNFC.substring(94);
                    Log.e("ikiciyarıBrusella",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 545) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(549);
                }
                else if(vaccineCode==4)  // Mantar Aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 95);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(96);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("3");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 537) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(541);

                }
                else if(vaccineCode==5) // şap aşısı, alum vaccine
                {
                    beforeOther = parsedDataFromNFC.substring(0, 92);     // spliting tag data into two pieces to insert alum vaccine true..
                    Log.e("ilkyarıalum",beforeOther);
                    afterOther = parsedDataFromNFC.substring(93);
                    Log.e("ikiciyarıalum",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC = beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 541) + Decoderiue.dateToAscii() + parsedDataFromNFC.substring(545);
                }
                else if(vaccineCode==6) // pasteurallea,
                {
                    beforeOther = parsedDataFromNFC.substring(0, 94);     // spliting tag data into two pieces to insert pasteur vaccine true..
                    Log.e("ilkyarıpasteur",beforeOther);
                    afterOther = parsedDataFromNFC.substring(95);
                    Log.e("ikiciyarıpasteur",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                }


            }


            if(operationCode > 0)  // operation update
            {
                String ndefMessage = parsedDataFromNFC;

                byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
                byte[] text = ndefMessage.getBytes("UTF-8"); // Content in UTF-8

                int langSize = lang.length;
                int textLength = text.length;

                ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
                payload.write((byte) (langSize & 0x1F));
                payload.write(lang, 0, langSize);
                payload.write(text, 0, textLength);
                NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                        NdefRecord.RTD_TEXT, new byte[0],
                        payload.toByteArray());
                NdefMessage ndefMessage1 = new NdefMessage(new NdefRecord[]{record});

                NdefRecordWrapper nrw = new NdefRecordWrapper(record);
                nmw = new NdefMessageWrapper(nrw);
                nm = nmw;

                ntag216.authenticatePwd(myPassword, myAck);
                ntag216.writeNDEF(nm);


                Toast.makeText(this, "OK from WTTA!!!", Toast.LENGTH_LONG).show();


                String operationName;
                if (operationCode == 1) {
                    operationName = "Blood Analysis";
                } else if (operationCode == 2) {
                    operationName = "Dressing";
                } else if (operationCode == 3) {
                    operationName = "Medical Examination";
                } else if (operationCode == 4) {
                    operationName = "Physical Examination";
                } else if (operationCode == 5) {
                    operationName = "Medical Operation ";
                } else if (operationCode == 6) {
                    operationName = "Birth";
                } else operationName = "";

                Intent OperationIntent = new Intent(WriteToTagActivity.this, InfoShownForUpdateActivity.class);
                OperationIntent.putExtra("AnimalID", animalIdForUpdate);
                OperationIntent.putExtra("OperationName", operationName);
                OperationIntent.putExtra("VaccineName", vaccineName);

                OperationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(OperationIntent);
            }

            if(vaccineCode > 0)  // vaccine update
            {
                String ndefMessage = parsedDataFromNFC;

                byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
                byte[] text = ndefMessage.getBytes("UTF-8"); // Content in UTF-8

                int langSize = lang.length;
                int textLength = text.length;

                ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
                payload.write((byte) (langSize & 0x1F));
                payload.write(lang, 0, langSize);
                payload.write(text, 0, textLength);
                NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                        NdefRecord.RTD_TEXT, new byte[0],
                        payload.toByteArray());
                NdefMessage ndefMessage1 = new NdefMessage(new NdefRecord[]{record});

                NdefRecordWrapper nrw = new NdefRecordWrapper(record);
                nmw = new NdefMessageWrapper(nrw);
                nm = nmw;

                ntag216.authenticatePwd(myPassword, myAck);
                ntag216.writeNDEF(nm);


                Toast.makeText(this, "OK from WTTA!!!", Toast.LENGTH_LONG).show();



                if (vaccineCode == 1) {
                    vaccineName = "Theileria";
                } else if (vaccineCode == 2) {
                    vaccineName = "Escherichia Coli";
                } else if (vaccineCode == 3) {
                    vaccineName = "Brusella";
                } else if (vaccineCode == 4) {
                    vaccineName = "Mantar";
                } else if (vaccineCode == 5) {
                    vaccineName = "Alum";
                } else if (vaccineCode == 6) {
                    vaccineName = "Pasteurella";
                } else vaccineName = "";

                Intent VaccineIntent = new Intent(WriteToTagActivity.this, InfoShownForUpdateActivity.class);
                VaccineIntent.putExtra("AnimalID", animalIdForUpdate);
                VaccineIntent.putExtra("VaccineName", vaccineName);
                VaccineIntent.putExtra("OperationName", operationName);

                VaccineIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(VaccineIntent);
            }




        } catch (Exception e) {
            Log.e("ULAK", "Error in ntagCardLogic: Password is wrong... " + e.getMessage() + e.getLocalizedMessage());
        } finally {
            Log.e("ULAK", "finally'nin içi.");
            tag.getReader().close();
        }
    }
}
