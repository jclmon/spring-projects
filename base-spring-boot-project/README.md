# base-spring-boot-project

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

* start ``` c:\mongodb\bin\mongod --config c:\mongodb\mongo.config``` 

* adm console ```c:\mongodb\bin\mongo``` 

* bbdd change ```use test``` 

* init with service http://localhost:8080/api/auth-service/roles-permissions-setup/init

* collections ```show collections``` 

* insert data
``` 
db.User.insert({ "_id" : "1", "status" : "1", "username" : "testuser1", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser1@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "_id" : "2", "status" : "2", "username" : "testuser2", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser2@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "_id" : "3", "status" : "3", "username" : "testuser3", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser3@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "_id" : "4", "status" : "4", "username" : "testuser4", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser4@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })
db.User.insert({ "_id" : "5", "status" : "5", "username" : "testuser5", "password": "$2a$10$MK2.vNiBHToVNwqJl9P2luHjbTMUkNi.fxKJ0b/u/dEFJY0taGjm6", "email" : "testuser5@testdomain.com", "permissions" : [{"code" : "USER_ADMIN"}] })

db.Product.insert({ "id" : "1", "sellerId" : "testuser1", "name": "Huami Pace", "price" : 30 })
db.Product.insert({ "id" : "2", "sellerId" : "testuser1", "name": "Huami Amazfit", "price" : 80.59 })
``` 
* select ```db.User.find().pretty()```

* db.User.find( { "username" : "testuser3" } )