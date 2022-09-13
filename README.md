# TweetAppBackend1
                                                       TWEET APP PROJECT 

1.This project uses MONGODB CRUD operations 

2.Implements registration of user for new users. 

3.Included login method with username and password including JWT token, whenever user try to login, new token will be generated. 

4.When a user tries to login and forgets password, in that case user can reset password. 

5.Retrieve all users and tweet data. 

6.Can be able to delete tweet, like tweet, reply tweet and edit tweet by username and tweet id. 

7.Retrieve all tweets by username and search user data by username. 

8.Implemented swagger for tweep app API calls. 

9.ELK stack is also used in this project. 

10.This project using Kafka for messaging. 

Steps to be followed to run application 

                                                      Kafka: 

Open command prompt and start the zookeeper using below command 

C:\kafka_2.12-2.4.1\bin\windows\zookeeper-server-start.bat C:\kafka_2.12 2.4.1\config\zookeeper.properties 

Again, open new command prompt and start running kafka server using below command. 

C:\kafka_2.12-2.4.1\bin\windows\kafka-server-start.bat C:\kafka_2.12 2.4.1\config\server.properties 

Execute the below command for first time only to create kafka topic. 

C:\kafka_2.12-2.4.1\bin\windows\kafka-topics.bat -zookeeper localhost:2181 -topic TweetMessage --create --partitions 3 --replication-factor 1 

To list kafka topic run the below commands in new command prompt. 

cd C:\kafka_2.12-2.4.1\bin\windows\ 

kafka-topics.bat --list --zookeeper localhost:2181 

To get the consumer message run he below command 

kafka-console-consumer -bootstrap-server localhost:9092 -topic TweetMessage\ 

Start running the application. 

                                                          Actuator: 

Go to browser and run the below localhost to check the health condition of the application. 

http://localhost:8085/actuator 

                                                     Prometheus: 

Open file explorer and find Prometheus path and command prompt and run the application. 

Open browser and run localhost to check whether it is working or not. 

http://localhost:9090 

Grafana: 

Open file explorer and find Grafana path and command prompt and run the application. 

Open browser and run localhost to check whether it is working or not. 

http://localhost:3000 

                                                        Elastic Search: 

Open file explorer and find Elastic search path and double click in batch file it automatically starts running. 

Open browser and run localhost to check whether it is working or not. 

http://localhost:9200 

                                                        Logstash: 

Open file explorer and find Prometheus path and command prompt and run the application with below command. 

Logstash -f logstash.conf. 

It will process all the logs 

Open browser and run localhost to check whether it is working or not. 

http://localhost:9600 

                                                          Kibana: 

Open file explorer and find Kibana path and double click in batch file it automatically starts running. 

Open browser and run localhost to check whether it is working or not. 

http://localhost:5601 

                                                         Swagger: 

Open browser and run the below localhost. 

http://localhost:8085/swagger-ui.html 

 
