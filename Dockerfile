FROM gradle:4.10-jdk8

USER root

COPY . /data
WORKDIR /data

# Downloads dependencies, compiles code and builds .jar file. Will not run tests.
RUN gradle bootJar && mv build/libs/makordid-*.jar api.jar

CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "api.jar"]