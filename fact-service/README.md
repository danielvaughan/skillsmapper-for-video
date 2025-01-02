# Fact Service

This is the code for the _Fact Service_ used in [Chapter 7](../chapters/ch07.asciidoc) of the book [Cloud Native Development with Google Cloud](https://www.oreilly.com/library/view/cloud-native-development/9781098145071/).

## Building

### Authenticating with the Repo

```shell
gcloud auth configure-docker
```

```shell
export DOCKER_REPO=europe-north1-docker.pkg.dev/home-lab-k8s/fact-service
```

### Building with Docker

```shell
docker build -t $DOCKER_REPO/fact-service-docker .
```

```shell
docker push $DOCKER_REPO/fact-service-docker
```

### Building with Buildpacks

```shell
pack build $DOCKER_REPO/fact-service-pack \
  --builder gcr.io/buildpacks/builder:google-22 \
  --path .
```

### Building with Jib

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.4</version>
    <configuration>
        <to>
            <image>$DOCKER_REPO/fact-service-jib</image>
        </to>
    </configuration>
</plugin>
```

```shell
mvn compile jib:build
```

Docker build only

```shell
mvn jib:dockerBuild
```
## Running

### Running with Maven

```shell
mvn clean install spring-boot:run
```

### Running with Spring Boot Dev

```shell

```

### Running with Skaffold

gcloud auth configure-docker europe-north1-docker.pkg.dev

export DOCKER_REPO=danielvaughan

export SKAFFOLD_DEFAULT_REPO=$DOCKER_REPO

https://github.com/GoogleContainerTools/skaffold/tree/main/examples/jib

## Running

### With Docker Compose


## Testing

### Local Unit tests with mocks

Using H2 for Development and Unit Testing
Mock PubSub (mokito) (https://stackoverflow.com/questions/31739455/how-to-do-junit-test-with-google-cloud-pub-sub)
https://medium.com/@claudiorauso/local-testing-spring-gcp-pub-sub-e2028b69d8e5
Firebase pubsub emulator

## Local integration tests

Using Testcontainers for Integration Testing
* PostgeSQL container
* PubSub Emulator Container

## Local system tests

Standup in KinD and run a jMeter-type test
* PostgeSQL container
* PubSub Emulator Container

### With Docker Compose and TestContainers

### With Skaffold and Containers

## With Pub/Sub Emulator

* https://java.testcontainers.org/modules/gcloud/
* https://github.com/saturnism/testcontainers-gcloud-examples/blob/main/springboot/pubsub-example/src/test/java/com/example/springboot/pubsub/PubSubIntegrationTests.java

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>gcloud</artifactId>
    <version>1.20.4</version>
    <scope>test</scope>
</dependency>
```

### Skaffold Post Deploy Hook

### Skaffold Jobs

## Deploying

### To Cloud Run

### To GKE Autopoliot

