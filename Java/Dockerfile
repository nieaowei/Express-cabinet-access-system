FROM maven:3.6.3-jdk-8

RUN mkdir /usr/local/ecas

ADD ./Java /usr/local/ecas

WORKDIR /usr/local/ecas

RUN mvn package -Dmaven.test.skip=true

ENTRYPOINT ["java","-jar","./target/demo-0.0.1-SNAPSHOT.jar"]