package nfciue.nfcsmartanimalpassport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.VolumeShaper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import nfciue.utilities.LocalObject;
import nfciue.utilities.MyFileWriter;

public class InfoShownForUpdateActivity extends AppCompatActivity {


    Button buttonUpdateDb;
    String vaccineNameFromPrevActivity;
    String opNameFromPrevActivity;
    String animalIDFromPrevActivity;
    String animalIdForUpdate;
    Animal animalFromDB;// todo: bu kısım tagden doğru animal ID alındığında kaldırılıcak. ilgili yerlerde animalIdFroPRevActivity kullanılacak. Aslı Fİrebasei düzenleyecek..
    String opComment;
    MySingletonClass mySingleton = MySingletonClass.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shown_for_update);
        final Context context = this;
        opNameFromPrevActivity = getIntent().getStringExtra("OperationName");
        vaccineNameFromPrevActivity = getIntent().getStringExtra("VaccineName");
        animalIdForUpdate = getIntent().getStringExtra("AnimalID");
        opComment = getIntent().getStringExtra("opComment");
        final ProgressBar progressBar = findViewById(R.id.progress);
        final ImageView imageUpdateStatus = findViewById(R.id.imageViewUpdate);
        final TextView textViewUpdate = findViewById(R.id.textViewStatusUpdate);


        if(vaccineNameFromPrevActivity.equals("NA"))
        {

       AnimalForUpdate(new AnimalCallback() {
           @Override
           public void onCallback(Animal animal) {
               animalFromDB=animal;

               progressBar.setVisibility(View.VISIBLE);
               if(isNetworkAvailable(context))

               {
                   Operations operation = new Operations(opNameFromPrevActivity, mySingleton.getValue(), new Date(),opComment);
                   animalFromDB.getOperations().add(operation);

                   FirebaseFirestore db = FirebaseFirestore.getInstance();
                   db.collection("Animals").document(animalIdForUpdate).set(animalFromDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           progressBar.setVisibility(View.GONE);
                           imageUpdateStatus.setVisibility(View.VISIBLE);
                           textViewUpdate.setVisibility(View.VISIBLE);
                           textViewUpdate.setText("Veritabanı başarılı bir şekilde güncellendi");
                           /*AlertDialog alertDialog = new AlertDialog.Builder(InfoShownForUpdateActivity.this).create();
                           alertDialog.setTitle("Success");
                           alertDialog.setMessage("Database is updated successfully");
                           alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {

                                           dialog.dismiss();
                                       }
                                   });
                           alertDialog.show();*/
                       }
                   });
               }
               else {

                   progressBar.setVisibility(View.GONE);
                   imageUpdateStatus.setVisibility(View.VISIBLE);
                   imageUpdateStatus.setImageResource(R.drawable.error);
                   textViewUpdate.setVisibility(View.VISIBLE);
                   textViewUpdate.setText("Veritabanı güncellenemiyor. Ağ Bağlantısını kontrol edin ve tekrar deneyin");
                   LocalObject localObject = new LocalObject(vaccineNameFromPrevActivity, opNameFromPrevActivity, animalIdForUpdate, opComment);

                   Gson gson = new Gson();
                   String json = gson.toJson(localObject);
                   System.out.print(json);
                   MyFileWriter.writeLocalObjectToDevice(json + ",\n");
                   //todo: get operation/vaccine and animal id and store in local.
                   //todo: Arrange an notification. User will push that update when connectivity re established. Dont't forget to get time!
               }
           }
       });






        }
        else if(opNameFromPrevActivity.equals("NA"))
        {
            progressBar.setVisibility(View.VISIBLE);
        AnimalForUpdate(new AnimalCallback() {
            @Override
            public void onCallback(Animal animal) {
                animalFromDB=animal;

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



                if(isNetworkAvailable(context))

                {
                    Operations operation = new Operations(opNameFromPrevActivity, mySingleton.getValue() , new Date(),opComment);
                    animalFromDB.getOperations().add(operation);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Animals").document(animalFromDB.getiD()).set(animalFromDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AlertDialog alertDialog = new AlertDialog.Builder(InfoShownForUpdateActivity.this).create();

                            progressBar.setVisibility(View.GONE);
                            imageUpdateStatus.setVisibility(View.VISIBLE);
                            textViewUpdate.setText("Veritabanı başarılı bir şekilde güncelendi.");

                        }
                    });
                }
                else {

                    progressBar.setVisibility(View.GONE);
                    imageUpdateStatus.setVisibility(View.VISIBLE);
                    imageUpdateStatus.setImageResource(R.drawable.error);
                    textViewUpdate.setVisibility(View.VISIBLE);
                    textViewUpdate.setText("Veritabanı güncellenemiyor. Ağ Bağlantısını kontrol edin ve tekrar deneyin");
                    //todo: get operation/vaccine and animal id and store in local.
                    //todo: Arrange an notification. User will push that update when connectivity re established. Dont't forget to get time!
                }


            }
        });

        }
        BottomNavigationView navigationView = findViewById(R.id.navigationUpdate);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_backToMain:

                        Intent ExpListIntent = new Intent(InfoShownForUpdateActivity.this, SignedInMainActivity.class);
                        ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ExpListIntent);

                        break;
                    case R.id.action_pending_updates:
                        Toast.makeText(InfoShownForUpdateActivity.this, "Anılın ellerinden öper :D", Toast.LENGTH_SHORT).show(); //TODO: localden bilgileri çekip yeni bir activityde listeleyelim. geçişi bu ikondan sağlarız.
                        break;
                    case R.id.action_edit:
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            Intent EditIntent = new Intent(InfoShownForUpdateActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        } else {
                            Toast.makeText(InfoShownForUpdateActivity.this, "You should be logged in for this!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_read:
                        Intent ReadIntent = new Intent(InfoShownForUpdateActivity.this, ReadActivity.class);
                        ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ReadIntent);
                        break;
                }
                return true;

            }
        });

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



    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
