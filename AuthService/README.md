1. How to start server?
```
mvn jetty:run
```
The default endpoint is localhost, and default port is 8080.

2. The following dependency and plugins are used, to simplify running the server using Jetty, and the program is built by java servlet so `javax.servlet-api` is included to support api functions.
```
    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.2</version>
            </plugin>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.14.v20181114</version>
            </plugin>
        </plugins>
    </build>
```