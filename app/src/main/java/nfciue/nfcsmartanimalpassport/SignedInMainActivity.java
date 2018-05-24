package nfciue.nfcsmartanimalpassport;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
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

import java.lang.reflect.Field;
import java.util.Date;

import nfciue.utilities.Decoderiue;
import nfciue.utilities.MyFileReader;
import nfciue.utilities.MyFileUpdater;

public class SignedInMainActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    BottomNavigationView navigationView;
    String operatorID;
    MySingletonClass mySingleton = MySingletonClass.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in_main);
        final Context context = this;
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
                    operatorID  = usr.getOperatorID();
                    mySingleton.setValue(operatorID);
                    TextView textView = findViewById(R.id.textViewWelcome);
                    textView.setText("Hoşgeldiniz  "+ usr.getNameLastname());
                }
            });
            //getSupportActionBar().setTitle("Welcome, " + user.getEmail());
        }


        navigationView=findViewById(R.id.navigationSignedInMain);
        disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_signout:
                        //This asks user if he/she wants to send update to firebase or not.
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle("Çıkış Yap")
                                .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        AlertDialog alertDialog = new AlertDialog.Builder(SignedInMainActivity.this).create();
                                        alertDialog.setTitle("Bilgi");
                                        alertDialog.setMessage("Başarıyla çıkış yaptınız.");
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
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_menu_info_details);
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        break;
                    case R.id.action_read:
                        Intent ReadIntent = new Intent(SignedInMainActivity.this, ReadActivity.class);
                        ReadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ReadIntent);
                        break;
                    case R.id.action_last_read_tag:
                        try{
                            String dataFromLastReadTag = MyFileReader.readLastReadTagData();
                            if(!dataFromLastReadTag.equals("")) {
                                Intent PartialInfoShownIntent = new Intent(SignedInMainActivity.this, PartialInfoShownActivity.class);
                                PartialInfoShownIntent.putExtra("dataFromReadActivityIntent", dataFromLastReadTag);
                                PartialInfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(PartialInfoShownIntent);
                            } else {
                                Toast.makeText(SignedInMainActivity.this, "En son okunmuş küpe bilgisi yok!", Toast.LENGTH_LONG).show();
                            }
                        } catch(Exception e) {
                            Toast.makeText(SignedInMainActivity.this, "En son okunmuş küpe bilgisi yok!", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.action_edit:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent EditIntent = new Intent(SignedInMainActivity.this, ChooseVaccineOperationActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        }
                        else{
                            Toast.makeText(SignedInMainActivity.this,"Önce veteriner girişi yapmalısınız!",Toast.LENGTH_SHORT).show();
                        } break;
                    case R.id.action_updates:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Intent EditIntent = new Intent(SignedInMainActivity.this, NotSentUpdatesActivity.class);
                            EditIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(EditIntent);
                        }
                        else{
                            Toast.makeText(SignedInMainActivity.this,"Önce veteriner girişi yapmalısınız!",Toast.LENGTH_SHORT).show();
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

    // Method for disabling ShiftMode of BottomNavigationView
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}
