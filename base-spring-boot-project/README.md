# microservices-sample-project

### Prerequisities
  * JDK 1.8.X
  * Maven 3.3.X
  * MongoDB
  
### MongoDB

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

* start ``` C:\mongodb\bin\mongod --config C:\mongodb\mongo.config``` 

* adm console ```mongo``` 

* bbdd change ```use auth-service-db``` 

* collections ```show collections``` 

* insert data
``` 
db.User.insert({ "username" : "testuser1", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser1@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "username" : "testuser2", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser2@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "username" : "testuser3", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser3@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "username" : "testuser4", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser4@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "username" : "testuser5", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser5@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.Product.insert({ "id" : "1", "sellerId" : "testuser1", "name": "Huami Pace", "price" : 30 })
db.Product.insert({ "id" : "2", "sellerId" : "testuser1", "name": "Huami Amazfit", "price" : 80.59 })
newstuff = [{ "username" : "testuser2", "email" : "testuser2@testdomain.com" }, { "username" : "testuser3", "email" : "testuser3@testdomain.com" }]
db.User.insert(newstuff);
``` 
* select ```db.User.find().pretty()``` 

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