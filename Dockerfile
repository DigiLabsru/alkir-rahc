# syntax=docker/dockerfile:experimental
FROM openjdk:17-jdk-alpine AS build

WORKDIR /workspace/app

COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:17-alpine

VOLUME /tmp

RUN apk add --no-cache curl

HEALTHCHECK --interval=20s --timeout=5s --retries=3 --start-period=20s \
  CMD curl --fail --silent http://127.0.0.1:8080/actuator/health | grep UP || echo 1

ARG DEPENDENCY=/workspace/app/build/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

ENTRYPOINT ["java","-cp","app:app/lib/*","ru.digilabs.alkir.rahc.RahcApplication"]
