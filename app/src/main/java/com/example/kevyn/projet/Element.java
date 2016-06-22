package com.example.kevyn.projet;

/**
 * Created by kevyn on 21/06/16.
 */
public class Element {
    private String commune;
    private String adresse;
    private String position;
    private String horaires;

    public Element(String _commune, String _adresse, String _position, String _horaires){
        this.commune = _commune;
        this.adresse = _adresse;
        this.position = _position;
        this.horaires = _horaires;
    }

    public String getCommune(){
        return this.commune;
    }

    public String getAdresse(){
        return this.adresse;
    }

    public String getPosition(){
        return this.position;
    }

    public String getHoraires(){
        return this.horaires;
    }

    public String toString(){
        return this.adresse+", "+this.commune+", "+this.position+", "+this.horaires;
    }
}
