docker build -t josecarloslopez/postgres-user:1.0 -f Dockerfile .


docker run --rm --name user-postgresdb-container -e -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data josecarloslopez/postgres-user:1.0
kubectl apply -f deployment.yaml