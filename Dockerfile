FROM maven as build
COPY pom.xml /tmp/sort/
COPY src/ /tmp/sort/src
WORKDIR /tmp/sort
RUN mvn test package

FROM openjdk:11-jre as  system_test
COPY --from=build /tmp/sort/target/sort-exercise*.jar /srv/sort-exercise.jar
COPY docker_files/tests /tmp/tests
COPY docker_files/test.sh /tmp/test.sh
RUN chmod +x /tmp/test.sh && /tmp/test.sh /tmp/tests

FROM openjdk:11-jre
COPY --from=system_test /srv/sort-exercise.jar /srv/sort-exercise.jar
COPY docker_files/run.sh /srv/
RUN chmod +x /srv/run.sh
ENTRYPOINT ["/bin/bash", "-c", "/srv/run.sh \"$@\"", "--"]
