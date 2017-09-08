# How to Run

This is a step-by-step guide how to run the example:

## Installation

* The example is implemented in Java. See
   https://www.java.com/en/download/help/download_options.xml . The
   examples need to be compiled so you need to install a JDK (Java
   Development Kit). A JRE (Java Runtime Environment) is not
   sufficient. After the installation you should be able to execute
   `java` and `javac` on the command line.

* Maven is needed to build the examples. See
  https://maven.apache.org/download.cgi for installation . You should be
  able to execute `mvn`on the command line after the installation.

* The example run in Docker Containers. You need to install Docker
  Community Edition, see https://www.docker.com/community-edition/
  . You should be able to run `docker` after the installation.

* The example need a lot of RAM. You should configure Docker to use 4
  GB of RAM. Otherwise Docker containers might be killed due to lack
  of RAM. On Windows and macOS you can find the RAM setting in the
  Docker application under Preferences/ Advanced.
  
* After installing Docker you should also be able to run
  `docker-compose`. If this is not possible, you might need to install
  it separately. See https://docs.docker.com/compose/install/ .

## Build

Change to the directory `scs-demo-jquery` and run `mvn clean
package`. This will take a while:

```
[~/SCS-jQuery/scs-demo-jquery]mvn clean package
...
[INFO] 
[INFO] --- maven-jar-plugin:2.5:jar (default-jar) @ scs-demo-order-jquery ---
[INFO] Building jar: /Users/wolff/SCS-jQuery/scs-demo-jquery/scs-demo-order-jquery/target/scs-demo-order-jquery-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:1.3.5.RELEASE:repackage (default) @ scs-demo-order-jquery ---
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] scs-demo-jquery .................................... SUCCESS [  0.948 s]
[INFO] scs-demo-catalog-jquery ............................ SUCCESS [ 17.129 s]
[INFO] scs-demo-order-jquery .............................. SUCCESS [ 10.332 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 28.767 s
[INFO] Finished at: 2017-09-08T15:54:39+02:00
[INFO] Final Memory: 36M/513M
[INFO] ------------------------------------------------------------------------
```

If this does not work:

* Ensure that `settings.xml` in the directory `.m2` in your home
directory contains no configuration for a specific Maven repo. If in
doubt: delete the file.

* The tests use some ports on the local machine. Make sure that no
server runs in the background.

* Skip the tests: `mvn clean package package -Dmaven.test.skip=true`.

* In rare cases dependencies might not be downloaded correctly. In
  that case: Remove the directory `repository` in the directory `.m2`
  in your home directory. Note that this means all dependencies will
  be downloaded again.

## Run the containers

First you need to build the Docker images. Change to the directory
`docker` and run `docker-compose build`. This will download some base
images, install software into Docker images and will therefore take
its time:

```
[~/SCS-jQuery/docker]docker-compose build 
...
Step 10/10 : CMD /start
 ---> Running in aed1c44da50b
 ---> 870158f27c4b
Removing intermediate container aed1c44da50b
Successfully built 870158f27c4b
Successfully tagged scsjquery_varnish:latest
```

Afterwards the Docker images should have been created. They have the prefix
`scsjquery`:

```
[~/SCS-jQuery/docker]docker images
REPOSITORY                                      TAG                 IMAGE ID            CREATED              SIZE
scsjquery_varnish                                       latest              870158f27c4b        44 seconds ago      328MB
scsjquery_order                                         latest              eea94180172c        48 seconds ago      205MB
scsjquery_catalog                                       latest              7fc6bcfc3a06        50 seconds ago      205MB
```

Now you can start the containers using `docker-compose up -d`. The
`-d` option means that the containers will be started in the
background and won't output their stdout to the command line:

```
[~/SCS-jQuery/docker]docker-compose up -d
Creating network "scsjquery_default" with the default driver
Creating scsjquery_catalog_1 ... 
Creating scsjquery_order_1 ... 
Creating scsjquery_order_1
Creating scsjquery_catalog_1 ... done
Creating scsjquery_varnish_1 ... 
Creating scsjquery_varnish_1 ... done
```

Check wether all containers are running:

```
[~/SCS-jQuery/docker]docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
b71d21cd324e        scsjquery_varnish   "/start"                 19 seconds ago      Up 17 seconds       0.0.0.0:8081->8080/tcp   scsjquery_varnish_1
c3cb6750323b        scsjquery_catalog   "/bin/sh -c '/usr/..."   20 seconds ago      Up 18 seconds       8080/tcp                 scsjquery_catalog_1
f062748bfa7f        scsjquery_order     "/bin/sh -c '/usr/..."   20 seconds ago      Up 18 seconds       8080/tcp                 scsjquery_order_1
```

`docker ps -a`  also shows the terminated Docker containers. That is
useful to see Docker containers that crashed rigth after they started.

If one of the containers is not running, you can look at its logs using
e.g.  `docker logs scsjquery_order_1`. The name of the container is
given in the last column of the output of `docker ps`. Looking at the
logs even works after the container has been
terminated. If the log says that the container has been `killed`, you
need to increase the RAM assigned to Docker to e.g. 4GB. On Windows
and macOS you can find the RAM setting in the Docker application under
Preferences/ Advanced.
  
If you need to do more trouble shooting open a shell in the container
using e.g. `docker exec -it scsjquery_order_1 /bin/sh` or execute
command using `docker exec scsjquery_order_1 /bin/ls`.

You can open the website of the example at http://localhost:8081/ .

You can terminate all containers using `docker-compose down`.
