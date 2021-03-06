package nfciue.nfcsmartanimalpassport;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import nfciue.utilities.Decoderiue;
import nfciue.utilities.LocalObject;
import nfciue.utilities.MyFileReader;
import nfciue.utilities.MyFileUpdater;

public class NotSentUpdatesActivity extends AppCompatActivity {
    BottomNavigationView navigationViewNotSentUpdates;
    Animal animalFromDB;
    MySingletonClass mySingleton = MySingletonClass.getInstance();
    TextView noUpdateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_sent_updates);
        final Context context = this;
        noUpdateTextView = (TextView) findViewById(R.id.noUpdateTextView);

        try {
            final LocalObject[] notSentUpdateObjects = MyFileReader.readNotSentUpdates();
            final int numberOfNotSentUpdates = notSentUpdateObjects.length;
            String[] listElements = new String[numberOfNotSentUpdates];

            if(numberOfNotSentUpdates == 0) {
                noUpdateTextView.setVisibility(View.VISIBLE);
            }

            for(int i = 0; i < numberOfNotSentUpdates; i++) {
                String listElement = "Hayvan Pasaport Numarası: " + notSentUpdateObjects[i].getAnimalIdForUpdate()
                        + "\nTarih: " + notSentUpdateObjects[i].getDate() + "\nOperasyon Notu: " + notSentUpdateObjects[i].getOpComment()
                        + "\nOperasyon: " + notSentUpdateObjects[i].getOpNameFromPrevActivity() + "\nAşı adı: " + notSentUpdateObjects[i].getVaccineNameFromPrevActivity();
                listElements[i] = listElement;
            }
            ListAdapter notSentUpdatesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listElements);
            ListView notSentUpdatesListView = (ListView) findViewById(R.id.notSentUpdatesList);
            notSentUpdatesListView.setAdapter(notSentUpdatesListAdapter);

            notSentUpdatesListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            final int myPosition = position;
                            //This asks user if he/she wants to send update to firebase or not.
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Sunucuya gönder")
                                    .setMessage("Bu güncellemeyi sunucuya göndermek istediğinize emin misiniz?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            final String notSentUpdatesAnimalID = notSentUpdateObjects[myPosition].getAnimalIdForUpdate();
                                            final Date notSentUpdatesDate = Decoderiue.getDateFromDateString(notSentUpdateObjects[myPosition].getDate());
                                            final String notSentUpdatesOpComment = notSentUpdateObjects[myPosition].getOpComment();
                                            final String notSentUpdatesOpType = notSentUpdateObjects[myPosition].getOpNameFromPrevActivity();
                                            final String notSentUpdatesVacType = notSentUpdateObjects[myPosition].getVaccineNameFromPrevActivity();
                                            final int linePosition = myPosition;
                                            Toast.makeText(NotSentUpdatesActivity.this, "Gönderiliyor, Lütfen Bekleyiniz.", Toast.LENGTH_LONG).show();

                                            if(isNetworkAvailable(context)) {
                                                if(FirebaseAuth.getInstance().getCurrentUser()!=null){

                                                    AnimalForUpdate(new InfoShownForUpdateActivity.AnimalCallback() {
                                                        @Override
                                                        public void onCallback(Animal animal) {
                                                            animalFromDB=animal;

                                                            if(isNetworkAvailable(context)) {
                                                                if(!notSentUpdatesOpType.equals("NA")) {
                                                                    Operations notSentOperations = new Operations(notSentUpdatesOpType, mySingleton.getValue(), notSentUpdatesDate, notSentUpdatesOpComment);
                                                                    animalFromDB.getOperations().add(notSentOperations);

                                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                    db.collection("Animals").document(animalFromDB.getiD()).set(animalFromDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            try {
                                                                                MyFileUpdater.removeLine(linePosition);
                                                                                Toast.makeText(NotSentUpdatesActivity.this, "BAŞARILI", Toast.LENGTH_LONG).show();
                                                                                //This will restart activity and refresh the list.
                                                                                recreate();
                                                                            } catch(Exception e) {
                                                                                Toast.makeText(NotSentUpdatesActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    if(!notSentUpdatesVacType.equals("NA")) {
                                                                        Vaccine Vaccine = new Vaccine(notSentUpdatesDate, notSentUpdatesVacType);
                                                                        animalFromDB.getVaccines().add(Vaccine);

                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                        db.collection("Animals").document(animalFromDB.getiD()).set(animalFromDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                try {
                                                                                    MyFileUpdater.removeLine(linePosition);
                                                                                    Toast.makeText(NotSentUpdatesActivity.this, "BAŞARILI", Toast.LENGTH_LONG).show();
                                                                                    //This will restart activity and refresh the list.
                                                                                    recreate();
                                                                                } catch(Exception e) {
                                                                                    Toast.makeText(NotSentUpdatesActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(NotSentUpdatesActivity.this, "İnternet yok, gönderilemedi. ", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }, notSentUpdatesAnimalID);
                                                }
                                                else{
                                                    Toast.makeText(NotSentUpdatesActivity.this, "No authentication. ", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(NotSentUpdatesActivity.this, "İnternet yok, callback hiç çalışmadı.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_menu_send);
                            AlertDialog dialog = builder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        }
                    }
            );
        } catch (Exception e) {
            noUpdateTextView.setVisibility(View.VISIBLE);
            Toast.makeText(NotSentUpdatesActivity.this, "Gönderilmemiş güncelleme yok.", Toast.LENGTH_LONG).show();
        }


        navigationViewNotSentUpdates=findViewById(R.id.navigationNotSentUpdates);
        navigationViewNotSentUpdates.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_back_from_not_sent_update:

                        Intent SignedInMainActivity = new Intent(NotSentUpdatesActivity.this, SignedInMainActivity.class);
                        SignedInMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(SignedInMainActivity);

                        break;

                    case R.id.action_read_tag:
                        Intent ReadIntent = new Intent(NotSentUpdatesActivity.this, ReadActivity.class);
                        ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ReadIntent);
                        break;

                    case R.id.action_update_tag:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent EditIntent = new Intent(NotSentUpdatesActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        } else {
                            Toast.makeText(NotSentUpdatesActivity.this, "No authentication. ", Toast.LENGTH_LONG).show();
                        }
                }
                return true;
            }});
    }

    public interface AnimalCallback {
        void onCallback(Animal animal);
    }
    public void AnimalForUpdate(final InfoShownForUpdateActivity.AnimalCallback updateCallback, String animalID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Animals").document(animalID);
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

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // drop NFC events
        }
    }
}
