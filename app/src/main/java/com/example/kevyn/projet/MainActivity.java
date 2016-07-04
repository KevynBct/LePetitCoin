package com.example.kevyn.projet;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private ArrayList<Element> resultList;
    private AdapterElement adapter;
    private ListView list;
    private Spinner spinner;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTime = true;

        // ResultList
        resultList = new ArrayList<Element>();
        adapter = new AdapterElement(this, resultList);
        list = (ListView) findViewById(R.id.resultList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> liste, View v, int position, long id) {
                Element elem = (Element) liste.getItemAtPosition(position);
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                LatLng latLng = new LatLng(elem.getLat(), elem.getLng());
                builder.setLatLngBounds(new LatLngBounds(latLng, latLng));
                try {
                    startActivityForResult(builder.build(getApplicationContext()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        list.setAdapter(adapter);


        // Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<Integer> spinnerList = new ArrayList<Integer>();
        spinnerList.add(5);
        spinnerList.add(10);
        spinnerList.add(25);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!firstTime) search();
                    else firstTime = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void searchButtonClick(View v) throws IOException {
        search();
    }

    public void search() throws IOException {
        EditText editText = (EditText) findViewById(R.id.searchBar);
        String text = editText.getText().toString().trim();

        if(text.length() != 0){
            doRequest(createAddressRequest(text));
        }else {
            Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
        }
        list.setAdapter(adapter);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String createAddressRequest(String _address) throws IOException {
        String result = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00170/Toilettes_publiques_nm_STBL/content";
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address = geocoder.getFromLocationName(_address, 1);

        if (address == null) return result;

        Address location = address.get(0);
        result += "?filter={\"_l\":{\"$near\":["+Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude())+"]}}";

        return result;
    }


    public void doRequest(String _addressRequest){
        resultList.clear();
        RequestQueue  queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,_addressRequest,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    Element    elemTmp;
                                    EditText   editText = (EditText) findViewById(R.id.searchBar);
                                    JSONObject repObj = (JSONObject) new JSONTokener(response).nextValue();
                                    JSONArray  jsonArray = repObj.optJSONArray("data");

                                    for(int i=0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        if(resultList.size() < (Integer) spinner.getSelectedItem()){
                                            elemTmp = new Element(jsonObject.optString("COMMUNE").toString(),jsonObject.optString("ADRESSE").toString(),jsonObject.optString("_l").toString(), jsonObject.optString("INFOS_HORAIRES").toString());

                                            if(editText.getText().toString().trim().length() != 0){
                                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                                List<Address> address;
                                                address = geocoder.getFromLocationName(editText.getText().toString().trim(),1);
                                                if (address != null) {
                                                    Address location = address.get(0);
                                                    elemTmp.setDistance(location.getLatitude(), location.getLongitude());
                                                }
                                            }
                                            resultList.add(elemTmp);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException je) {
                                    Toast.makeText(getApplicationContext(), je.toString(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }},
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.getMessage());
                            }}
                ){};
        queue.add(stringRequest);
    }
}