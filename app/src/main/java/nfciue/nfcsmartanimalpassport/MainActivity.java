package nfciue.nfcsmartanimalpassport;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;

import nfciue.utilities.MyFileReader;
import nfciue.utilities.MyFileUpdater;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent SignedInIntent = new Intent(MainActivity.this, SignedInMainActivity.class);
            SignedInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(SignedInIntent);
        }
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.navigationMain);
        disableShiftMode(navigationView);
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
                    case R.id.action_last_read_tag:
                        String dataFromLastReadTag = MyFileReader.readLastReadTagData();
                        if(!dataFromLastReadTag.equals("")) {
                            Intent PartialInfoShownIntent = new Intent(MainActivity.this, PartialInfoShownActivity.class);
                            PartialInfoShownIntent.putExtra("dataFromReadActivityIntent", dataFromLastReadTag);
                            PartialInfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(PartialInfoShownIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "En son okunmuş küpe bilgisi yok!", Toast.LENGTH_LONG).show();
                        }
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




    }
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("EXIT", "Application is being closed...");
        MyFileUpdater.removeLastReadTagData();
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
