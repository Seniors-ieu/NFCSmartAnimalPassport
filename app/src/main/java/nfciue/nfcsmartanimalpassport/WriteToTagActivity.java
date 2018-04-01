package nfciue.nfcsmartanimalpassport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WriteToTagActivity extends AppCompatActivity {
    final Context context = this;

    byte[] myPass;
    String textToNFC;
    int operationCode;
    int vaccineCode;
    String animalIdForUpdate;

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

        myPass = getIntent().getByteArrayExtra("password");
        operationCode = getIntent().getIntExtra("operationCode",-1);




       // myPass = getIntent().getByteArrayExtra("myPass");
       // textToNFC = getIntent().getStringExtra("textToNFC");
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
                    ntagCardLogic(NTagFactory.getInstance().getNTAG216(m_libInstance.getCustomModules()), textToNFC, myPass);
                } catch (Throwable t) {
                    Log.e("ULAK", "Error in switch(NTag216) " + t.getMessage());
                }
                break;
            default:
                Log.e("ULAK", "An error occured when getting card logic.");
                break;
        }
    }

    private void ntagCardLogic(final INTag tag, String dataTextToNFC, byte[] pass) {
        Log.e("ULAK", "ntagCardLogic'in içi");


        try {
            Log.e("ULAK", "ntagCardLogic11111");
            tag.getReader().connect();
            NTag213215216 ntag216 = (NTag213215216) tag;

            byte[] myPassword = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4};
           // byte[] myPassword = myPass;
            byte[] myAck = new byte[] {(byte) 0x00, (byte) 0x00};
            byte[] defPass = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};




            INdefMessage nm = ntag216.readNDEF();  //read process begins

            NdefMessageWrapper nmw = new NdefMessageWrapper(nm.toByteArray());
            byte [] byteArrayOfData = nmw.getRecords()[0].getPayload();
            System.out.println(byteArrayOfData);
            String dataFromNFCTag = new String(nmw.getRecords()[0].getPayload()); // tag data
            animalIdForUpdate = dataFromNFCTag.substring(3,14); //Firebase update yapan activitye gönderilecek..

           String parsedDataFromNFC=dataFromNFCTag.substring(3);   // her yazma operasyonunda tagin başına gelen dil kodunu atmak için..

            // read codes end..


            String beforeOperationPart = parsedDataFromNFC.substring(0, 372);     // spliting tag data into two pieces to insert operation type..

            String afterOperationPart = parsedDataFromNFC.substring(373);



            Log.e("1",beforeOperationPart);
            Log.e("2",afterOperationPart);



            if(operationCode==1)
            {

                beforeOperationPart = beforeOperationPart.concat("1");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==2)
            {
                beforeOperationPart = beforeOperationPart.concat("2");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==3)
            {
                beforeOperationPart = beforeOperationPart.concat("3");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==4)
            {
                beforeOperationPart = beforeOperationPart.concat("4");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==5)
            {
                beforeOperationPart = beforeOperationPart.concat("5");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==6)
            {
                beforeOperationPart = beforeOperationPart.concat("6");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }
            else if(operationCode==7)
            {
                beforeOperationPart = beforeOperationPart.concat("7");
                parsedDataFromNFC = beforeOperationPart.concat(afterOperationPart);
            }



            String ndefMessage = parsedDataFromNFC ;

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
            if(operationCode==1)
            {
                operationName="Blood Analysis";
            }
            else if(operationCode==2)
            {
                operationName="Dressing";
            }
            else if(operationCode==3)
            {
                operationName="Medical Examination";
            }
            else  if(operationCode==4)
            {
                operationName="Physical Examination";
            }
            else  if(operationCode==5)
            {
                operationName="Medical Operation ";
            }
            else if( operationCode==6)
            {
                operationName="Birth";
            }
            else operationName="";

            Intent OperationIntent = new Intent(WriteToTagActivity.this, InfoShownForUpdateActivity.class);
            OperationIntent.putExtra("AnimalID",animalIdForUpdate);
            OperationIntent.putExtra("OperationName",operationName);

            OperationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(OperationIntent);




        } catch (Exception e) {
            Log.e("ULAK", "Error in ntagCardLogic: Password is wrong... " + e.getMessage() + e.getLocalizedMessage());
        } finally {
            Log.e("ULAK", "finally'nin içi.");
            tag.getReader().close();
            //NTag213215216 tag1 = (NTag213215216) tag;  //ULAK buradan devam edebilirsinnn.

        }
    }

    public void updateOperationFirebase(String nfcData){

        //todo: bütün stringi bölerek animal objesini parametreleriyle kendin yarat : offline running
         //animalIdForUpdate = nfcData.substring(3,14);
        animalIdForUpdate="TR 35 123";
        Log.e("animalıd",animalIdForUpdate);  //TR351234567

        final Animal animal = new Animal();
        animal.setAlumVaccine(true);
        animal.setBirthdate("16.05.2010");
        animal.setBirthFarmNo("55567533");
        animal.setBreed("Holstein");
        animal.setBrusellosisVaccine(false);
        animal.setCurrentFarmNo("1234567");
        animal.setDeathDate("NA");
        animal.setDeathPlace("NA");
        animal.seteSignDirector("14567SFGHJK4567");
        animal.seteSignOwner("1235WRTY357");
        animal.setExportCountryCode(0);
        animal.setExportDate("NA");
        animal.setFarmChangeDate("NA");
        animal.setiD(animalIdForUpdate);
        animal.setFemale(false);
        animal.setMotherId("TR 35 122");


        readVaccineForUpdate(new VaccineCallback() { //All vaccines will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback( ArrayList<otherVaccine> otherVaccines) {     //otherVaccines veritabanından gelen aşılar
                animal.setOtherVaccine(otherVaccines);
            }
        });


        readOperationForUpdate(new OperationCallback() { //All operations will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback(ArrayList <Operations> operations) {     //otherVaccines veritabanından gelen aşılar
                animal.setOperations(operations);
            }
        });

        animal.setOwnerTc("11111111111");
        animal.setPasturellaVaccine(true);// with the information that is retrieved from Tag, initialize animal with parameters. All fields should match with db , othervise use call back to retrieve whole object from db.. (aslı)
        //Owner info won't be updated by user for now, if this changes, initialize an owner also.

        String operationName;
        if(operationCode==1)
        {
             operationName="Blood Analysis";
        }
        else if(operationCode==2)
        {
            operationName="Dressing";
        }
        else if(operationCode==3)
        {
            operationName="Medical Examination";
        }
        else  if(operationCode==4)
        {
            operationName="Physical Examination";
        }
        else  if(operationCode==5)
        {
            operationName="Medical Operation ";
        }
        else if( operationCode==6)
        {
            operationName="Birth";
        }
        else operationName="";


            // write to db begins
        Operations operation = new Operations(operationName,"1111111",new Date());   //todo:operator id will be replaced with a real one;
        animal.getOperations().add(operation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Animals").document(animal.getiD()).set(animal);

    }   // bunlar büyük ihtimal hiç kullanılmayacak, gerektiğinde ben silerim (Aslı)
    public interface VaccineCallback {
        void onCallback(ArrayList <otherVaccine> otherVaccines);
    }
    public void readVaccineForUpdate(final WriteToTagActivity.VaccineCallback updateCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Animals").document(animalIdForUpdate);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Animal animal= documentSnapshot.toObject(Animal.class);
                ArrayList<otherVaccine> otherVaccines = animal.getOtherVaccine();
                updateCallback.onCallback(otherVaccines);

            }
        });
    }

    public interface OperationCallback {
        void onCallback(ArrayList <Operations> operations);
    }
    public void readOperationForUpdate(final WriteToTagActivity.OperationCallback updateCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Animals").document(animalIdForUpdate);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Animal animal= documentSnapshot.toObject(Animal.class);
                ArrayList<Operations> operations = animal.getOperations();
                updateCallback.onCallback(operations);

            }
        });
    }
}
