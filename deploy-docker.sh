sh build.sh
ssh root@chome '
  docker container stop paper-resource-backend;
  docker container rm  paper-resource-backend;
  docker pull kukeblue/paper-resource-backend:v1;
  docker run -d -p 8080:8080 --name="paper-resource-backend" kukeblue/paper-resource-backend:v1
'

