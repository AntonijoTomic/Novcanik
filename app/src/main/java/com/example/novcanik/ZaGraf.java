package com.example.novcanik;

public class ZaGraf {
    private Double iznos;
    private String podkategorija;

    public ZaGraf(Double iznos, String podkategorija) {
        this.iznos = iznos;
        this.podkategorija = podkategorija;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }


    public String getPodkategorija() {
        return podkategorija;
    }

    public void setPodkategorija(String podkategorija) {
        this.podkategorija = podkategorija;
    }
}