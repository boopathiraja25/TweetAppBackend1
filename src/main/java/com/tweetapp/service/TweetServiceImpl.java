package com.tweetapp.service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.exception.IncorrectOrDeletedTweetException;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.kafka.ProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetServiceImpl implements TweetService {

	@Autowired
    TweetRepository tweetRepository;
    
	
    ProducerService producerService;

    //Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);

    @Override
    public List<Tweet> getAllTweets(){
        log.info("Getting all the tweets");
        List<Tweet> tweet;
        tweet = tweetRepository.findAll();
        log.info("Getting all tweets");
        return tweet;
    }

    @Override
    public List<Tweet> getAllTweetsByUsername(String  username) throws InvalidUsernameOrPasswordException
    {
    	List<Tweet> allTweets = tweetRepository.findByUserUsername(username);
    	if(allTweets != null) {
    		log.info("Getting all tweets by username" +username);
    		return allTweets;
    		
    	}
    	else {
    		throw new InvalidUsernameOrPasswordException("Invalid credentials");
    	}
        
         
    }

    @Override
    public Tweet postNewTweet(Tweet tweet)
    {
        log.info("New tweet posted successfully");
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet updateTweetById(String id, Tweet tweet) throws IncorrectOrDeletedTweetException
    {
        Tweet updateTweet = tweetRepository.findById(id).orElse(null);
        if(updateTweet!=null)
        {
            
            tweet = tweetRepository.save(tweet);
            producerService.sendMessage(LocalDateTime.now() + "-" + "updated Tweet" + "-" + tweet.getUser().getUsername());
            log.info("Updated tweet successfully");
            return tweet;
        }
        else 
        	log.info("Tweet not updated");
        	throw new IncorrectOrDeletedTweetException("Incorrect or deleted tweet");
       
    }

    @Override
    public String deleteTweetById(String id)
    {
        Tweet deleteTweet = tweetRepository.findById(id).orElse(null);
        if(deleteTweet!=null)
        {
            log.info("Tweet Deleted Successfully");
            tweetRepository.delete(deleteTweet);
            
        }
        return "Tweet deleted successfully";


    }

    @Override
    public String likeTweetById(String id) throws IncorrectOrDeletedTweetException
    {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if(tweet!=null) {
            tweet.setLikes(tweet.getLikes() + 1);
            tweetRepository.save(tweet);
            return "Liked Tweet successfully";
        }
        else 
        	throw new IncorrectOrDeletedTweetException("Incorrect or deleted tweet");
        
    }

    @Override
    public String replyTweetById(Tweet replyTweet, String id) 
    {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if(tweet!=null)
        {
            List<Tweet> replies = tweet.getReplies();
            replies.add(replyTweet);
            tweet.setReplies(replies);
            tweetRepository.save(tweet);
        }
        return "Replied tweet successfully" ;
    }

}
