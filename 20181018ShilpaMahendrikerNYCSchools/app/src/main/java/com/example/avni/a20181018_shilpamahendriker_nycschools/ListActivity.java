package com.example.avni.a20181018_shilpamahendriker_nycschools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    // Array list which populates data parsed from the JSON NYC API
    ArrayList<School> schools = new ArrayList<>();
    ProgressBar progressBar;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Using Volley library to parse Json data
        mQueue = Volley.newRequestQueue(this);

        // Method to parse JSON
        jsonParse();


        /*Creating the instance of the School adapter*/
        SchoolAdapter adapter = new SchoolAdapter(this, schools);

        // Defining the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attaching the school adapter to reclycer view in order to populate the JSON Parsed data
        recyclerView.setAdapter(adapter);
    }

    private void jsonParse() {
        // json object response url
        String url = "https://data.cityofnewyork.us/resource/97mf-9njv.json?$select=borough,dbn,school_name,location";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Using Try catch to catch any exception while parsing
                        try {
                            // Iterating for loop to the end of the parsed arrays and adding them to location arraylist in every iteration
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject school = response.getJSONObject(i);
                                String schoolName = school.getString("school_name");
                                String borough = school.getString("borough");
                                String dbn = school.getString("dbn");
                                String location = school.getString("location");
                                // add the parsed array to arraylist
                                schools.add(new School(dbn, schoolName, borough, location));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Using progress bar which shows up if loading id slow
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                // Checking for possible errors while parsing and displaying the error message
                if (error instanceof NetworkError) {
                    errorMessage(getResources().getString(R.string.network_error_msg));

                } else if (error instanceof ServerError) {
                    errorMessage(getResources().getString(R.string.server_error_msg));

                } else if (error instanceof AuthFailureError) {
                    errorMessage(getResources().getString(R.string.network_error_msg));

                } else if (error instanceof ParseError) {
                    errorMessage(getResources().getString(R.string.parsing_error_msg));

                } else if (error instanceof NoConnectionError) {
                    errorMessage(getResources().getString(R.string.network_error_msg));

                } else if (error instanceof TimeoutError) {
                    errorMessage(getResources().getString(R.string.network_error_msg));

                }
            }
        });

        mQueue.add(request);
    }
    // Method to display the parsing error message and stopping the progress bar loading spinner
    private void errorMessage(String errMsg){
        Toast.makeText(getApplicationContext(), errMsg,
                Toast.LENGTH_LONG).show();
        findViewById(R.id.progressBar).setVisibility(View.GONE); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


                finish();
                startActivity(getIntent());

        return super.onOptionsItemSelected(item);
    }

}
