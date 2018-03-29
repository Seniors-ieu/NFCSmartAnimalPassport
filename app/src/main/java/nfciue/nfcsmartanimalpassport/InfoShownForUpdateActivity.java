package nfciue.nfcsmartanimalpassport;

import android.app.Dialog;
import android.content.Context;
import android.media.VolumeShaper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class InfoShownForUpdateActivity extends AppCompatActivity {

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
    EditText multiLineOperations;
    Button buttonAddOperation;
    Button buttonAddVaccine;
    String animalIdForUpdate;
    ArrayList<otherVaccine> vaccinesStoredIndb;
    ArrayList<Operations> operationsStoredINdb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_for_update);
        final Context context = this;


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
        multiLineOperations = (EditText) findViewById(R.id.multiLineOperations);

        //read tag and fill textboxes (anıl)...


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
        animal.setiD("TR 35 123");
        animalIdForUpdate = animal.getiD();
        animal.setFemale(false);
        animal.setMotherId("TR 35 122");


        readVaccineForUpdate(new VaccineCallback() { //All vaccines will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback( ArrayList <otherVaccine> otherVaccines) {     //otherVaccines veritabanından gelen aşılar
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


        buttonAddOperation = findViewById(R.id.buttonAddOperation);
        buttonAddVaccine= findViewById(R.id.buttonAddVaccine);

        buttonAddOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_operation);
                dialog.setTitle("Add New Operation");

                // set the custom dialog components - text, image and button
                final EditText newOperation =dialog.findViewById(R.id.editTextOperation);
                Button buttonAdd = dialog.findViewById(R.id.buttonAdd);

                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String operationName= newOperation.getText().toString();
                        Operations operation = new Operations(operationName,"1111111",new Date());
                        animal.getOperations().add(operation);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Animals").document(animal.getiD()).set(animal);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        buttonAddVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_operation);
                dialog.setTitle("Add New Vaccine");

                // set the custom dialog components - text, image and button
                final EditText newVaccine =dialog.findViewById(R.id.editTextOperation);
                Button buttonAdd = dialog.findViewById(R.id.buttonAdd);

                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String vaccineName= newVaccine.getText().toString();
                        otherVaccine otherVaccine = new otherVaccine(new Date(),vaccineName);
                        animal.getOtherVaccine().add(otherVaccine);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Animals").document(animal.getiD()).set(animal);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });








    }
    public interface VaccineCallback {
        void onCallback(ArrayList <otherVaccine> otherVaccines);
    }
    public void readVaccineForUpdate(final VaccineCallback updateCallback) {
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
    public void readOperationForUpdate(final OperationCallback updateCallback) {
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
