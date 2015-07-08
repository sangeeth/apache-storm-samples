#!/bin/bash
docker run --link apachestormsamples_zookeeper_1:zk -i -t wurstmeister/kafka:0.8.2.1 /bin/bash
