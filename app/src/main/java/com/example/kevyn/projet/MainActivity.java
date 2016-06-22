package com.example.kevyn.projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Element> resultListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void searchButtonClick(View v){
        resultListe = new ArrayList<Element>();
        EditText    editText = (EditText) findViewById(R.id.searchBar);
        String      adresseRequest = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00170/Toilettes_publiques_nm_STBL/content?filter={\"COMMUNE\":{\"$eq\":\""+editText.getText()+"\"}}";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,adresseRequest,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject repObj = (JSONObject) new JSONTokener(response).nextValue();
                                    JSONArray jsonArray = repObj.optJSONArray("data");
                                    for(int i=0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        resultListe.add(new Element(jsonObject.optString("COMMUNE").toString(),jsonObject.optString("ADRESSE").toString(),jsonObject.optString("_l").toString(), jsonObject.optString("INFOS_HORAIRES").toString()));
                                    }
                                } catch (JSONException je) {
                                    Toast.makeText(getApplicationContext(), je.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }},
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.getMessage());
                            }}
                ){};
        queue.add(stringRequest);

        ListView lv = (ListView) findViewById(R.id.resultList);
        lv.setAdapter(new AdapterElement(this, resultListe));
    }

    public void mapButtonClick(View v) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }
}