# Test Task for Java Developer
The project contains a Spring Boot RESTful API that updates statistics in a MongoDB database and caches responses.

Key features implemented:

User registration and authentication.
CRUD operations on users.
Retrieval of statistics for a specified date (or date range).
Retrieval of statistics for a specified ASIN (or list of ASINs).
Retrieval of aggregated statistics for all dates.
Retrieval of aggregated statistics for all ASINs.
Statistics are updated from the 'test_report.json' file every 10 minutes (you can edit the filename and path by modifying the batch.properties file in the batch module).
Project requirements fulfilled:

Spring Security (JWT) for authentication (you can customize settings by editing the application.yaml file in the security module).
Map for caching.
Database: MongoDB (you can configure access to your database by editing the properties file in the repository module).
Authentication: Spring Security (JWT).
Caching: Map.
