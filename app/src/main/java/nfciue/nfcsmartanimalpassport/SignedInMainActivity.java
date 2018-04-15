package nfciue.nfcsmartanimalpassport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedInMainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button buttonWrite;
    private Button buttonRead;
    private Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_signed_in_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent NotSignedIntent = new Intent(SignedInMainActivity.this, MainActivity.class);
            NotSignedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(NotSignedIntent);
        }
        else
        {

            getSupportActionBar().setTitle("Welcome, "+user.getEmail());
        }






        buttonWrite=findViewById(R.id.buttonWrite);
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
                    // User is signed in
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

    }
    @Override
    public void onBackPressed() {

    }

}
