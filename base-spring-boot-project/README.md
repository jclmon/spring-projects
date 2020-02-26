# base-spring-boot-project

## MongoDB

Mongo config C:\mongodb\mongo.config
``` 
##store data here
storage:
  dbPath: C:\mongodb\data

##all output go here
systemLog:
  destination: file
  path: C:\mongodb\log\mongo.log
  logAppend: true

##authorization
security:
    authorization: "disabled"
``` 

* start ``` c:\mongodb\bin\mongod --config c:\mongodb\mongo.config``` 

* adm console ```c:\mongodb\bin\mongo``` 

* bbdd change ```use db_products``` 

* init with service http://localhost:8080/api/auth-service/roles-permissions-setup/init

* collections ```show collections``` 

* insert data
``` 
db.User.insert({ "_id" : "1", "status" : "1", "username" : "testuser1", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser1@testdomain.com", "permissions" : [{"code" : "USER_SELLER"}] })
db.User.insert({ "_id" : "2", "status" : "2", "username" : "testuser2", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser2@testdomain.com", "permissions" : [{"code" : "USER_SELLER"}] })
db.Product.insert({ "_id" : "1", "sellerId" : "testuser1", "name": "Huami Pace", "price" : 30 })
db.Product.insert({ "_id" : "2", "sellerId" : "testuser1", "name": "Huami Amazfit", "price" : 80.59 })
``` 
* select ```db.User.find().pretty()```

* db.User.find( { "username" : "testuser3" } )

## Run project

### install common-lib
  * go inside to the common-lib project folder
  * then run ``` mvn clean install ```
  
### run discovery-server
  * go inside to the discovery-server project folder
  * then run ``` mvn clean package ```
  * then run ``` java -jar target\discovery-server-0.0.1-SNAPSHOT.jar ```
  
### run api-gateway
  * go inside to the api-gateway project folder
  * then run ``` mvn clean package ```
  * then run ``` java -jar target\api-gateway-0.0.1-SNAPSHOT.jar ```
  
### run auth-service
  * go inside to the auth-service project folder
  * then run ``` mvn clean package ```
  * then run ``` java -jar target\auth-service-0.0.1-SNAPSHOT.jar ```
  
### auth-service init
  * make request for the auth-service for setup roles and permissions.
  * ``` POST /api/auth-service/roles-permissions-setup/init ```
  
### run product-service
  * go inside to the product-service project folder
  * then run ``` mvn clean package ```
  * then run ``` java -jar target\product-service-0.0.1-SNAPSHOT.jar ```