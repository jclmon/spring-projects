### Ejecución del proyecto

Arrancar mongodb y crear la base de datos, insertar el primer usuario para pruebas:

```
C:\mongodb\bin>mongo
MongoDB shell version: 2.6.11
connecting to: test
> use microservices
switched to db microservices
> show collections
> db.User.insert({ "username" : "testuser1", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser1@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"},{"code" : "USER_SELLER"}] })
WriteResult({ "nInserted" : 1 })
```

Instalación de common-lib

```
$ cd common-lib
$ mvn clean install
```

Arrancar EUREKA discovery-server

```
$ cd discovery-server
$ mvn clean package
$ java -jar target\discovery-server-0.0.1-SNAPSHOT.jar
```

Arrancar API api-gateway

```
$ cd api-gateway
$ mvn clean package
$ java -jar target\api-gateway-0.0.1-SNAPSHOT.jar
```

Arrancar auth-service

```
$ cd auth-service
$ mvn clean package
$ java -jar target\auth-service-0.0.1-SNAPSHOT.jar
```

run product-service

```
$ cd product-service
$ mvn clean package
$ java -jar target\product-service-0.0.1-SNAPSHOT.jar
```

Inicialización de roles y permisos en auth-service
POST http://localhost:8080/api/auth-service/roles-permissions-setup/init

### Para entorno producción construir las imágenes, subirlas si se quieren tener en repositorio y desplegar con Kubernetess

```
$ cd discovery-server
$ docker build -t josecarloslopez/discovery-server:1.0 -f Dockerfile .
$ docker push josecarloslopez/discovery-server:1.0
$ kubectl apply -f deployment.yaml
$ cd ..
$ cd api-gateway
$ docker build -t josecarloslopez/api-gateway:1.0 -f Dockerfile .
$ docker push josecarloslopez/api-gateway:1.0
$ kubectl apply -f deployment.yaml
$ cd ..
$ cd auth-service
$ docker build -t josecarloslopez/auth-service:1.0 -f Dockerfile .
$ docker push josecarloslopez/auth-service:1.0
$ kubectl apply -f deployment.yaml
$ cd mongo
$ docker build -t josecarloslopez/mongodb-microservicios:1.0 -f Dockerfile .
$ kubectl apply -f deployment.yaml
$ cd ..
$ cd ..
$ cd product-service
$ docker build -t josecarloslopez/product-service:1.0 -f Dockerfile .
$ docker push josecarloslopez/product-service:1.0
$ kubectl apply -f deployment.yaml
```

Inicialización de roles y permisos en auth-service
POST http://192.168.2.101:31234/api/auth-service/roles-permissions-setup/init

### Para kubernetes crear usuario en mongodb

```
kubectl -ti exec mongodb-5558574d46-jp2fz sh
$ mongo
> use admin
switched to db admin
> db.auth("root", "example")
> show databases
admin          0.000GB
config         0.000GB
local          0.000GB
microservices  0.000GB
> use microservices
switched to db microservices
> db.User.insert({ "username" : "testuser1", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser1@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"},{"code" : "USER_SELLER"}] })
WriteResult({ "nInserted" : 1 })
> show collections
Permission
Role
User
```

