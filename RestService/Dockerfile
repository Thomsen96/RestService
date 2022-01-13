# Build with: docker build --tag tokenservice_server .
# Test with: docker run -ti -p 8080:8080 tokenservice_server

FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/token-service-1.0.0-runner.jar /usr/src/
WORKDIR /usr/src/
CMD java -Dquarkus.http.port=8181 -Xmx64m -jar token-service-1.0.0-runner.jar
