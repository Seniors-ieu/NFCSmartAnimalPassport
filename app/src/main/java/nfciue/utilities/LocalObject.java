package nfciue.utilities;

/**
 * Created by anil on 3.05.2018.
 */

public class LocalObject {
    private String vaccineNameFromPrevActivity;
    private String opNameFromPrevActivity;
    private String animalIdForUpdate;
    private String opComment;
    private String date;

    public LocalObject(String vaccineNameFromPrevActivity, String opNameFromPrevActivity, String animalIdForUpdate, String opComment) {
        this.vaccineNameFromPrevActivity = vaccineNameFromPrevActivity;
        this.opNameFromPrevActivity = opNameFromPrevActivity;
        this.animalIdForUpdate = animalIdForUpdate;
        this.opComment = opComment;
        // Get current time.
        long unixTime = System.currentTimeMillis() / 1000L;
        date = Decoderiue.unixToDate(unixTime);
    }

    public String getDate() {
        return date;
    }

    public String getOpComment() {
        return opComment;
    }

    public String getVaccineNameFromPrevActivity() {
        return vaccineNameFromPrevActivity;
    }

    public String getOpNameFromPrevActivity() {
        return opNameFromPrevActivity;
    }

    public String getAnimalIdForUpdate() {
        return animalIdForUpdate;
    }
}
