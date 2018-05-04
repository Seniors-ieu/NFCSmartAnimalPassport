package nfciue.nfcsmartanimalpassport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignedInMainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent NotSignedIntent = new Intent(SignedInMainActivity.this, MainActivity.class);
            NotSignedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(NotSignedIntent);
        } else {
            Log.e("oncreatesignedinmain", user.getUid());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users usr = documentSnapshot.toObject(Users.class);
                    TextView textView = findViewById(R.id.textViewWelcome);
                    textView.setText("Hoşgeldiniz  "+ usr.getNameLastname());
                }
            });
            //getSupportActionBar().setTitle("Welcome, " + user.getEmail());
        }


        navigationView=findViewById(R.id.navigationSignedInMain);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_signout:

                        FirebaseAuth.getInstance().signOut();
                        AlertDialog alertDialog = new AlertDialog.Builder(SignedInMainActivity.this).create();
                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("You signed out");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                        Intent MainIndent = new Intent(SignedInMainActivity.this, MainActivity.class);
                        MainIndent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(MainIndent);

                        break;
                    case R.id.action_read:
                        Intent ReadIntent = new Intent(SignedInMainActivity.this, ReadActivity.class);
                        ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ReadIntent);
                        break;
                    case R.id.action_edit:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent EditIntent = new Intent(SignedInMainActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        }
                        else{
                            Toast.makeText(SignedInMainActivity.this,"You should be logged in for this!",Toast.LENGTH_SHORT).show();
                        } break;
                }
                return true;

            }});



      /*  buttonWrite=findViewById(R.id.buttonWrite);
        buttonRead=findViewById(R.id.buttonRead);
        buttonSignOut=findViewById(R.id.buttonSignOut);


        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                AlertDialog alertDialog = new AlertDialog.Builder(SignedInMainActivity.this).create();
                alertDialog.setTitle("Info");
                alertDialog.setMessage("You signed out");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Intent MainIndent = new Intent(SignedInMainActivity.this, MainActivity.class);
                MainIndent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(MainIndent);
            }

        });




        // It brings the ReadActivity.

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReadIntent = new Intent(SignedInMainActivity.this, ReadActivity.class);
                ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ReadIntent);
            }
        });




        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // Users is signed in
                    Intent WriteIntent = new Intent(SignedInMainActivity.this,  ChooseVaccineOperationActivity.class);
                    WriteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(WriteIntent);
                } else {
                    // No user is signed in
                    AlertDialog alertDialog = new AlertDialog.Builder(SignedInMainActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("You are not authorized to write tag!!!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });

    */
    }
    @Override
    public void onBackPressed() {

    }

}
