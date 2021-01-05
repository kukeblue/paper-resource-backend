docker container stop paper-resource-backend
docker container rm  paper-resource-backend
docker build -t kukeblue/paper-resource-backend:v1 .
docker run -d -p 8080:8080 --name="paper-resource-backend" paper-resource-backend:v1


