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
    String opComment;



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
        opComment=getIntent().getStringExtra("opComment");



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


            String beforeOperationPart = parsedDataFromNFC.substring(0, 468); //372     // spliting tag data into two pieces to insert operation type..

            String afterOperationPart = parsedDataFromNFC.substring(469);   //373



            Log.e("1",beforeOperationPart);
            Log.e("2",afterOperationPart);



            if(operationCode != -1)
            {

                beforeOperationPart = beforeOperationPart.concat(String.valueOf(operationCode));
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
                parsedDataFromNFC = parsedDataFromNFC.substring(0, 641) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(651);
            }

            String beforeOther;
            String afterOther;
            if(vaccineCode!=-1){


                if(vaccineCode==1)    //Şap aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 129);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(130);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 651) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(661);
                }
                else if(vaccineCode==2)  //Sığır vebası aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 130);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(131);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 661) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(671);
                }
                else if(vaccineCode==3) //Theileria Annulata Aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 131);     // spliting tag data into two pieces to insert brusella vaccine true..
                    Log.e("ilkyarıBrusella",beforeOther);
                    afterOther = parsedDataFromNFC.substring(132);
                    Log.e("ikiciyarıBrusella",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 671) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(681);
                }
                else if(vaccineCode==4)  // E.Coli Aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 132);     // spliting tag data into two pieces to insert othervaccine type..
                    Log.e("ilkyarıOther",beforeOther);
                    afterOther = parsedDataFromNFC.substring(133);
                    Log.e("ikiciyarıOther",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 681) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(691);

                }
                else if(vaccineCode==5) // Buzağı Septisemi Serumu Aşısı
                {
                    beforeOther = parsedDataFromNFC.substring(0, 133);     // spliting tag data into two pieces to insert alum vaccine true..
                    Log.e("ilkyarıalum",beforeOther);
                    afterOther = parsedDataFromNFC.substring(134);
                    Log.e("ikiciyarıalum",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC = beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 691) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(701);
                }
                else if(vaccineCode==6) // Brucellosis,
                {
                    beforeOther = parsedDataFromNFC.substring(0, 134);     // spliting tag data into two pieces to insert pasteur vaccine true..
                    Log.e("ilkyarıpasteur",beforeOther);
                    afterOther = parsedDataFromNFC.substring(135);
                    Log.e("ikiciyarıpasteur",afterOther);
                    beforeOther = beforeOther.concat("T");
                    parsedDataFromNFC =beforeOther.concat(afterOther);
                    parsedDataFromNFC = parsedDataFromNFC.substring(0, 701) + Decoderiue.dateToUnixTimeText() + parsedDataFromNFC.substring(711);
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




                String operationName;
                if (operationCode == 1) {
                    operationName = "Kan Analizi";
                } else if (operationCode == 2) {
                    operationName = "Pansuman";
                } else if (operationCode == 3) {
                    operationName = "Medikal Muayene";
                } else if (operationCode == 4) {
                    operationName = "Fiziksel Muayene";
                } else if (operationCode == 5) {
                    operationName = "Medikal Operasyon";
                } else if (operationCode == 6) {
                    operationName = "Doğum";
                }else if (operationCode == 7) {
                    operationName = "Diğer";
                } else operationName = "";

                Intent OperationIntent = new Intent(WriteToTagActivity.this, InfoShownForUpdateActivity.class);
                OperationIntent.putExtra("AnimalID", animalIdForUpdate);
                OperationIntent.putExtra("OperationName", operationName);
                OperationIntent.putExtra("VaccineName", vaccineName);
                OperationIntent.putExtra("opComment", opComment);


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


                Toast.makeText(this, "Küpe Güncellendi", Toast.LENGTH_LONG).show();



                if (vaccineCode == 1) {
                    vaccineName = "Şap Aşısı";
                } else if (vaccineCode == 2) {
                    vaccineName = "Sığır Vebası Aşısı";
                } else if (vaccineCode == 3) {
                    vaccineName = "Theileria Annulata Aşısı";
                } else if (vaccineCode == 4) {
                    vaccineName = "E.Coli Aşısı";
                } else if (vaccineCode == 5) {
                    vaccineName = "Buzağı Septisemi Serumu Aşısı";
                } else if (vaccineCode == 6) {
                    vaccineName = "Brucellosis Aşısı";

                }else if (vaccineCode == 7) {
                    vaccineName = "Diğer";
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
            Toast.makeText(context,"Bir hata oluştu. Lütfen telefonu küpeye yaklaştırın ve şifrenizin doğru olduğundan emin olun.",Toast.LENGTH_LONG).show();
        } finally {
            Log.e("ULAK", "finally'nin içi.");
            tag.getReader().close();
        }
    }
}
