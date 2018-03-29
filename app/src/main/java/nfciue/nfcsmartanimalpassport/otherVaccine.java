package nfciue.nfcsmartanimalpassport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ASLI on 28.3.2018.
 */

public class otherVaccine implements Serializable{
    private Date vaccineDate;
    private String vaccineName;

    public otherVaccine(Date vaccineDate, String vaccineName) {
        this.vaccineDate = vaccineDate;
        this.vaccineName = vaccineName;
    }

    public otherVaccine() {
    }

    public Date getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(Date vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
}