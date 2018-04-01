package nfciue.nfcsmartanimalpassport;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ChooseVaccineOperationActivity extends AppCompatActivity {

    Button buttonVaccine;
    Button buttonOperation;
    int operationCode ;
    int psw1;
    int psw2;
    int psw3;
    int psw4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vaccine_operation);
        final Context context = this;

        buttonVaccine = findViewById(R.id.buttonVaccine);
        buttonOperation = findViewById(R.id.buttonOp);

        buttonVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //todo operation için yapılan dialog boxun benzeri buraya yapılacak :VACCINE ADD
            }
        });

        buttonOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_operation);
                dialog.setTitle("Please Choose Operation Type");

                final EditText editTextPsw1 = dialog.findViewById(R.id.editTextPsw1);
                final EditText editTextPsw2 = dialog.findViewById(R.id.editTextPsw2);
                final EditText editTextPsw3 = dialog.findViewById(R.id.editTextPsw3);
                final EditText editTextPsw4 = dialog.findViewById(R.id.editTextPsw4);
                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerOperation);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.operations_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        operationCode = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here , todo: show alert dialog : you must choose a type!!
                    }

                });

                Button buttonOk = dialog.findViewById(R.id.buttonOk);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        psw1 = Integer.parseInt(editTextPsw1.getText().toString());
                        psw2 = Integer.parseInt(editTextPsw2.getText().toString());
                        psw3 = Integer.parseInt(editTextPsw3.getText().toString());
                        psw4 = Integer.parseInt(editTextPsw4.getText().toString());
                        byte[] password = new byte[] {(byte)psw1, (byte)psw2 , (byte)psw3 , (byte)psw4};

                        Intent OperationIntent = new Intent(ChooseVaccineOperationActivity.this, WriteToTagActivity.class);
                        OperationIntent.putExtra("password",password);
                        OperationIntent.putExtra("operationCode",operationCode);
                        OperationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(OperationIntent);

                    }
                });
                dialog.show();


            }
        });
    }


}
