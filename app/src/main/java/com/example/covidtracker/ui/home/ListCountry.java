package com.example.covidtracker.ui.home;

public class ListCountry {
    String tCases, cName, tDeath, tRecovered, cFlag;

    public void setcFlag(String cFlag) {
        this.cFlag = cFlag;
    }

    public String getcFlag() {
        return cFlag;
    }

    public void settDeath(String tDeath) {
        this.tDeath = tDeath;
    }

    public String gettDeath() {
        return tDeath;
    }

    public void settRecovered(String tRecovered) {
        this.tRecovered = tRecovered;
    }

    public String gettRecovered() {
        return tRecovered;
    }

    public void settCases(String tCases) {
        this.tCases = tCases;
    }

    public String gettCases() {
        return tCases;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcName() {
        return cName;
    }

    public ListCountry(String tCases, String tDeath, String tRecovered, String cFlag, String cName){
        this.tCases = tCases;
        this.tDeath = tDeath;
        this.tRecovered = tRecovered;
        this.cFlag = cFlag;
        this.cName = cName;
    }
}
