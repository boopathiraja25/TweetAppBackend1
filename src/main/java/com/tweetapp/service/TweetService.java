package com.tweetapp.service;

import java.util.List;

import com.tweetapp.exception.IncorrectOrDeletedTweetException;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.model.Tweet;

public interface TweetService {
	List<Tweet> getAllTweets();
    List<Tweet> getAllTweetsByUsername(String username) throws InvalidUsernameOrPasswordException;
    Tweet postNewTweet(Tweet tweet);
    Tweet updateTweetById(String id, Tweet tweet) throws IncorrectOrDeletedTweetException;
    String deleteTweetById(String id);
    String likeTweetById(String id) throws IncorrectOrDeletedTweetException;
    String replyTweetById(Tweet replyTweet,String id);
}
