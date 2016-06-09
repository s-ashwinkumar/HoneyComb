FROM jeanblanchard/busybox-java:8
EXPOSE 8080
ADD FaultInjector.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
