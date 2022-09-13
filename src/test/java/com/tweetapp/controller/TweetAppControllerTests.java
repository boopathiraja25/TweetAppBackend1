package com.tweetapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tweetapp.exception.IncorrectOrDeletedTweetException;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UserNameAlreadyExistsException;
import com.tweetapp.controller.TweetAppController;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.pojo.UserResponse;
import com.tweetapp.service.TokenService;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;


@WebMvcTest(value = TweetAppController.class)
public class TweetAppControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    TweetService tweetService;

    @MockBean
    TokenService tokenService;

 
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    private User User1;
    private User User2;
    private Tweet tweet1;
    private Tweet tweet2;
    private Tweet tweet3;
    private UserResponse response;

    @BeforeEach
    public void setup(){
        User1 = new User("boopathi", "pasword", "boo@gmail.com", "Boopathi", "Raja", "9655406272");
        User2 = new User( "user","password","email","name","lname","9655406272");
        tweet1 = new Tweet("1","App", LocalDateTime.now(),123L, new User ("user","password","email","name","lname","9655406272"), 
        		new ArrayList<>(),"Monsoon");
        tweet2 = new Tweet("2","Flower", LocalDateTime.now(),125L, new User( "boopathi", "pasword", "boo@gmail.com", "Boopathi", "Raja", "9655406272"), new ArrayList<>(),"Monsoon");
        response = new UserResponse(User1,"active","token");
    }
    
    /*
     * TEST VALID REGISTER USER
     */

    @Test
    @DisplayName("Test valid registerUser()")
    public void testValidRegisterUser() throws Exception{
        when(userService.createUser(any())).thenReturn(User1);
        this.mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User1))).andExpect(status().isCreated());
        verify(userService, times(1)).createUser(any());
    }
    
    /*
     * TEST INVALID REGISTER USER
     */

    @Test
    @DisplayName("Test Invalid registerUser()")
    public void testInvalidRegisterUser() throws Exception{
        when(userService.createUser(any())).thenThrow(new UserNameAlreadyExistsException("Register with different username"));
        this.mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User1))).andExpect(status().isBadRequest());
        verify(userService, times(1)).createUser(any());
    }

    /*
     * TEST VALID LOGIN
     */
    @Test
    @DisplayName("Test valid Login()")
    public void testValidLogin() throws Exception{
        when(userService.login(any(),any())).thenReturn(response);
        when(tokenService.createToken(any())).thenReturn("token");
        this.mockMvc.perform(post("/api/v1.0/tweets/login").queryParam("username", User1.getUsername()).queryParam("password", User1.getPassword())).andExpect(status().isOk());
        verify(userService,times(1)).login(any(),any());
    }

    /*
     * TEST INVALID LOGIN
     */
    @Test
    @DisplayName("Test Invalid Login()")
    public void testInvalidLogin() throws Exception{
        when(userService.login(any(), any())).thenThrow(new InvalidUsernameOrPasswordException("INVALID_CREDENTIALS"));
        when(tokenService.createToken(any())).thenReturn("token");
        this.mockMvc.perform(post("/api/v1.0/tweets/login").queryParam("username", "xxx").queryParam("password", "xxx")).andExpect(status().isBadRequest());
        verify(userService,times(1)).login(any(),any());
    }
    
    /*
     * TEST INVALID LOGIN1
     */
    @Test
    @DisplayName("Test Invalid1 Login()")
    public void testInvalidLogin1() throws Exception{
        when(userService.login(any(), any())).thenReturn(null);
        when(tokenService.createToken(any())).thenReturn("token");
        this.mockMvc.perform(post("/api/v1.0/tweets/login").queryParam("username", "xxx").queryParam("password", "xxx")).andExpect(status().isBadRequest());
        verify(userService,times(1)).login(any(),any());
    }

    /*
     * TEST FORGOT PASSWORD
     */
    @Test
    @DisplayName("Test forgotPassword()")
    public void testForgotPassword() throws Exception {
        when(userService.forgotPassword("boopathi")).thenReturn("Forgot password");
        this.mockMvc.perform(get("/api/v1.0/tweets/boopathi/forgot")).andExpect(status().isOk());
        verify(userService,times(1)).forgotPassword("boopathi");
    }
    
    /*
     * TEST RESET PASSWORD
     */
    @Test
    @DisplayName("Test forgotPassword()")
    public void testResetPassword() throws Exception {
        when(userService.resetPassword(User1.getUsername(),User1.getPassword())).thenReturn("Reset password successfully");
        this.mockMvc.perform(post("/api/v1.0/tweets/reset").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(User1))).andExpect(status().isOk());
        verify(userService,times(1)).resetPassword(User1.getUsername(),User1.getPassword());
    }
    
    /*
     * TEST VALIDATE TOKEN
     */
    @Test
    @DisplayName("Test validateToken()")
    public void testValidateToken() throws Exception{
    	when(tokenService.isTokenValid(any())).thenReturn(true);
    	this.mockMvc.perform(get("/api/v1.0/tweets/validate")).andExpect(status().isOk());
    }

    /*
     * TEST GET ALL USERS
     */
    @Test
    @DisplayName("Test getAllUsers()")
    public void testGetAllUsers() throws Exception{
        when(userService.getAllUsers()).thenReturn(Arrays.asList(User1));
        this.mockMvc.perform(get("/api/v1.0/tweets/users/all")).andExpect(status().isOk());
        verify(userService,times(1)).getAllUsers();
    }
  
    /*
     * TEST VALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test  valid searchByUserName()")
    public void testValidSearchByUserName() throws Exception{
        when(userService.searchByUsername(any())).thenReturn(Arrays.asList(User1));
        this.mockMvc.perform(get("/api/v1.0/tweets/user/search/boopathi")).andExpect(status().isOk());
        verify(userService,times(1)).searchByUsername(any());
    }
    
    /*
     * TEST INVALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test  Invalid searchByUserName()")
    public void testInvalidSearchByUserName() throws Exception{
        when(userService.searchByUsername("2000")).thenThrow(new InvalidUsernameOrPasswordException("Username not found"));
        this.mockMvc.perform(get("/api/v1.0/tweets/user/search/2000")).andExpect(status().isBadRequest());
        verify(userService,times(1)).searchByUsername("2000");
    }
  
    /*
     * TEST GET ALL TWEETS
     */
    @Test
    @DisplayName("Test getAllTweets()")
    public void testGetAllTweets() throws Exception {
        when(tweetService.getAllTweets()).thenReturn(Arrays.asList(tweet1));
        this.mockMvc.perform(get("/api/v1.0/tweets/all")).andExpect(status().isOk());
        verify(tweetService,times(1)).getAllTweets();
    }
  
    /*
     * TEST VALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test valid getAllTweetsOfUser()")
    public void testValidGetAllTweetsOfUser() throws Exception {
        when(tweetService.getAllTweetsByUsername("boopathi")).thenReturn(Arrays.asList(tweet1));
        this.mockMvc.perform(get("/api/v1.0/tweets/{username}","boopathi")).andExpect(status().isOk());
        verify(tweetService,times(1)).getAllTweetsByUsername("boopathi");
    }

    /*
     * TEST INVALID GET ALL TWEETS OF USER BY USERNAME
     */
    @Test
    @DisplayName("Test Invalid getAllTweetsOfUser()")
    public void testInvalidGetAllTweetsOfUser() throws Exception {
        when(tweetService.getAllTweetsByUsername(any())).thenThrow(new InvalidUsernameOrPasswordException("Tweets not found for given username"));
        this.mockMvc.perform(get("/api/v1.0/tweets/xxx")).andExpect(status().isBadRequest());
        verify(tweetService,times(1)).getAllTweetsByUsername("xxx");
    }

    /*
     * ADD NEW TWEET
     */
    @Test
    @DisplayName("Test postTweet()")
    public void testValidPostTweet() throws Exception {
        when(tweetService.postNewTweet(tweet1)).thenReturn(tweet1);
        this.mockMvc.perform(post("/api/v1.0/tweets/{username}/add","boopathi").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).registerModule(new JavaTimeModule()).writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).postNewTweet(any());
    }

    /*
     * TEST VALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test valid updateTweet()")
    public void testValidUpdateTweet() throws Exception {
        when(tweetService.updateTweetById("1",tweet1)).thenReturn(tweet1);
        this.mockMvc.perform(put("/api/v1.0/tweets/{username}/update/{id}","boopathi","1").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).registerModule(new JavaTimeModule()).writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).updateTweetById(any(),any());
    }

    /*
     *  TEST INVALID UPDATE TWEET
     */
    @Test
    @DisplayName("Test Invalid updateTweet()")
    public void testInvalidUpdateTweet() throws Exception {
        when(tweetService.updateTweetById(any(),any())).thenThrow(new IncorrectOrDeletedTweetException("Tweet not updated with given id"));
        this.mockMvc.perform(put("/api/v1.0/tweets/{username}/update/{id}","boopathi","12")).andExpect(status().isBadRequest());
    }

    /*
     * TEST DELETE TWEET
     */
    @Test
    @DisplayName("Test deleteTweet()")
    public void testValidDeleteTweet() throws Exception {
        when(tweetService.deleteTweetById(any())).thenReturn("Tweet Deleted Successfully");
        this.mockMvc.perform(delete("/api/v1.0/tweets/boopathi/delete/1")).andExpect(status().isOk());
        verify(tweetService,times(1)).deleteTweetById(any());
    }

    /*
     * TEST VALID LIKE TWEET
     */
    @Test
    @DisplayName("Test valid likeTweet()")
    public void testValidLikeTweet() throws Exception {
        when(tweetService.likeTweetById(any())).thenReturn("Liked");
        this.mockMvc.perform(put("/api/v1.0/tweets/boopathi/like/1")).andExpect(status().isOk());
        verify(tweetService,times(1)).likeTweetById(any());
    }
    
    /*
     * TEST INVALID LIKE TWEET
     */
    @Test
    @DisplayName("Test Invalid likeTweet()")
    public void testInvalidLikeTweet() throws Exception {
        when(tweetService.likeTweetById(any())).thenThrow(new IncorrectOrDeletedTweetException("Tweet not deleted"));
        this.mockMvc.perform(put("/api/v1.0/tweets/boopathi/like/15")).andExpect(status().isBadRequest());
        verify(tweetService,times(1)).likeTweetById(any());
    }

    /*
     * TEST REPLY TWEET
     */
    @Test
    @DisplayName("Test replyTweet()")
    public void testValidReplyTweet() throws Exception {
        when(tweetService.replyTweetById(any(),any())).thenReturn("Replied tweet successfully");
        this.mockMvc.perform(post("/api/v1.0/tweets/boopathi/reply/1").contentType(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).registerModule(new JavaTimeModule()).writeValueAsString(tweet1))).andExpect(status().isOk());
        verify(tweetService,times(1)).replyTweetById(any(),any());
    }

   
}