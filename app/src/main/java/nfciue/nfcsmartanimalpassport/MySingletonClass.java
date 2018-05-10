package nfciue.nfcsmartanimalpassport;

/**
 * Created by ASLI on 10.5.2018.
 */

public class MySingletonClass {
    private static final MySingletonClass ourInstance = new MySingletonClass();

    public static MySingletonClass getInstance() {
        return ourInstance;
    }

    private MySingletonClass() {
    }
    private String val;

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}
