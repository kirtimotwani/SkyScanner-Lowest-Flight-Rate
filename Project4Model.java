/**
 * Author: Kirti Motwani
 * Last Modified: November 10, 2019
 *
 * This program demonstrates the java class that implements
 * the business logic for the business logic for the project
 * The program takes input received by servlet and queries the
 * API, parses through the Json String received and processes it
 * to be sent in desired format.
 */
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Date;


public class Project4Model {
    public JsonObject doFlightsearch(String origin, String destination, String depDate) throws UnirestException {
        try {
            Project4Mongo pm = new Project4Mongo();

            Date date = new Date();
            long time = date.getTime();
            Timestamp startTime = new Timestamp(time);
            String originTrim = origin.trim();
            String originSearch = originTrim.replaceAll(" ", "%20");
            HttpResponse<String> airportOrigin = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query=" + originSearch)
                    .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                    .header("x-rapidapi-key", "03bd850e97mshc76ca1a6109b4f2p196a9ajsn2d24da601da1")
                    .asString();

            String originString = airportOrigin.getBody().toString();
            JsonObject originObj = new Gson().fromJson(originString, JsonObject.class);

            //processing origin
            JsonElement res = originObj.get("Places");
            String ree = res.toString();
            String originAir = ree.substring(ree.indexOf("PlaceId") + 10, ree.indexOf(",") - 1);

            String destTrim = destination.trim();
            String destSearch = destTrim.replaceAll(" ", "%20");
            HttpResponse<String> airportDest = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/US/GBP/en-GB/?query=" + destSearch)
                    .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                    .header("x-rapidapi-key", "03bd850e97mshc76ca1a6109b4f2p196a9ajsn2d24da601da1")
                    .asString();

            //processing destination
            String destString = airportDest.getBody().toString();
            JsonObject destObj = new Gson().fromJson(destString, JsonObject.class);
            JsonElement destAir = destObj.get("Places");
            String destA = destAir.toString();
            String dest = destA.substring(destA.indexOf("PlaceId") + 10, destA.indexOf(",") - 1);

            HttpResponse<String> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsedates/v1.0/US/USD/en-US/" + originAir + "/" + dest + "/" + depDate + "?inboundpartialdate=2019-12-01")
                    .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                    .header("x-rapidapi-key", "03bd850e97mshc76ca1a6109b4f2p196a9ajsn2d24da601da1")
                    .asString();

            //processing the obtained Json
            String respStr = response.getBody().toString();
            JsonObject respObj = new Gson().fromJson(respStr, JsonObject.class);

            //retrieving useful information and leaving the rest from API
            JsonElement responseElemet = respObj.get("Dates");
            String resp = responseElemet.toString();
            String travelDate = resp.substring(resp.indexOf("PartialDate") + 14, resp.indexOf(",") - 1);
            JsonElement quoteElement = respObj.get("Quotes");
            String quoteStr = quoteElement.toString();
            String minPriceStr = quoteStr.substring(quoteStr.indexOf("MinPrice"));
            String minPrice = minPriceStr.substring(10, minPriceStr.indexOf(","));
            String carrID = quoteStr.substring(quoteStr.indexOf("CarrierIds") + 13, quoteStr.indexOf("]"));
            JsonElement carr = respObj.get("Carriers");
            String carriers = carr.toString();
            String airlinesID = carriers.substring(carriers.indexOf(carrID));
            String airlines = airlinesID.substring(airlinesID.indexOf("Name") + 7, airlinesID.indexOf("}") - 1);

            Date enddate = new Date();
            long endtime = enddate.getTime();
            Timestamp endTime = new Timestamp(endtime);

            //calculating API latency
            long apiLatency = endTime.getTime() - startTime.getTime();

            //building Json to be sent
            JsonObject results = new JsonObject();
            results.addProperty("Travel Date", depDate);
            results.addProperty("Minimum Fare", minPrice);
            results.addProperty("Airlines", airlines);

            pm.insertDocuments(depDate, destination, Double.valueOf(minPrice), airlines, startTime, apiLatency);

            String returnedValue = response.getBody().toString();
            return results;
        }
        catch(Exception e)
        {
            JsonObject error=new JsonObject();
            error.addProperty("Travel Date","Results Not Found");
            error.addProperty("Minimum Fare", "or Bad Request");
            error.addProperty("Airlines", "Enter input again");
            return error;
        }
    }
}



