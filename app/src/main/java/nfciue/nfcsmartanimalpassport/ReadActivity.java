package nfciue.nfcsmartanimalpassport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nxp.nfclib.NxpNfcLib;

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
}
