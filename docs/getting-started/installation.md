# Installation

Adding TorpedoQuery to your project is straightforward. Choose your preferred dependency management tool:

## Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.torpedoquery</groupId>
    <artifactId>org.torpedoquery</artifactId>
    <version>2.5.2</version>
</dependency>
```

## Gradle

Add this to your `build.gradle`:

```groovy
implementation 'org.torpedoquery:org.torpedoquery:2.5.2'
```

!!! tip
    Always check [Maven Central](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery) for the latest version.

## Manual Download

You can download the JAR file directly from [Maven Central](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery).

## Requirements

TorpedoQuery requires:

- Java 8 or higher
- A JPA provider (like Hibernate, EclipseLink, etc.)

## Verifying Installation

To verify that TorpedoQuery is properly installed, create a simple class with the following code:

```java
import static org.torpedoquery.jpa.Torpedo.*;

// Your entity class
public class MyEntity {
    private String name;
    // getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

// Test code
public class TorpedoQueryTest {
    public void testQuery() {
        MyEntity entity = from(MyEntity.class);
        Query<MyEntity> query = select(entity);
        String queryString = query.getQuery();
        System.out.println(queryString);
        // Should print: select myEntity_0 from MyEntity myEntity_0
    }
}
```

If everything is set up correctly, you should be able to build this code without errors.

## Next Steps

Once you've successfully installed TorpedoQuery, check out the [Quick Start Guide](quick-start.md) to begin creating queries.