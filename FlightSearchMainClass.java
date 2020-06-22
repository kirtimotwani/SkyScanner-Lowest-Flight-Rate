package edu.cmu.ds.androidinterestingpicturelab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FlightSearchMainClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully getting information from web service, it
         * can callback to this object with the resulting information.  The "this" of the OnClick will be the OnClickListener.
         */
        final FlightSearchMainClass ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button) findViewById(R.id.submit);

        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String origin = ((EditText) findViewById(R.id.searchTerm1)).getText().toString();
                String destination = ((EditText) findViewById(R.id.searchTerm2)).getText().toString();
                String departureDate = ((EditText) findViewById(R.id.searchTerm3)).getText().toString();
                GetFlightFares gp = new GetFlightFares();
                gp.search(origin,destination,departureDate, ma); // Done asynchronously in another thread.  It calls ip.resultsReady() in this thread when complete.
            }
        });
    }

    /*
     * This is called by the GetFlightFares object when the information is ready.  This allows for passing back the information for updating the TextView
     */
    public void resultsReady(String results) {
        TextView result=(TextView) findViewById(R.id.result);
        result.setVisibility(View.VISIBLE);
        result.setText(results);

        TextView searchView = (EditText)findViewById(R.id.searchTerm1);
        if (results != null) {
        } else {
        }
        searchView.setText("");
        TextView searchView2 = (EditText)findViewById(R.id.searchTerm2);
        if (results != null) {
        } else {
        }
        searchView.setText("");
        TextView searchView3 = (EditText)findViewById(R.id.searchTerm3);
        if (results != null) {
        } else {
        }
        searchView.setText("");
        searchView2.setText("");
        searchView3.setText("");
    }
}
