/**
 * Author: Kirti Motwani
 * Last Modified: November 10, 2019
 *
 * This program demonstrates the web servlet that is used to
 * query the logs stored in MongoDB database in order to
 * create a web-based dashboard displaying analytics metrics
 * and logs. The dashboard is created in Dashboard.jsp file
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

@WebServlet(name = "CreateDashboardServlet",
      urlPatterns = {"/getDashboard"})
public class CreateDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        //connect to mongoDB Atlas
        String connectionURL = "mongodb+srv://kirti:project4@cluster0-lfizk.mongodb.net/test?retryWrites=true&w=majority";
        MongoClientURI connection = new MongoClientURI(connectionURL);
        MongoClient client = new MongoClient(connection);
        MongoDatabase dB = client.getDatabase("project4");
        MongoCollection<Document> collection = dB.getCollection("User_Request_Data");

        //ArayList for sending information to jsp
        List<Integer> reqNum=new ArrayList<>();
        List<JsonElement> destinations=new ArrayList<>();
        List<JsonElement> timeS=new ArrayList<>();
        List<Integer> lat=new ArrayList<>();
        List<Double> minPr=new ArrayList<>();

        int count=0;

        //query mongoDB database
        try (MongoCursor<Document> cursor = collection.find().projection(fields(excludeId())).iterator()) {
            while (cursor.hasNext()) {
                JsonObject jobj = new Gson().fromJson((cursor.next().toJson()), JsonObject.class);
                JsonElement Dest = jobj.get("Destination");
                JsonElement req_time=jobj.get("Request Timestamp");
                JsonObject jsonDateObject = req_time.getAsJsonObject();
                JsonElement date=jsonDateObject.get("$date");
                JsonElement api_lat=jobj.get("API Latency");
                JsonObject jsonLatObject = api_lat.getAsJsonObject();
                JsonElement latency=jsonLatObject.get("$numberLong");
                String latStr=latency.toString().replaceAll("\"","");
                JsonElement minPrice=jobj.get("Minimum Price");
                String minPrStr=minPrice.toString();
                count+=1;
                reqNum.add(count);
                destinations.add(Dest);
                timeS.add(date);
                lat.add(Integer.valueOf(latStr));
                minPr.add(Double.valueOf(minPrStr));
            }
        }
        int sumLatency=0;
        for (int i=0;i<lat.size();i++)
        {
            sumLatency+=lat.get(i);
        }
        int averageLat=sumLatency/lat.size();

        double sumMinPrice=0;
        for (int i=0;i<minPr.size();i++)
        {
            sumMinPrice+=minPr.get(i);
        }
        double avgMinPrice=sumMinPrice/minPr.size();

        request.setAttribute("AvgLat",averageLat);
        request.setAttribute("AvgMinPrice",avgMinPrice);
        String nextView="Dashboard.jsp";
        request.setAttribute("Destinations",destinations);
        request.setAttribute("reqNum",reqNum);
        request.setAttribute("timeStamp",timeS);
        request.setAttribute("latency",lat);
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);

    }
}
