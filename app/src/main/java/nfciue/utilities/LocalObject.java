package nfciue.utilities;

/**
 * Created by anil on 3.05.2018.
 */

public class LocalObject {
    private String vaccineNameFromPrevActivity;
    private String opNameFromPrevActivity;
    private String animalIdForUpdate;
    private String opComment;

    public LocalObject(String vaccineNameFromPrevActivity, String opNameFromPrevActivity, String animalIdForUpdate, String opComment) {
        this.vaccineNameFromPrevActivity = vaccineNameFromPrevActivity;
        this.opNameFromPrevActivity = opNameFromPrevActivity;
        this.animalIdForUpdate = animalIdForUpdate;
        this.opComment = opComment;
    }
}
