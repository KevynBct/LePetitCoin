package com.example.kevyn.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class AdapterElement extends ArrayAdapter<Element>{

    public AdapterElement(Context _context, List<Element> _element) {
        super(_context, R.layout.liste_element, _element);
    }

    public View getView(int _position, View _convertView, ViewGroup _parent){
        View row;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        row = inflater.inflate(R.layout.liste_element, null);
        Element e = getItem(_position);

        // Distance
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        TextView distance = (TextView) row.findViewById(R.id.textDistance);
        distance.setText(df.format(e.getDistance()/1000)+"km");

        // Adresse
        TextView adresse = (TextView)row.findViewById(R.id.adresseElem);
        adresse.setText(e.getAdresse());

        // Commune
        TextView commune = (TextView)row.findViewById(R.id.communeElem);
        commune.setText(e.getCommune());

        // Horaires
        TextView horaires = (TextView)row.findViewById(R.id.horairesElem);
        horaires.setText(e.getHoraires());

        return row;
    }
}