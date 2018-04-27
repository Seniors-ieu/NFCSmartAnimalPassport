package nfciue.nfcsmartanimalpassport;

/**
 * Created by ASLI on 27.4.2018.
 */

class Users {
    private String nameLastname;
    private String UID;

    public Users(String nameLastname, String UID) {
        this.nameLastname = nameLastname;
        this.UID = UID;
    }

    public String getNameLastname() {
        return nameLastname;
    }

    public String getUID() {
        return UID;
    }

    public void setNameLastname(String nameLastname) {
        this.nameLastname = nameLastname;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    public Users(){}
}
