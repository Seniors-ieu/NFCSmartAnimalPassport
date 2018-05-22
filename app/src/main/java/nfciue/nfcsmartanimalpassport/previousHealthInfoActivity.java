package nfciue.nfcsmartanimalpassport;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class previousHealthInfoActivity extends AppCompatActivity {
    private String animalID;
    private Animal animalFromDB ;
    private ArrayList<String> groups;
    private HashMap<String,ArrayList<String>> items;
    private ArrayList<ArrayList<String>> data;
    ArrayList<Operations> operations;
    ArrayList<Vaccine> Vaccines;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_health_info);
        animalID = getIntent().getStringExtra("AnimalID");
        final ExpandableListView expListView = (ExpandableListView) findViewById(R.id.exp_list_previous_health_info);
        final Context context =this;

        AnimalForUpdate(new AnimalCallback(){
            @Override
            public void onCallback(Animal animal) {
                animalFromDB=animal;
                operations = animalFromDB.getOperations();
                Vaccines = animalFromDB.getVaccines();

                groups = new ArrayList<String>();
                groups.add("Geçmiş Aşı Bilgisi");
                groups.add("Geçmiş Operasyon Bilgisi");

                ArrayList<String> vaccineNames = new ArrayList<String>();
                for(int i = 0; i< Vaccines.size(); i++){
                    vaccineNames.add(Vaccines.get(i).getVaccineName());
                }
                ArrayList<String> operationNames = new ArrayList<String>();
                for(int i=0; i<operations.size();i++){
                    operationNames.add(operations.get(i).getOperationType()+"\n Operator:" + operations.get(i).getOperatorId());
                }

                items = new HashMap<String, ArrayList<String>>();
                items.put(groups.get(0), vaccineNames);
                items.put(groups.get(1), operationNames);


                ArrayList<String> vaccineDates = new ArrayList<String>();
                for(int i = 0; i< Vaccines.size(); i++){
                    vaccineDates.add(Vaccines.get(i).getVaccineDate().toString());
                }

                ArrayList<String> operationDateComment= new ArrayList<String>();
                for(int i=0; i<operations.size();i++){
                    operationDateComment.add(operations.get(i).getOperationDate().toString()+"\n Hekim Notları:" + operations.get(i).getComment());
                }

                data = new ArrayList<ArrayList<String>>();
                data.add(vaccineDates);
                data.add(operationDateComment);

                ExpandableListAdapter adapter = new ExpListAdapter(context, groups, items,data);
                expListView.setAdapter(adapter);
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                        if(i==1){
                            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                                if(operations.get(i1).getOperatorId().equals(MySingletonClass.getInstance().getValue())){  //user is only  authorized to edit on his own commit
                                    Toast.makeText(context,"can edit",Toast.LENGTH_SHORT).show();
                                    Intent EditIntent = new Intent(previousHealthInfoActivity.this, EditOperationActivity.class);
                                    EditIntent.putExtra("animalFromDB",animalFromDB);
                                    EditIntent.putExtra("SelectedOp",operations.get(i1));
                                    EditIntent.putExtra("indexOfSelectedOp",i1);
                                    Log.e("previoActivity selected",String.valueOf(i1));
                                    EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(EditIntent);
                                    //get input
                                    //update animalfromdb
                                    //send animalfromdb to firebase
                                }
                                else{
                                    Toast.makeText(context,"Başkasına ait bir operasyonu düzenleyemezsiniz.",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(previousHealthInfoActivity.this,"Bunun için kullanıcı girişi yapmış olmanız gerek.",Toast.LENGTH_SHORT).show();
                            }

                        }

                        return true;
                    }
                });


            }
        });


        BottomNavigationView navigationView = findViewById(R.id.navigationHealthInfo);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_back:

                       finish();

                        break;
                    case R.id.action_pending_updates:
                        Toast.makeText(previousHealthInfoActivity.this, "Anılın ellerinden öper :D", Toast.LENGTH_SHORT).show(); //TODO: localden bilgileri çekip yeni bir activityde listeleyelim. geçişi bu ikondan sağlarız.
                        break;
                    case R.id.action_edit:
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            Intent EditIntent = new Intent(previousHealthInfoActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        } else {
                            Toast.makeText(previousHealthInfoActivity.this, "You should be logged in for this!", Toast.LENGTH_SHORT).show();
                        }
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
        DocumentReference ref = db.collection("Animals").document(animalID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Animal animal= documentSnapshot.toObject(Animal.class);
                updateCallback.onCallback(animal);

            }
        });
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
