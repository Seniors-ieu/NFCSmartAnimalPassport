package nfciue.nfcsmartanimalpassport;

/**
 * Created by ASLI on 27.4.2018.
 */

class Users {
    private String nameLastname;
    private String UID;
    private String operatorID;

    public Users(String nameLastname, String UID, String opID) {
        this.nameLastname = nameLastname;
        this.UID = UID;
        this.operatorID=opID;
    }

    public String getNameLastname() {
        return nameLastname;
    }

    public String getOperatorID() {
        return operatorID;
    }
    public void setOperatorID(String a ){
        operatorID=a;
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
