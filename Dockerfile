FROM maven:3-openjdk-17-slim AS builder
WORKDIR /buildApp
COPY ./pom.xml .
COPY ./src ./src
RUN --mount=type=cache,target=/root/.m2 --mount=type=cache,target=/opt/buildApp/target mvn install

FROM openjdk:17

ARG PROFILE
ARG ADDITIONAL_OPTS

ENV PROFILE=${PROFILE}
ENV ADDITIONAL_OPTS=${ADDITIONAL_OPTS}

WORKDIR /deployjava

COPY --from=builder /buildApp/target/accountsPayable-*.jar app.jar

SHELL ["/bin/sh", "-c"]

EXPOSE 8080
EXPOSE 5005

CMD java ${ADDITIONAL_OPTS} -jar app.jar --spring.profiles.active=${PROFILE}