docker build -t josecarloslopez/api-gateway:1.0 -f Dockerfile .
kubectl apply -f deployment.yaml

--para exponer la url
minikube service api-gateway