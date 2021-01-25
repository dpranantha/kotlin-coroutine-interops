An example on how Kotlin coroutine interoperable with Java and Spring Reactor.

Java codebase:
1. Build and run unit tests: `mvn clean install`
2. Build and run both unit tests and integration tests: `mvn clean install -Pintegration`
3. Run the application via Maven: `mvn exec:java -Dexec.mainClass="com.dpranantha.coroutineinterops.CoroutineInteropsApplication"`
4. Swagger OpenAPI 3 GUI: `http://localhost:8080/internal`
