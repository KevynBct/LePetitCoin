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

        // Image
        TextDrawable drawable = TextDrawable.builder().buildRound("", this.getColor(e.getDistance()));
        ImageView image = (ImageView)row.findViewById(R.id.imageElem);
        image.setImageDrawable(drawable);

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

    public int getColor(Float _distance){
        int color = Color.parseColor("#EF5350");

        if(_distance < 700){
            color = Color.parseColor("#66BB6A");
        }else if(_distance >= 700 && _distance < 1000){
            color = Color.parseColor("#FFEE58");
        }

        return color;
    }
}