package nfciue.nfcsmartanimalpassport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.VolumeShaper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class InfoShownForUpdateActivity extends AppCompatActivity {


    Button buttonUpdateDb;
    String vaccineNameFromPrevActivity;
    String opNameFromPrevActivity;
    String animalIDFromPrevActivity;
    String animalIdForUpdate="TR 35 123"; // todo: bu kısım tagden doğru animal ID alındığında kaldırılıcak. ilgili yerlerde animalIdFroPRevActivity kullanılacak. Aslı Fİrebasei düzenleyecek..




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_for_update);
        final Context context = this;
        opNameFromPrevActivity = getIntent().getStringExtra("OperationName");
        vaccineNameFromPrevActivity = getIntent().getStringExtra("VaccineName");
        animalIDFromPrevActivity = getIntent().getStringExtra("AnimalID");


        if(vaccineNameFromPrevActivity.equals("NA"))
        {

        final Animal animal = new Animal();    //burada veritabanından tam okuma yapılabilir, internet olmadıkça bu aktivitynn çalışması zaten mümkün değil.
        animal.setAlumVaccine(true);
        animal.setBirthdate("16.05.2010");
        animal.setBirthFarmNo("55567533");
        animal.setBreed("Holstein");
        animal.setBrusellosisVaccine(true);
        animal.setCurrentFarmNo("1234567");
        animal.setDeathDate("NA");
        animal.setDeathPlace("NA");
        animal.seteSignDirector("14567SFGHJK4567");
        animal.seteSignOwner("1235WRTY357");
        animal.setExportCountryCode(0);
        animal.setExportDate("NA");
        animal.setFarmChangeDate("NA");
        animal.setiD("TR 35 123");
        animal.setFemale(true);
        animal.setMotherId("TR 35 122");


        readVaccineForUpdate(new VaccineCallback() { //All vaccines will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback(ArrayList<otherVaccine> otherVaccines) {     //otherVaccines veritabanından gelen aşılar
                animal.setOtherVaccine(otherVaccines);
            }
        });


        readOperationForUpdate(new OperationCallback() { //All operations will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback(ArrayList<Operations> operations) {     //otherVaccines veritabanından gelen aşılar
                animal.setOperations(operations);
            }
        });

        animal.setOwnerTc("11111111111");
        animal.setPasturellaVaccine(true);// with the information that is retrieved from Tag, initialize animal with parameters. All fields should match with db , othervise use call back to retrieve whole object from db.. (aslı)
        //Owner info won't be updated by user for now, if this changes, initialize an owner also.


        buttonUpdateDb = findViewById(R.id.buttonUpdateDb);


        buttonUpdateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                Operations operation = new Operations(opNameFromPrevActivity, "1111111", new Date());
                animal.getOperations().add(operation);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Animals").document(animal.getiD()).set(animal);
                AlertDialog alertDialog = new AlertDialog.Builder(InfoShownForUpdateActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Database is updated successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


            }
        });


    }
    else if(opNameFromPrevActivity.equals("NA"))
    {
        final Animal animal = new Animal();    //burada veritabanından tam okuma yapılabilir, internet olmadıkça bu aktivitynn çalışması zaten mümkün değil.

        animal.setBirthdate("16.05.2010");
        animal.setBirthFarmNo("55567533");
        animal.setBreed("Holstein");

        animal.setCurrentFarmNo("1234567");
        animal.setDeathDate("NA");
        animal.setDeathPlace("NA");
        animal.seteSignDirector("14567SFGHJK4567");
        animal.seteSignOwner("1235WRTY357");
        animal.setExportCountryCode(0);
        animal.setExportDate("NA");
        animal.setFarmChangeDate("NA");
        animal.setiD("TR 35 123");
        animal.setFemale(false);
        animal.setMotherId("TR 35 122");


        readVaccineForUpdate(new VaccineCallback() { //All vaccines will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback(ArrayList<otherVaccine> otherVaccines) {     //otherVaccines veritabanından gelen aşılar
                animal.setOtherVaccine(otherVaccines);
            }
        });


        readOperationForUpdate(new OperationCallback() { //All operations will be retrieved from db, tg only have the last one...
            @Override
            public void onCallback(ArrayList<Operations> operations) {     //otherVaccines veritabanından gelen aşılar
                animal.setOperations(operations);
            }
        });

        animal.setOwnerTc("11111111111");
        animal.setAlumVaccine(true);
        animal.setBrusellosisVaccine(true);
        animal.setPasturellaVaccine(true);// with the information that is retrieved from Tag, initialize animal with parameters. All fields should match with db , othervise use call back to retrieve whole object from db.. (aslı)
        //Owner info won't be updated by user for now, if this changes, initialize an owner also.


        buttonUpdateDb = findViewById(R.id.buttonUpdateDb);


        buttonUpdateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                otherVaccine otherVaccine = new otherVaccine(new Date(),vaccineNameFromPrevActivity);
                animal.getOtherVaccine().add(otherVaccine);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Animals").document(animal.getiD()).set(animal);
                AlertDialog alertDialog = new AlertDialog.Builder(InfoShownForUpdateActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Database is updated successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


            }
        });

    }

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
