package nfciue.nfcsmartanimalpassport;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by ASLI on 20.3.2018.   // commit for anıl
 */

public class Animal implements Serializable {



    private String birthFarmNo;
    private String birthdate;
    private String breed;
    private String currentFarmNo;
    private String deathDate;
    private String deathPlace;
    private String eSignDirector;
    private String eSignOwner;
    private int exportCountryCode;
    private String exportDate;
    private String farmChangeDate;
    private String iD;
    private boolean isFemale;
    private String motherId;
    private String  ownerTc;




    private ArrayList <Vaccine> vaccines;
    private ArrayList <Operations> operations;

    public Animal(String birthFarmNo, String birthdate, String breed, String currentFarmNo, String deathDate, String deathPlace, String eSignDirector, String eSignOwner, int exportCountryCode, String exportDate, String farmChangeDate, String iD, boolean isFemale, String motherId, String ownerTc, ArrayList<Vaccine> Vaccine, ArrayList<Operations> Operations) {

        this.birthFarmNo = birthFarmNo;
        this.birthdate = birthdate;
        this.breed = breed;
        this.currentFarmNo = currentFarmNo;
        this.deathDate = deathDate;
        this.deathPlace = deathPlace;
        this.eSignDirector = eSignDirector;
        this.eSignOwner = eSignOwner;
        this.exportCountryCode = exportCountryCode;
        this.exportDate = exportDate;
        this.farmChangeDate = farmChangeDate;
        this.iD = iD;
        this.isFemale = isFemale;
        this.motherId = motherId;
        this.ownerTc = ownerTc;
        this.vaccines = Vaccine;
        this.operations = Operations;
    }



    public String getBirthFarmNo() {
        return birthFarmNo;
    }

    public void setBirthFarmNo(String birthFarmNo) {
        this.birthFarmNo = birthFarmNo;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }



    public String getCurrentFarmNo() {
        return currentFarmNo;
    }

    public void setCurrentFarmNo(String currentFarmNo) {
        this.currentFarmNo = currentFarmNo;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String geteSignDirector() {
        return eSignDirector;
    }

    public void seteSignDirector(String eSignDirector) {
        this.eSignDirector = eSignDirector;
    }

    public String geteSignOwner() {
        return eSignOwner;
    }

    public void seteSignOwner(String eSignOwner) {
        this.eSignOwner = eSignOwner;
    }

    public int getExportCountryCode() {
        return exportCountryCode;
    }

    public void setExportCountryCode(int exportCountryCode) {
        this.exportCountryCode = exportCountryCode;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public String getFarmChangeDate() {
        return farmChangeDate;
    }

    public void setFarmChangeDate(String farmChangeDate) {
        this.farmChangeDate = farmChangeDate;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherİd) {
        this.motherId = motherİd;
    }


    public String getOwnerTc() {
        return ownerTc;
    }

    public void setOwnerTc(String ownerTc) {
        this.ownerTc = ownerTc;
    }

    public ArrayList<Operations> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operations> Operations) {
        this.operations = Operations;
    }
    public ArrayList<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(ArrayList <Vaccine> vaccines) {
        this.vaccines = vaccines;
    }



    public Animal(){}



}
