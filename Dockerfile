FROM openjdk:22 as build

WORKDIR /app

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:22-slim

ARG DEPENDENCY=/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib	/app/lib
COPY --from=build ${DEPENDENCY}/META-INF	/app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes	/app

RUN mkdir -p ./src/main/resources/

COPY src/main/resources/epicycloid.db ./src/main/resources/

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "org.epicycloide_back.epicycloide_back.EpicycloideBackApplication"]
