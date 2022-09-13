package com.tweetapp.kafka;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "TweetMessage", groupId = "tweet_group")
    public void consume(String message){
        logger.info("Consumed message " +message);
        logger.info(String.format("Consumed message: %s", message));
    }

}