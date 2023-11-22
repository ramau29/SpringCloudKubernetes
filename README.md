# SpringCloudKubernetes

Para crear el paquete (.jar) del msvc-usuarios.
Parado en la carpeta del msvc, ejecutar el comando ```./mvnw clean package -DskipTests```
Va a genera el .jar en la carpeta target.
Para ejecutar el servicio en la maquina local: ```java -jar ./taget/msvc-usuraios-0.0.1-SNAPSHOT.jar```

Construimos la imagen con el comando: ```docker build -t nombre-de-la-imagen .```
(. porque es el dir raiz, el mismo donde estamos parados, donde esta el archivo Dockefile)

Con el comando ```docker images``` vemos la imagen creada (en este caso, IMAGE ID = 3260a86e951e)
Con ```docker run 3260a86e951e``` va a ejecutar el contenedor, va a ejecutar el entrypoint y levantar la aplicacion

![img.png](img.png)

# Multi Stage Builder Pattern
uso de varios FROM para contruir la imagen en varias etapas (compilaciones)
"usar la imagen anterios para construir la siguiente"