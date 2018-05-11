package nfciue.nfcsmartanimalpassport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditOperationActivity extends AppCompatActivity {
    Spinner spinnerOps ;
    EditText editTextOpComments ;
    Animal animalFromDB;
    ArrayList<Operations> operations;
    int selectedIndexOperation;
    int newOpType;
    String opName = "not available /diğer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);
        final Context context =this;

        animalFromDB = (Animal) getIntent().getSerializableExtra("animalFromDB");
        operations = animalFromDB.getOperations();
        selectedIndexOperation = getIntent().getIntExtra("indexOfSelectedOp",-1);

        spinnerOps= findViewById(R.id.spinnerOps);
        editTextOpComments = findViewById(R.id.editTextOpComments);
        editTextOpComments.setText(animalFromDB.getOperations().get(selectedIndexOperation).getComment());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.operations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOps.setAdapter(adapter);
        spinnerOps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){
                    case 0 : opName = "Kan Analizi"; break;
                    case 1 : opName = "Pansuman"; break;
                    case 2 : opName = "Medikal Muayene"; break;
                    case 3 : opName = "Fiziksel Muayene"; break;
                    case 4 : opName = "Medikal Operasyon"; break;
                    case 5 : opName = "Doğum"; break;
                }
                animalFromDB.getOperations().get(selectedIndexOperation).setOperationType(opName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context,"Bir seçim yapmalısınız",Toast.LENGTH_SHORT).show();
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

                    case R.id.action_approve:
                        animalFromDB.getOperations().get(selectedIndexOperation).setComment(editTextOpComments.getText().toString());
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Animals").document(animalFromDB.getiD()).set(animalFromDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    AlertDialog alertDialog = new AlertDialog.Builder(EditOperationActivity.this).create();
                                    alertDialog.setTitle("Güncelleme Başarılı");
                                    alertDialog.setMessage("Operasyon bilgileri güncellendi");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tamam",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                    Intent EditIntent = new Intent(EditOperationActivity.this, previousHealthInfoActivity.class);
                                                    EditIntent.putExtra("AnimalID",animalFromDB.getiD());
                                                    EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(EditIntent);
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });
                        } else {
                            Toast.makeText(EditOperationActivity.this, "You should be logged in for this!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
                return true;

            }
        });

    }
}
