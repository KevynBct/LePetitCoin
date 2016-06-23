package com.example.kevyn.projet;

/**
 * Created by kevyn on 21/06/16.
 */
public class Element {
    private String commune;
    private String adresse;
    private Double lat;
    private Double lng;
    private String horaires;

    public Element(String _commune, String _adresse, String _position, String _horaires){
        this.commune = _commune;
        this.adresse = _adresse;
        this.toLngLat(_position);
        this.horaires = _horaires;
    }

    public String getCommune(){
        return this.commune;
    }

    public String getAdresse(){
        return this.adresse;
    }

    public Double getLat(){ return this.lat; }

    public Double getLng(){ return this.lng; }

    public String getHoraires(){
        return this.horaires;
    }

    public void toLngLat(String _position){
        String [] tab;
        tab = _position.split(",");

        tab[0] = tab[0].substring(1);
        tab[1] = tab[1].substring(0, tab[1].length()-1);

        this.lat = Double.parseDouble(tab[0]);
        this.lng = Double.parseDouble(tab[1]);
    }

    public String toString(){
        return this.adresse+", "+this.commune+", "+this.horaires;
    }
}
