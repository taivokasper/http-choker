# Http-Choker

Its main aim is to choke any http server with long lasting POST, PUT etc requests with payload.
 
It does so by doing many simultaneous requests to the given url and gradually flushing a random byte to it to avoid a timeout. 
Eventually it will occupy all available threads on the server that will all be waiting for content to read from the input stream.

It is meant to be used in development scenarios.

# Instructions

Build using Gradle
> ./gradlew shadowJar

Run with Java 8  
> java -Xmx3G -jar build/libs/http-choker-0.1-SNAPSHOT-all.jar 100000 100 POST http://localhost:8080/api/doSomething

1. First argument 100000 is the number of threads to use
2. Second argument 100 is the number of milliseconds to sleep between writing a byte
3. Third argument is the request type (POST, PUT, PATCH, etc)
4. Forth argument is the url

# Contributions

All contributions are welcome through pull requests.

# Disclaimer

1. Use at your own risk!
2. You(!) are responsible for any harm done with the software not the author!
3. Read 1. and 2.