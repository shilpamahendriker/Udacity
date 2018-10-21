package com.example.avni.a20181018_shilpamahendriker_nycschools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

public class SchoolDetailsActivity extends AppCompatActivity {
    TextView textViewSchoolName;
    TextView textViewSchoolLocation;
    TextView textViewSatScoresLbl;
    TextView textViewCriticalReading;
    TextView textViewMathAvg;
    TextView textViewWritingAvg;
    private RequestQueue mQueue;
    private String location;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        //Enabling up navigation to List Activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Extracting data received through the intent
        Intent intent = getIntent();
        String dbn = intent.getStringExtra("DBN");
        location = intent.getStringExtra("LOCATION");
        name = intent.getStringExtra("NAME");

        //Using Volley library to parse the data from NYC OPEN API
        mQueue = Volley.newRequestQueue(this);

        //Defining textviews to display school data and sat scores

        textViewSchoolName = findViewById(R.id.txtSchoolName);
        textViewSchoolLocation = findViewById(R.id.txtSchoolLocation);
        textViewSatScoresLbl = findViewById(R.id.txtSatScoresLabel);
        textViewCriticalReading = findViewById(R.id.txtCriticalReadingAvg);
        textViewMathAvg = findViewById(R.id.txtMathAvg);
        textViewWritingAvg = findViewById(R.id.txtWritingAvg);


        // json object response url which returns json object with the given DBN of school which is received
        // from List activity
        final String url = "https://data.cityofnewyork.us/resource/734v-jeq5.json?dbn=" + dbn;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Checking if in case there is no JSON response for a DBN of the school and
                        // setting appropriate text to all the display text views
                        if (response.length() < 1) {

                            textViewSchoolName.setText(name);
                            location = location.split("\\(")[0];
                            textViewSchoolLocation.setText(location);
                            textViewSatScoresLbl.setText(R.string.no_sat_data);
                            textViewCriticalReading.setVisibility(View.GONE);
                            textViewMathAvg.setVisibility(View.GONE);
                            textViewWritingAvg.setVisibility(View.GONE);
                        } else {

                            try {
                                // Using Try catch to catch any exception while parsing
                                JSONObject school = response.getJSONObject(0);
                                String schoolName = school.getString("school_name");
                                int criticalReadingAvgScore = school.getInt("sat_critical_reading_avg_score");
                                int satMathAvgScore = school.getInt("sat_math_avg_score");
                                int satWritingAvgScore = school.getInt("sat_writing_avg_score");
                                textViewSchoolName.setText(schoolName);
                                location = location.split("\\(")[0];
                                textViewSchoolLocation.setText(location);
                                textViewSatScoresLbl.setText(R.string.sat_scores);
                                String criticalReadingLabel = getResources().getString(R.string.critical_reading_label);
                                textViewCriticalReading.setText(getResources().getString(R.string.critical_reading_label) + String.valueOf(criticalReadingAvgScore));
                                textViewMathAvg.setText(getResources().getString(R.string.Math_average_label) + String.valueOf(satMathAvgScore));
                                textViewWritingAvg.setText(getResources().getString(R.string.Writing_average) + String.valueOf(satWritingAvgScore));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
}
