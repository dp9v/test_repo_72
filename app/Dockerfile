FROM gradle:8.2.0-jdk17

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app