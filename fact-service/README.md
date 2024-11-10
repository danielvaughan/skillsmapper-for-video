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

## Running

### Running with Maven

```shell
mvn clean install spring-boot:run
```

### Running with Spring Boot Dev

```shell

```

### Running with Skaffold

## Integrating

### With Maven and h2 and Mocks

### With Docker Compose and TestContainers

### With Skaffold and Containers

## Deploying

### To Cloud Run

### To GKE Autopoliot
