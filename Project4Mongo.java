/**
 * Author: Kirti Motwani
 * Last Modified: November 10, 2019
 *
 * This program demonstrates the java class that implements
 * the business logic for the business logic for the project
 * The program takes input received by servlet and queries the
 * API. The class receives the 6 useful data points and inserts
 * them to the mongoDB
 * Atlas database
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.Timestamp;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;


public class Project4Mongo {

        public static void insertDocuments(String depDate,String destination, Double minPrice, String airlines, Timestamp requestTime, Long latency)
        {
            //connect to mongoDB Atlas
            String connectionURL = "mongodb+srv://kirti:project4@cluster0-lfizk.mongodb.net/test?retryWrites=true&w=majority";
            MongoClientURI connection = new MongoClientURI(connectionURL);
            MongoClient client = new MongoClient(connection);
            MongoDatabase dB = client.getDatabase("project4");
            MongoCollection<Document> collection = dB.getCollection("User_Request_Data");

            // create new document and insert
            Document doc = new Document("Departure Date", depDate)
                    .append("Destination",destination)
                    .append("Minimum Price",minPrice)
                    .append("Airlines",airlines)
                    .append("Request Timestamp",requestTime)
                    .append("API Latency",latency);
            collection.insertOne(doc);
            try (MongoCursor<Document> cursor = collection.find().projection(fields(excludeId())).iterator()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            }
        }
    }
