# SkyScanner-Lowest-Flight-Rate
### An android application with a Web service developed in Java using MongoDB as back-end database. Application used SkyScanner public API to get lowest flight prices. The web service is deployed on Heroku.

#### Tech Stack used: Java, Web Development, MongoDB, Heroku, Docker, Web Server coding, HTML, JSON, BSON, IntelliJ, Android App Development

I did this project as a part of one of my courses- 'Distributed Systems for ISM' in Masters studies at CMU. I developed an android application where a person can select Origin, Destination and a date of travel, the application returns the date on which flight fare is minimum within a week of the entered date, the fare amount and other details of the airlines. The application uses RESTful Web service that communicates with a third party public API (SkyScanner) to fetch the required information for given inputs by user, the web service simultaneouly stores this information in MongoDB Database. Another web service is used to display the a dashboard with important log information on a Webpage using HTML. 
The application is deployed on a Cloud plaform- Heroku so that the web application cam run efficiently without having to manually start the server every time.

Below is an illustration of the flow of my project

![figure](https://github.com/kirtimotwani/SkyScanner-Lowest-Flight-Rate/blob/master/android.jpg)

Given below is the details of what each file does in the project

Project4Model.java and Project4Mongo.java- The projecy also follows a separation of concerns where each program is written to perform a specific task. These programs implement the business logic of the web application where one java class fetches data from 3rd party API and other stores it in MongoDB.

![mongo](https://github.com/kirtimotwani/SkyScanner-Lowest-Flight-Rate/blob/master/mongo.png)

CreateDashboardServlet.java and Project4Task1Servlet.java- These programs return the response from API in JSON format and aggregate the important logging analytics information to display on a webpage

These webservices are deployed on a cloud platform- Heroku that can be accessed using the link- https://hidden-lake-39790.herokuapp.com/ 

"https://hidden-lake-39790.herokuapp.com/getFlights/"+origin+"+"+destn+"+"+depDate for the URL- "https://hidden-lake-39790.herokuapp.com/getFlights/Pittsburgh+Florida+2019-11-27" the JSON response would be

{"Travel Date":"2019-11-27","Minimum Fare":"116.0","Airlines":"American Airlines"} 

And the url pattern for accessing Dashboard with useful analytics information is

https://hidden-lake-39790.herokuapp.com/getDashboard

![dash](https://github.com/kirtimotwani/SkyScanner-Lowest-Flight-Rate/blob/master/dash.png)
