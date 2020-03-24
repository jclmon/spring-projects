### Usar ConfigMap en lugar de Spring Cloud Config
* Arrancar minikube
´´´
minikube start --vm-driver=none --apiserver-ips 127.0.0.1 --apiserver-name localhost
´´´
* Comprobar que está funcionando coredns
´´´
kubectl -n kube-system get pods
´´´
* En caso de no estar funcionando
´´´
$ sudo firewall-cmd --permanent --zone=trusted --add-interface=docker0
$ sudo firewall-cmd --reload
$ sudo firewall-cmd --get-active-zones
$ sudo firewall-cmd --list-all --zone=trusted
$ sudo chown -R $USER $HOME/.kube $HOME/.minikube
$ minikube dashboard &
´´´
* Si tuviesemos que eliminar la configuracion para volverla a crear
´´´
minikube delete
rm -rf ~/.minikube
´´´

### Dockerfiles

* Dockerfile para auth service crear también para product service
´´´
FROM openjdk:8-jre-alpine
ENV APP_FILE auth-service-0.0.1-SNAPSHOT.jar
ENV APP_HOME /usr/apps
ENV PROFILE prod
EXPOSE 81
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$PROFILE -jar $APP_FILE"]
´´´

### Construir las imágenes, subirlas si se quieren tener en repositorio y desplegar con Kubernetess
´´´
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
´´´

### Crear usuario en BBDD Mongo

´´´
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
´´´
