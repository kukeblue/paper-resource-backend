docker container stop paper-resource-backend
docker container rm  paper-resource-backend
docker build -t kukeblue/paper-resource-backend:v1 .
docker run -d -v /opt/paper:/opt/paper -p 8080:8080 --name="paper-resource-backend" kukeblue/paper-resource-backend:v1
docker push kukeblue/paper-resource-backend:v1






