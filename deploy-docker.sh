sh build.sh
ssh root@chome '
  docker container stop paper-resource-backend;
  docker container rm  paper-resource-backend;
  docker pull kukeblue/paper-resource-backend:v1;
  docker run -d -v /opt/paper:/opt/paper -v /opt/paper_preview:/opt/paper_preview -p 8080:8080 --name="paper-resource-backend" kukeblue/paper-resource-backend:v1
'
