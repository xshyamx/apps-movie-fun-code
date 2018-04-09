# Movie Fun!

Smoke Tests require server running on port 8080 by default.

## Build WAR ignoring Smoke Tests

```
$ mvn clean package -DskipTests -Dmaven.test.skip=true
```

## Run Smoke Tests against specific URL

```
$ MOVIE_FUN_URL=http://moviefun.example.com mvn test
```

## Run the spring boot application

1. Start minio
    ```sh
    minio server ~/shared
    ```
2. Export the `MINIO_ACCESS_KEY` and `MINIO_SECRET_ACCESS_KEY` from the output of the previous command
    ```sh
    export MINIO_ACCESS_KEY=VALUE_FROM_PREVIOUS_COMMAND
    export MINIO_SECRET_ACCESS_KEY=VALUE_FROM_PREVIOUS_COMMAND
    ```
3. Setup the `VCAP_SERVICES` environment variable
    ```sh
    export VCAP_SERVICES="{\"aws-s3\": [{\"credentials\": {\"access_key_id\": \"${MINIO_ACCESS_KEY:?missing}\", \"bucket\": \"movie-fun-course\", \"secret_access_key\": \"${MINIO_SECRET_ACCESS_KEY:?missing}\"}, \"label\": \"aws-s3\", \"name\": \"moviefun-s3\"}],\"p-mysql\": [{\"credentials\": {\"jdbcUrl\": \"jdbc:mysql://127.0.0.1:3306/movies?user=root\"}, \"name\": \"movies-mysql\"}]}"
    ```
4. Run spring boot
    ```sh
    mvn spring-boot:run
    ```
