package edu.cmu.ds.androidinterestingpicturelab;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/*
 * This class provides capabilities to get infromation about cheap
 * air tickets from skyScanner API. The Android app takes origin,
 * destination and departure date as input and displays minimum air fare,
 * date after desired departure date on which fare is minimum and the
 * airlines that provide the fare
 * 
 */
public class GetFlightFares {
	FlightSearchMainClass ip = null;
	
	/*
	 * search is the public GetFlightFares method.  Its arguments are the origin, destination & departure date and the object that called it.  This provides a callback
	 * path such that the resultsReady method in that object is called when the information is available from the search.
	 */
	public void search(String origin,String destination, String travelDate, FlightSearchMainClass ip) {
		this.ip = ip;
		new AsyncFlightSearch().execute(origin,destination,travelDate);
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
    private class AsyncFlightSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return search(urls[0], urls[1], urls[2]);
        }

        protected void onPostExecute(String results) {
        	ip.resultsReady(results);
        }

        /*
         * Pass the search string on web service deployed
         * to Heroku which then fetches information from Skyscanner API
         */
        private String search(String origin, String destn, String depDate) { // TODO
//        	input "pittsburgh,florida,2019-11-25";
			String result="";
			try {
			URL herokuUrl = new URL("https://hidden-lake-39790.herokuapp.com/getFlights/"+origin+"+"+destn+"+"+depDate);
			HttpURLConnection conn = (HttpURLConnection)herokuUrl.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			result = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String resultString = result;
			JsonObject resultJson = new Gson().fromJson(resultString, JsonObject.class);
			JsonElement price=resultJson.get("Minimum Fare");
			JsonElement airline=resultJson.get("Airlines");
			JsonElement travel=resultJson.get("Travel Date");
			String resultOutput="Minimum Fare: $"+price+"\n"+"Airlines: "+airline+"\n"+"Travel Date: "+travel;
			return resultOutput;
        }
    }
}