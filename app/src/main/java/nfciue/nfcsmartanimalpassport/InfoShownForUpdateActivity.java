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
    String animalIdForUpdate;
    Animal animalFromDB;// todo: bu kısım tagden doğru animal ID alındığında kaldırılıcak. ilgili yerlerde animalIdFroPRevActivity kullanılacak. Aslı Fİrebasei düzenleyecek..




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_for_update);
        final Context context = this;
        opNameFromPrevActivity = getIntent().getStringExtra("OperationName");
        vaccineNameFromPrevActivity = getIntent().getStringExtra("VaccineName");
        animalIdForUpdate = getIntent().getStringExtra("AnimalID");


        if(vaccineNameFromPrevActivity.equals("NA"))
        {

       AnimalForUpdate(new AnimalCallback() {
           @Override
           public void onCallback(Animal animal) {
               animalFromDB=animal;
           }
       });


        buttonUpdateDb = findViewById(R.id.buttonUpdateDb);


        buttonUpdateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                Operations operation = new Operations(opNameFromPrevActivity, "1111111", new Date());
                animalFromDB.getOperations().add(operation);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Animals").document(animalIdForUpdate).set(animalFromDB);
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
        AnimalForUpdate(new AnimalCallback() {
            @Override
            public void onCallback(Animal animal) {
                animalFromDB=animal;
            }
        });
        buttonUpdateDb = findViewById(R.id.buttonUpdateDb);


        buttonUpdateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(vaccineNameFromPrevActivity.equals("Theileria") || vaccineNameFromPrevActivity.equals("Escherichia Coli")|| vaccineNameFromPrevActivity.equals("Mantar") )
                {
                    otherVaccine otherVaccine = new otherVaccine(new Date(),vaccineNameFromPrevActivity);
                    animalFromDB.getOtherVaccine().add(otherVaccine);
                }
                else if(vaccineNameFromPrevActivity.equals("Brusella"))
                {

                    animalFromDB.setBrusellosisVaccine(true);
                }
                else if(vaccineNameFromPrevActivity.equals("Pasteurella"))
                {

                    animalFromDB.setPasturellaVaccine(true);
                }
                else if(vaccineNameFromPrevActivity.equals("Alum"))
                {

                    animalFromDB.setAlumVaccine(true);
                }


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Animals").document(animalFromDB.getiD()).set(animalFromDB);
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



    public interface AnimalCallback {
        void onCallback(Animal animal);
    }
    public void AnimalForUpdate(final AnimalCallback updateCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Animals").document(animalIdForUpdate);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Animal animal= documentSnapshot.toObject(Animal.class);
                updateCallback.onCallback(animal);

            }
        });
    }

}
