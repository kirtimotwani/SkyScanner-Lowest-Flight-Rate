/**
 * Author: Kirti Motwani
 * Last Modified: November 10, 2019
 *
 * This program demonstrates a simple servlet that is
 * used to take input from the android application
 * and hits the API SkyScanner to get information to be displayed
 * on android app
 */
import com.google.gson.JsonObject;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Project4Task1Servlet extends HttpServlet {

    Project4Model p4m = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        p4m = new Project4Model();
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("search: "+request.getPathInfo());
        String searchURl=request.getPathInfo();

        //String processing to pass appropriate URL to the API

        String origin=searchURl.substring(1,searchURl.indexOf("+"));
        String dest=searchURl.substring(searchURl.indexOf("+")+1,searchURl.lastIndexOf("+"));
        String depDate= searchURl.substring(searchURl.lastIndexOf("+")+1);

        JsonObject pictureURL = null;
        try {
            pictureURL = p4m.doFlightsearch(origin,dest,depDate);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        //Json reponse converted to String and passed
        response.getWriter().append(pictureURL.toString());

    }
}
