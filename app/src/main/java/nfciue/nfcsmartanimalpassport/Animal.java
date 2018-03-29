package nfciue.nfcsmartanimalpassport;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by ASLI on 20.3.2018.
 */

public class Animal implements Serializable {


    private boolean alumVaccine;
    private String birthFarmNo;
    private String birthdate;
    private String breed;
    private boolean brusellosisVaccine;
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
    private boolean pasturellaVaccine;



    private ArrayList <otherVaccine> otherVaccine;
    private ArrayList <Operations> operations;

    public Animal(boolean alumVaccine, String birthFarmNo, String birthdate, String breed, boolean brusellosisVaccine, String currentFarmNo, String deathDate, String deathPlace, String eSignDirector, String eSignOwner, int exportCountryCode, String exportDate, String farmChangeDate, String iD, boolean isFemale, String motherId, String ownerTc, boolean pasturellaVaccine, ArrayList<otherVaccine> otherVaccine, ArrayList<Operations> Operations) {
        this.alumVaccine = alumVaccine;
        this.birthFarmNo = birthFarmNo;
        this.birthdate = birthdate;
        this.breed = breed;
        this.brusellosisVaccine = brusellosisVaccine;
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
        this.pasturellaVaccine = pasturellaVaccine;
        this.otherVaccine = otherVaccine;
        this.operations = Operations;
    }

    public boolean isAlumVaccine() {
        return alumVaccine;
    }

    public void setAlumVaccine(boolean alumVaccine) {
        this.alumVaccine = alumVaccine;
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

    public boolean isBrusellosisVaccine() {
        return brusellosisVaccine;
    }

    public void setBrusellosisVaccine(boolean brusellosisVaccine) {
        this.brusellosisVaccine = brusellosisVaccine;
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

    public boolean isPasturellaVaccine() {
        return pasturellaVaccine;
    }

    public void setPasturellaVaccine(boolean pasturellaVaccine) {
        this.pasturellaVaccine = pasturellaVaccine;
    }
    public ArrayList<Operations> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operations> Operations) {
        this.operations = Operations;
    }
    public ArrayList<otherVaccine> getOtherVaccine() {
        return otherVaccine;
    }

    public void setOtherVaccine(ArrayList <otherVaccine> otherVaccine) {
        this.otherVaccine = otherVaccine;
    }



    public Animal(){}



}
