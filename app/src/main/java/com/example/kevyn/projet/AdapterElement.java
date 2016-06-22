package com.example.kevyn.projet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

public class AdapterElement extends ArrayAdapter<Element>{

    public AdapterElement(Context _context, List<Element> _element) {
        super(_context, R.layout.liste_element, _element);
    }

    public View getView(int _position, View _convertView, ViewGroup _parent){
        View row;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        row = inflater.inflate(R.layout.liste_element, null);

        Element e = getItem(_position);
        TextDrawable drawable = TextDrawable.builder().buildRound(e.getAdresse().substring(0,1).toUpperCase(), generator.getRandomColor());
        ImageView image = (ImageView)row.findViewById(R.id.imageElem);
        image.setImageDrawable(drawable);
        TextView adresse = (TextView)row.findViewById(R.id.adresseElem);
        adresse.setText(e.getAdresse());
        TextView commune = (TextView)row.findViewById(R.id.communeElem);
        commune.setText(e.getCommune());
        TextView horaires = (TextView)row.findViewById(R.id.horairesElem);
        horaires.setText(e.getHoraires());

        return row;
    }
}