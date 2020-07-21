FROM maven as build
COPY pom.xml /tmp/sort/
COPY src/ /tmp/sort/src
WORKDIR /tmp/sort
RUN mvn package

FROM openjdk:11-jre
COPY --from=build /tmp/sort/target/sort-exercise*.jar /srv/sort-exercise.jar
COPY docker_files/* /srv/
RUN chmod +x /srv/run.sh
ENTRYPOINT ["/bin/bash", "-c", "/srv/run.sh \"$@\"", "--"]
