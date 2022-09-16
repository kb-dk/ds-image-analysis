# ds-image-analysis

This service provides image analysis capabilities.

Developed and maintained by the Royal Danish Library.

## Requirements

* Maven 3                                  
* Java 11

## Setup

**PostgreSQL database creation, Solr installation etc. goes here**

## Build & run

Build with
``` 
mvn package
```

Test the webservice with
```
mvn jetty:run
```

The default port is 8080 and the default Hello World service can be accessed at
<http://localhost:8080/ds-image-analysis/v1/hello>

The Swagger UI is available at <http://localhost:8080/ds-image-analysis/api/>, providing access to both the `v1` and the 
`devel` versions of the GUI. 

See the file [DEVELOPER.md](DEVELOPER.md) for developer specific details and how to deploy to tomcat.
