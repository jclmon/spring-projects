# Ejecución del proyecto

## Postgres

Con la base de datos Postgres arrancada en el puerto 5432, utilizar instalación clásica o virtualizada con dockerfile o docker compose.

### Dockerfile

Configuración de la base de datos fichero custom-user.sh

```
#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER docker PASSWORD root;
        CREATE DATABASE user;
        GRANT ALL PRIVILEGES ON DATABASE docker TO user;
EOSQL
```

#### Ejecución de Dockerfile

##### Construir el fichero:

```
FROM postgres
ENV POSTGRES_DB user
ENV POSTGRES_USER docker
ENV POSTGRES_PASSWORD root
COPY custom-user.sh /docker-entrypoint-initdb.d/
```

##### Construir la imagen:

```
docker build -t josecarloslopez/postgres-user:1.0 -f Dockerfile .
```

##### Ejecutar con mapeo de volúmenes:

```
docker run --rm --name user-postgresdb-container -e -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data josecarloslopez/postgres-user:1.0
```

### Docker compose

```
# A Docker Compose must always start with the version tag.
# We use '3' because it's the last version.
version: '3'

# You should know that Docker Compose works with services.
# 1 service = 1 container.
# For example, a service, a server, a client, a database...
# We use the keyword 'services' to start to create services.
services:
  # The name of our service is "database"
  # but you can use the name of your choice.
  # Note: This may change the commands you are going to use a little bit.
  database:
    # Official Postgres image from DockerHub (we use the last version)
    image: 'postgres:latest'

    # By default, a Postgres database is running on the 5432 port.
    # If we want to access the database from our computer (outside the container),
    # we must share the port with our computer's port.
    # The syntax is [port we want on our machine]:[port we want to retrieve in the container]
    # Note: You are free to change your computer's port,
    # but take into consideration that it will change the way
    # you are connecting to your database.
    ports:
      - 5432:5432

    environment:
      POSTGRES_USER: docker # The PostgreSQL user (useful to connect to the database)
      POSTGRES_PASSWORD: root # The PostgreSQL password (useful to connect to the database)
      POSTGRES_DB: user # The PostgreSQL default database (automatically created at first launch)
      
    # The `volumes` tag allows us to share a folder with our container.
    # Its syntax is as follows: [folder path on our machine]:[folder path to retrieve in the container]
    volumes:
      # In this example, we share the folder `db-data` in our root repository, with the default PostgreSQL data path.
      # It means that every time the repository is modifying the data inside
      # of `/var/lib/postgresql/data/`, automatically the change will appear in `db-data`.
      # You don't need to create the `db-data` folder. Docker Compose will do it for you.
      - ./db-data/:/var/lib/postgresql/data/
```



## Servicios

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

Arrancar product-service

```
$ cd product-service
$ mvn clean package
$ java -jar target\product-service-0.0.1-SNAPSHOT.jar
```



### Para entorno producción construir las imágenes, subirlas si se quieren tener en repositorio y desplegar con Kubernetess

```
$ cd auth-service/postgresql
$ kubectl apply -f deployment.yaml
$ cd ..
$ cd ..
$ cd product-service/postgresql
$ kubectl apply -f deployment.yaml
$ cd ..
$ cd ..
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
$ cd ..
$ cd product-service
$ docker build -t josecarloslopez/product-service:1.0 -f Dockerfile .
$ docker push josecarloslopez/product-service:1.0
$ kubectl apply -f deployment.yaml
```

Es necesario exponer hacia afuera el servicio de api-gateway para poder acceder a él, para ello en minikube:

```
PS C:\Users\Admin> minikube service api-gateway
|-----------|-------------|-------------|----------------------------|
| NAMESPACE |    NAME     | TARGET PORT |            URL             |
|-----------|-------------|-------------|----------------------------|
| default   | api-gateway |          80 | http://192.168.2.153:31234 |
|-----------|-------------|-------------|----------------------------|
```

En este caso se despliegan dos bases de datos totalmente independientes una para usuarios y otra para productos, para ver los ficheros de las dos bases de datos en minikube:

```
PS C:\Users\Admin> minikube ssh
                         _             _
            _         _ ( )           ( )
  ___ ___  (_)  ___  (_)| |/')  _   _ | |_      __
/' _ ` _ `\| |/' _ `\| || , <  ( ) ( )| '_`\  /'__`\
| ( ) ( ) || || ( ) || || |\`\ | (_) || |_) )(  ___/
(_) (_) (_)(_)(_) (_)(_)(_) (_)`\___/'(_,__/'`\____)

$ ls /
bin   dev  home  lib    libexec  media  opt   root  sbin  sys  usr
data  etc  init  lib64  linuxrc  mnt    proc  run   srv   tmp  var
$ ls /data
pgdata
$ cd /data/pgdata
$ ls
product  user
```

