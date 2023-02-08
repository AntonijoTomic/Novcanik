package com.example.novcanik;

import java.io.Serializable;
import java.util.Date;

public class Transakcija implements Serializable {
    private Double iznos;
    private CharSequence datum;
    private String kategorija;
    private String podkategorija;

    public Transakcija(Double iznos, CharSequence datum, String kategorija, String podkategorija) {
        this.iznos = iznos;
        this.datum = datum;
        this.kategorija = kategorija;
        this.podkategorija = podkategorija;
    }

    public CharSequence getDatum() {
        return datum;
    }

    public void setDatum(CharSequence datum) {
        this.datum = datum;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getPodkategorija() {
        return podkategorija;
    }

    public void setPodkategorija(String podkategorija) {
        this.podkategorija = podkategorija;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }


}
