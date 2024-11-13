package com.example.covidtracker.ui.home;

public class Home{
    String Continent, Cases, Deaths,  Recovered, Population;
    public String getContinent(){return Continent;}
    public void setContinent(String Continent){this.Continent = Continent;}
    public String getCases(){return Cases;}
    public void setCases(String Cases){this.Cases = Cases;}
    public String getDeaths(){return Deaths;}
    public void setDeaths(String Deaths){this.Deaths = Deaths;}
    public String getRecovered(){return Recovered;}
    public void setRecovered(String Recovered){this.Recovered = Recovered;}
    public String getPopulation(){return Population;}
    public void setPopulation(String Population){this.Population = Population;}


    public Home(String Continent, String Cases, String Deaths, String Recovered, String Population){
        this.Continent = Continent;
        this.Cases = Cases;
        this.Deaths = Deaths;
        this.Recovered = Recovered;
        this.Population = Population;

    }

}
