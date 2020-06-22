# SkyScanner-Lowest-Flight-Rate
### An android application with a Web service developed in Java using MongoDB as back-end database. Application used SkyScanner public API to get lowest flight prices. The web service is deployed on Heroku.

#### Tech Stack used: Java, Web Development, MongoDB, Heroku, Docker, Web Server coding, HTML, IntelliJ, Android App Development

I did this project as a part of one of my courses- 'Distributed Systems for ISM' in Masters studies at CMU. I developed an android application where a person can select Origin, Destination and a date of travel, the application returns the date on which flight fare is minimum within a week of the entered date, the fare amount and other details of the airlines. The application uses RESTful Web service that communicates with a third party public API (SkyScanner) to fetch the required information for given inputs by user, the web service simultaneouly stores this information in MongoDB Database. Another web service is used to display the a dashboard with important log information on a Webpage using HTML. 
The application is deployed on a Cloud plaform- Heroku so that the web application cam run efficiently without having to manually start the server every time.

Below is an illustration of the flow of my project

! [diag](https://github.com/kirtimotwani/SkyScanner-Lowest-Flight-Rate/blob/master/android.jpg)
