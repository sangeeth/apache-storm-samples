# Simple Samples using Apache Storm and Apache Kafka.

## Instructions to Setup the Environment in Mac OS X

First step is to install Docker with Boot2Docker. Please follow the instruction mentioned in  [http://docs.docker.com/installation/mac/](http://docs.docker.com/installation/mac/ "Instructions to setup Docker"). Once you believe you are done with the installation, please verify your installation by running the following commands from a Terminal.

    boot2docker init
    boot2docker start
    boot2docker shellinit
    
Now copy the Docker environment variables to your `~/.bash_profile` 

    export DOCKER_TLS_VERIFY=1
    export DOCKER_HOST=tcp://192.168.59.103:2376
    export DOCKER_CERT_PATH=/Users/sangeeth/.boot2docker/certs/boot2docker-vm

Then finally run the following command to deploy a Docker Hello World image to verify the Docker installation
 
    docker run hello-world 

If the output shows things are fine, then your Docker VM is ready for Storm and Kafka image. So run the following commands to setup a Storm+Kafka Cluster with Openfire XMPP Server. Few examples may use XMPP. 

    docker-compose up
    
This will take a while. Once done your VM is ready with Storm and Kafka cluster for various examples provided in this repository.

Install Apache Storm in your machine say at `/opt/apache/apache-storm-0.9.5`. Then add the following environment variables in your `~/.bash_profile`

    export STORM_HOME=/opt/apache/apache-storm-0.9.5
    export PATH=$STORM_HOME/bin:$PATH
    
Verify the setup:

  - Storm Supervisor Log: [http://192.168.59.103:8000/log?file=supervisor.log](http://192.168.59.103:8000/log?file=supervisor.log)
  - Storm UI: [http://192.168.59.103:8080/index.html](http://192.168.59.103:8080/index.html)
  - Openfire XMPP: [http://192.168.59.103:9090/](http://192.168.59.103:9090/)

## Troubleshooting

1. If you face the following error when you run `docker run hello-world`

        An error occurred trying to connect: Post https://192.168.59.103:2376/v1.19/containers/create: x509: certificate is valid for 127.0.0.1, 10.0.2.15, not 192.168.59.103

To get out of this problem, I did the following

     boot2docker down
     boot2docker delete
     rm -f ~/.boot2docker/certs/boot2docker-vm
     boot2docker init
     boot2docker start
     boot2docker shellinit
     docker run hello-world

For more details about this issue, please read [https://github.com/docker/machine/issues/531](https://github.com/docker/machine/issues/531 "Issue 531")


## For Further Readings

1. Installation Instruction for Mac OS X: [http://docs.docker.com/installation/mac/](http://docs.docker.com/installation/mac/)
2. Kafka Cluster Docker Image: [http://wurstmeister.github.io/kafka-docker/](http://wurstmeister.github.io/kafka-docker/)
3. Running Storm + Kafka on Docker:  [http://alvinhenrick.com/2014/08/18/apache-storm-and-kafka-cluster-with-docker/](http://alvinhenrick.com/2014/08/18/apache-storm-and-kafka-cluster-with-docker/)
