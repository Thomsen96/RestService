## run the docker container with the command below. where the entry point especially is important.
#docker run --name tokenservice_client --network=deploy_app_network --entrypoint /home/app/run.sh tokenservice_client:latest

FROM maven:3.8.4-jdk-11-slim
ENV IN_DOCKER_ENV=True
## Get source code into the container
COPY features /home/app/features
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app/

## Create a run file on the container that can start the test, in "quiet mode"
RUN echo "#!/bin/bash \n mvn --no-transfer-progress -f /home/app/pom.xml test" > /home/app/run.sh

## Make scripte executeable
RUN chmod +x /home/app/run.sh
