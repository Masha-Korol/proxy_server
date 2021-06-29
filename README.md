Proxy server

This application provides REST-API redirecting users’ requests to proxy servers. User can choose a country - preferable proxy location. 

Technology stack:

Spring Boot;
Maven;
PostgreSQL database;
Hibernate.


To install the application use from the root folder:

mvn install 

To run the application use from the root folder:

mvn spring-boot:run

How to use the application (views in the Postman):

1. To get the list of all the available proxy servers:

http://localhost:8080/api/v1/servers/ 

 

2. To send a request:

http://localhost:8080/api/v1/requests/ 

 


3. To change time interval for proxy servers’ list updating:

http://localhost:8080/api/v1/servers/timing



