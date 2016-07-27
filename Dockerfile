FROM maven:3.3.3-jdk-8


WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
ADD src /code/src
ADD faults /code/faults
ADD flatfiles /code/flatfiles
ADD webroot /code/webroot
RUN ["mvn","-Dmaven.test.skip=true","package"]

EXPOSE 8080
CMD ["mvn", "exec:java"]