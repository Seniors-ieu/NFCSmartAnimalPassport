package nfciue.nfcsmartanimalpassport;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.ndef.INdefMessage;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.ntag.INTAGI2Cplus;
import com.nxp.nfclib.ntag.INTag;
import com.nxp.nfclib.ntag.INTagI2C;
import com.nxp.nfclib.ntag.NTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.utils.Utilities;

public class ReadActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_read);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);   // set the duration of splash screen
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                } finally {

                }
            }
        };
        timer.start();

        initializeLibrary();    // Initialize library.
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
                    ntagCardLogic(NTagFactory.getInstance().getNTAG216(m_libInstance.getCustomModules()));
                } catch (Throwable t) {
                    Log.e("ULAK", "Error in switch(NTag216) " + t.getMessage());
                }
                break;
            default:
                Log.e("ULAK", "An error occured when getting card logic.");
                break;
        }
    }

    private void ntagCardLogic(final INTag tag) {
        Log.e("ULAK", "ntagCardLogic'in içi");


        try {
            Log.e("ULAK", "ntagCardLogic11111");
            tag.getReader().connect();
            NTag213215216 ntag216 = (NTag213215216) tag;
//            byte[] dataFromTag = ntag216.read(4);
//            String dft = "AA";
//            for(int i = 0; i < dataFromTag.length; i++) {
//                dft = dft + (char)dataFromTag[i];
//            }
            byte[] myPassword = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4};
            byte[] myAck = new byte[] {(byte) 0x00, (byte) 0x00};
            byte[] defPass = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};



            INdefMessage nm = ntag216.readNDEF();

            NdefMessageWrapper nmw = new NdefMessageWrapper(nm.toByteArray());
            String dataFromNFCTag = new String(nmw.getRecords()[0].getPayload());

            Intent PartialInfoShownIntent = new Intent(ReadActivity.this, PartialInfoShownActivity.class);
            PartialInfoShownIntent.putExtra("dataFromReadActivityIntent", dataFromNFCTag);
            PartialInfoShownIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(PartialInfoShownIntent);

        } catch (Exception e) {
            Log.e("ULAK", "Error in ntagCardLogic: " + e.getMessage());
        } finally {
            Log.e("ULAK", "finally'nin içi.");
            tag.getReader().close();


        }
    }
}
