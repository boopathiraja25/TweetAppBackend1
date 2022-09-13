package com.tweetapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exception.IncorrectOrDeletedTweetException;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UserNameAlreadyExistsException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.pojo.UserResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {
	@Autowired
    UserService userService;

    @Autowired
    TweetService tweetService;

    Logger logger = LoggerFactory.getLogger(TweetAppController.class);
    
    /*
     * REGISTER
     * */
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserNameAlreadyExistsException {
        logger.info("User created successfully");
        return new ResponseEntity<User>(userService.createUser(user),HttpStatus.CREATED);
        }

    /*
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(String username,String  password) throws InvalidUsernameOrPasswordException
    {
    	logger.debug("Inside TweetAppController -> login ");
    	try {
    		UserResponse authUser = userService.login(username,password);
    		System.out.println(authUser);
    		if(authUser != null) {
    			return new ResponseEntity<UserResponse>(authUser,HttpStatus.OK);
    			
    			} else {
    			throw new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS");
    		}
    	} catch(InvalidUsernameOrPasswordException e) {
    		throw new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS");
    	}
    }
     
     /*
     * FORGOT PASSWORD
     */
    @GetMapping("{username}/forgot")
    public ResponseEntity<HttpStatus> forgotPassword(@PathVariable("username") String username)
    {
    	logger.info("Forgot password request received with username: " +username);
    	userService.forgotPassword(username);
    	ResponseEntity<HttpStatus> map = new ResponseEntity<>(HttpStatus.OK);
    	return map;
    }


    /*
     * RESET PASSWORD
     */
    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody User user)
    {
    	logger.info("Request to reset the pssword");
    	userService.resetPassword(user.getUsername(), user.getPassword());
    	ResponseEntity<HttpStatus> reset = new ResponseEntity<>(HttpStatus.OK);
    	return reset;
    }
    
    /*
     * GET ALL USERS
    */
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers(){
        logger.info("Getting all users");
        ResponseEntity<List<User>> getUsers = new ResponseEntity<List<User>>(userService.getAllUsers(),HttpStatus.OK);
        return getUsers;
    }

    /*
     * GET ALL TWEETS
     */
    @GetMapping("/all")
    public ResponseEntity<List<Tweet>> getAllTweets()
    {
        logger.info("Get all tweets");
       ResponseEntity<List<Tweet>> getTweets = new ResponseEntity<List<Tweet>>(tweetService.getAllTweets(),HttpStatus.OK);
        return getTweets;
    }

    /*
     * GET USER BY USERNAME
     */
    @GetMapping("/user/search/{username}")
    public ResponseEntity<List<User>> searchByUsername(@PathVariable String username) throws InvalidUsernameOrPasswordException
    {
        logger.info("Getting user by username");
        ResponseEntity<List<User>> searchUsername = new ResponseEntity<List<User>>(userService.searchByUsername(username),HttpStatus.OK);
        return searchUsername;
    }

    /*
     * GET ALL TWEETS BY USERNAME
     */
    @GetMapping("/{username}")
    public ResponseEntity<List<Tweet>> getAllTweetsByUsername(@PathVariable String username) throws InvalidUsernameOrPasswordException
    {
        logger.info("Getting tweets by username");
        ResponseEntity<List<Tweet>> response = new ResponseEntity<List<Tweet>>(tweetService.getAllTweetsByUsername(username),HttpStatus.OK);
        return response;
    }

    /*
     * ADD NEW TWEET
     */
    @PostMapping("/{username}/add")
    public ResponseEntity<Tweet> postNewTweet(@PathVariable("username") String username, @RequestBody Tweet addTweet) throws UserNameAlreadyExistsException
    {
         logger.info("New tweet posted successfully");
        addTweet = tweetService.postNewTweet(addTweet);
        ResponseEntity<Tweet> tweet = new ResponseEntity<Tweet>(addTweet,HttpStatus.OK);
        return tweet;
    }

    /*
     * UPDATE TWEET
     */
    
    @PutMapping("/{username}/update/{id}")
    public ResponseEntity<Tweet> updateTweetById (@PathVariable("username") String username, @PathVariable("id") String id, @RequestBody Tweet tweetUpdate) throws IncorrectOrDeletedTweetException
    {
        logger.info("Updated tweet successfully");
        tweetUpdate = tweetService.updateTweetById(id,tweetUpdate);
        ResponseEntity<Tweet> tweet = new ResponseEntity<Tweet>(tweetUpdate,HttpStatus.OK);
        return tweet;
    }


    /*
     * DELETE TWEET
     */
    @DeleteMapping("/{username}/delete/{id}")
    public ResponseEntity<Tweet> deleteTweetById(@PathVariable("username") String username, @PathVariable("id") String id)
    {
        logger.info("Deleted tweet successfully");
        tweetService.deleteTweetById(id);
        return new ResponseEntity<Tweet>(HttpStatus.OK);
    }


    /*
     * LIKE TWEET
     */
    @PutMapping("/{username}/like/{id}")
    public ResponseEntity<HttpStatus> likeTweetById(@PathVariable("username") String username, @PathVariable("id") String id) throws IncorrectOrDeletedTweetException
    {
        logger.info("Like a tweet");
        tweetService.likeTweetById(id);
        ResponseEntity<HttpStatus> likeTweet = new ResponseEntity<>(HttpStatus.OK);
        return likeTweet;
    }

    /*
     * REPLY TWEET
     */
    @PostMapping("/{username}/reply/{id}")
    public ResponseEntity<HttpStatus> replyTweetById(@PathVariable("username") String username, @PathVariable("id") String id, @RequestBody Tweet replyTweet) throws IncorrectOrDeletedTweetException
    {

            logger.info("Replying to the tweet");
            tweetService.replyTweetById(replyTweet, id);
            ResponseEntity<HttpStatus> reply = new ResponseEntity<>(HttpStatus.OK);
            return reply;
    }
}
