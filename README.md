Arquitectura básica basada en MDL, Couchbase y Vert.x 

-Para generar una imagen con docker, desde la raíz ejecutar:

	docker build -t <username>/arq-example .

-Para crear el contenedor de la imagen anterior basta ejecutar:

	docker run -d -p 8080:8080 --name arq-container <username>/arq-example


