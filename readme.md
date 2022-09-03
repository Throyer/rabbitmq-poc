## How to use
> 🚨 create `environment` file and add permission to execute scripts
>
> ```shell
> cp .docker/.env.example .docker/.env && chmod -R +x .docker/scripts
> ```

  - start containers
    ```
    .docker/scripts/develop-compose up -d
    ```

  - show logs
    ```
    .docker/scripts/develop-compose logs -f service-a service-b
    ```

  - send user to `Service A`
    ```http
    POST http://localhost:8081/api/v1/users
    ```
    ```json
    {
      "name": "Jubileu da silva"
    }
    ```
    ```curl
    curl --location --request POST 'http://localhost:8081/api/v1/users' \
    --header 'Content-Type: application/json' \
    --data-raw '{
      "name": "Wade Schneider"
    }'
    ```
  
  - get users on `Service B`
    ```http
    GET http://localhost:8082/api/v1/users
    ```
    ```
    curl --location --request GET 'http://localhost:8082/api/v1/users'
    ```