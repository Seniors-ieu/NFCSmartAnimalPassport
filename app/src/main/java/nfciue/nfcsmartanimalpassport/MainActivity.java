package nfciue.nfcsmartanimalpassport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button buttonWrite;
    private Button buttonRead;
    private Button buttonSignIn;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("this is main","");
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent SignedInIntent = new Intent(MainActivity.this, SignedInMainActivity.class);
            SignedInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(SignedInIntent);
        }
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.navigationMain);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_signin:

                            Intent ExpListIntent = new Intent(MainActivity.this, SignInActivity.class);
                            ExpListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ExpListIntent);

                           break;
                    case R.id.action_read:
                        Intent ReadIntent = new Intent(MainActivity.this, ReadActivity.class);
                        ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ReadIntent);
                        break;
                    case R.id.action_edit:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent EditIntent = new Intent(MainActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"You should be logged in for this!",Toast.LENGTH_SHORT).show();
                        } break;
                }
                return true;

            }});


       /* buttonWrite=findViewById(R.id.buttonWrite);
        buttonRead=findViewById(R.id.buttonRead);
        buttonSignIn=findViewById(R.id.buttonSignIn);


            buttonSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent LoginIndent = new Intent(MainActivity.this, SignInActivity.class);
                    LoginIndent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(LoginIndent);
            }

            });




            // It brings the ReadActivity.

            buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ReadIntent = new Intent(MainActivity.this, ReadActivity.class);
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
                        Intent WriteIntent = new Intent(MainActivity.this,  ChooseVaccineOperationActivity.class);
                        WriteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(WriteIntent);
                    } else {
                        // No user is signed in
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
            });*/

    }
    @Override
    public void onBackPressed() {

    }

}
