# Instruction to Run the Sample

First do a clean build by running the following command
    
    mvn clean install

To deploy the Topology to the Storm cluster run the following command

    storm jar ./target/hello-world-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.sangeethlabs.storm.basic.Main3 192.168.59.103
    
To kill the Topology in the Storm cluster run the following command

    storm kill main-3-topology 192.168.59.103