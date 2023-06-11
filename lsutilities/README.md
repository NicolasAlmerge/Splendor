# Lobby Service Utilities

This project covers 100% of the LS api specification for COMP 361.

To include it in the project, your `pom.xml` file should have this structure:

```xml
<project>
    
    ...
    
    <repositories>
        <repository>
            <id>lsutilities</id>
            <url>https://cs.mcgill.ca/~nalmer/comp361repo/lsutilities/</url>
        </repository>
    </repositories>

    <dependencies>
        
        ...

        <dependency>
            <groupId>ca.mcgill.nalmer</groupId>
            <artifactId>lsutilities</artifactId>
            <version>1.0</version>
        </dependency>
        
        ...
        
    </dependencies>
    
    ...
    
</project>
```

The documentation is found inside the [docs](../docs) folder.
