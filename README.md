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

## Using a client to call the service 
This project produces a support JAR containing client code for calling the service from Java.
This can be used from an external project by adding the following to the [pom.xml](pom.xml):
```xml
<!-- Used by the OpenAPI client -->
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>jackson-databind-nullable</artifactId>
    <version>0.2.2</version>
</dependency>

<dependency>
    <groupId>dk.kb.image-analysis</groupId>
    <artifactId>ds-image-analysis</artifactId>
    <version>1.0-SNAPSHOT</version>
    <type>jar</type>
    <classifier>classes</classifier>
    <!-- Do not perform transitive dependency resolving for the OpenAPI client -->
    <exclusions>
        <exclusion>
          <groupId>*</groupId>
          <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
after this the client can be created with
```java
    DsImageAnalysisClient imageAnalysisClient = new DsImageAnalysisClient("https://example.com/ds-image-analysis/v1");
```

Note that this client is a double client (analysis and manipulation).
See [DsImageAnalysisClient](dk/kb/image/util/DsImageAnalysisClient.java) for details. 

During development, a SNAPSHOT for the OpenAPI client can be installed locally by running
```shell
mvn install
```


See the file [DEVELOPER.md](DEVELOPER.md) for developer specific details and how to deploy to tomcat.
