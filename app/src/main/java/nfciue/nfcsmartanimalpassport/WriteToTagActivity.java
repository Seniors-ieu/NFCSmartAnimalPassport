package nfciue.nfcsmartanimalpassport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.ndef.INdefMessage;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.ntag.INTag;
import com.nxp.nfclib.ntag.NTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class WriteToTagActivity extends AppCompatActivity {
    final Context context = this;

    byte[] myPass;
    String textToNFC;

    // The license key, got from TapLinx.
    private String m_strKey = "a8ecf9cd118701724d7a24c76c9495f0";

    // The TapLinx library instance.
    private NxpNfcLib m_libInstance = null;

    // Initialize the TapLinx library.
    private void initializeLibrary() {
        m_libInstance = NxpNfcLib.getInstance();
        m_libInstance.registerActivity(this, m_strKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_tag);

        initializeLibrary();    // Initialize library.


        myPass = getIntent().getByteArrayExtra("myPass");
        textToNFC = getIntent().getStringExtra("textToNFC");
    }

    // Called if app becomes active.
    @Override
    protected void onResume() {
        m_libInstance.startForeGroundDispatch();
        super.onResume();
    }

    // Called if app becomes inactive.
    @Override
    protected void onPause() {
        m_libInstance.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("ULAK", "onNewIntent");
        cardLogic(intent);
        super.onNewIntent(intent);
    }

    private void cardLogic(final Intent intent) {
        CardType type = CardType.UnknownCard;
        try {
            type = m_libInstance.getCardType(intent);
            Log.d("ULAK", "Card type found: " + type.getTagName());
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        switch (type) {
            case NTag216:
                try {
                    ntagCardLogic(NTagFactory.getInstance().getNTAG216(m_libInstance.getCustomModules()), textToNFC, myPass);
                } catch (Throwable t) {
                    Log.e("ULAK", "Error in switch(NTag216) " + t.getMessage());
                }
                break;
            default:
                Log.e("ULAK", "An error occured when getting card logic.");
                break;
        }
    }

    private void ntagCardLogic(final INTag tag, String dataTextToNFC, byte[] pass) {
        Log.e("ULAK", "ntagCardLogic'in içi");


        try {
            Log.e("ULAK", "ntagCardLogic11111");
            tag.getReader().connect();
            NTag213215216 ntag216 = (NTag213215216) tag;

            //byte[] myPassword = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4};
            byte[] myPassword = pass;
            byte[] myAck = new byte[] {(byte) 0x00, (byte) 0x00};
            byte[] defPass = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};


            String ndefMessage = dataTextToNFC;

            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = ndefMessage.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            NdefMessage ndefMessage1 = new NdefMessage(new NdefRecord[]{record});

            NdefRecordWrapper nrw = new NdefRecordWrapper(record);
            NdefMessageWrapper nmw = new NdefMessageWrapper(nrw);
            INdefMessage nm = nmw;

            ntag216.authenticatePwd(myPassword, myAck);
            ntag216.writeNDEF(nm);



            Toast.makeText(this, "OK from WTTA!!!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("ULAK", "Error in ntagCardLogic: Password is wrong... " + e.getMessage());
        } finally {
            Log.e("ULAK", "finally'nin içi.");
            tag.getReader().close();
            //NTag213215216 tag1 = (NTag213215216) tag;  //ULAK buradan devam edebilirsinnn.

        }
    }

}
