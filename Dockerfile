FROM amazoncorretto:21

COPY out/artifacts/library_management_app_jar/library-management-app.jar /app/library-management-app.jar

WORKDIR /app
ENTRYPOINT ["java", "-jar", "library-management-app.jar"]
