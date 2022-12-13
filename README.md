# ds-image-analysis

This service provides image analysis capabilities. The service is capable of calculating dominant colors in a given image.
These calculations can be done in two colorspaces: RGB and OKlab. 

Developed and maintained by the Royal Danish Library.

## Requirements

* Maven 3                                  
* Java 11

## Build & run

Build with:
``` 
mvn package
```

Test webservice with:
```
mvn jetty:run
```

The default port is 9075 and the Swagger UI is available at <http://localhost:9075/ds-image-analysis/api/>, providing access to both the `v1` and the 
`devel` versions of the GUI. 

See the file [DEVELOPER.md](DEVELOPER.md) for developer specific details and how to deploy to tomcat.
