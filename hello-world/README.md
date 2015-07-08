# Instruction to Run the Sample

First do a clean build by running the following command
    
    mvn clean install
    
## Running Main3

To deploy the Topology to the Storm cluster run the following command

    storm jar ./target/hello-world-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.sangeethlabs.storm.basic.Main3 192.168.59.103
    
To kill the Topology in the Storm cluster run the following command

    storm kill main-3-topology 192.168.59.103
    
## Running Main4

Before deploying the Storm Topology, create the Topic in Kafka to which the ``WordsKafkaProducer`` can post the words. In order to create a Topic in Kafka, run the following command to connect to the Docker shell, where Kafka is running.

    docker run --link apachestormsamples_zookeeper_1:zk -i -t wurstmeister/kafka:0.8.2.1 /bin/bash
    
In the Docker shell run the following command to create the topic named ``words-stream``.

    $KAFKA_HOME/bin/kafka-topics.sh --create --topic words-stream --partitions 2 --zookeeper $ZK_PORT_2181_TCP_ADDR --replication-factor 1
   
A sample Terminal output is as shown below.

    SANGEETH-MAC:apache-storm-samples sangeeth$ docker run --link apachestormsamples_zookeeper_1:zk -i -t wurstmeister/kafka:0.8.2.1 /bin/bash
    root@c66a1e16dba4:/# $KAFKA_HOME/bin/kafka-topics.sh --create --topic words-stream --partitions 2 --zookeeper $ZK_PORT_2181_TCP_ADDR --replication-factor 1
    Created topic "words-stream".
    root@c66a1e16dba4:/# exit
    exit
    SANGEETH-MAC:apache-storm-samples sangeeth$ 

Now you are ready to deploy the Main4 Topology to Storm Cluster using the following command

    storm jar ./target/hello-world-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.sangeethlabs.storm.basic.Main4 192.168.59.103 
    
## Running ``WordsKafkaProducer``

In order to validate whether the producer is working running the following command in Docker shell

    $KAFKA_HOME/bin/kafka-console-consumer.sh --zookeeper $ZK_PORT_2181_TCP_ADDR --topic words-stream --from-beginning