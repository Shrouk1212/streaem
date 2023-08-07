# streaem

The Streaem Product Management Service is a stand-alone application that handles the ingestion, retrieval, and management of product data from an external JSON feed. 

## Features

1. On startup consumes an existing JSON feed from an external API and stores this feed in
memory.
2. Exposes a REST endpoint to retrieve the information for a single product from the feed
3. Exposes a REST endpoint to retrieve all products of a given category, optionally filtered
only by those in stock.
4. Exposes a REST endpoint to allow for updating of any of the fields of a single product
5. Exposes a REST endpoint to set the current stock level for a given product



## Technologies Used

- Java 11.
- Spring boot.
- Spring MVC.
- JUnit.


## Installation

To run the project you will need JDK 11.
Go to the pom folder location and run the follwoing command to generate the jar.

```bash
  mvn clean install
```
once the jar is created run the following command.
```bash
  java -jar target/demo-0.0.1-SNAPSHOT.jar
```
The API will run on the default port 8080.
you can use tools like Postman or Swaggar to access ths APIs.

## API Reference

for information you can visit Swaggar using the following link.
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#