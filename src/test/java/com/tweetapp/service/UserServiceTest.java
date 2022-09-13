package com.tweetapp.service;

import com.tweetapp.exception.IncorrectOrDeletedTweetException;
import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UserNameAlreadyExistsException;
import com.tweetapp.model.User;
import com.tweetapp.pojo.UserResponse;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TokenService;
import com.tweetapp.service.TweetServiceImpl;
import com.tweetapp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    TweetServiceImpl tweetService;
    
    @Mock
    TokenService tokenService;

    @Mock
    UserRepository userRepo;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private User user1;
    private User user2;
    private User user3;
    private UserResponse response;


    @BeforeEach
    public void setup() {
        user1 = new User( "boopathi", "pasword", "boo@gmail.com", "Boopathi", "Raja", "9655406272");
        response = new UserResponse(user1,"active","token");
        

    }
    
    /*
     * TEST VALID REGISTER USER
     */
    @Test
    @DisplayName("Test valid createUser() of UserService")
    public void testCreateUser() {
    	when(userRepo.save(user1)).thenReturn(user1);
    	assertTrue(userService.createUser(user1).equals(user1));
    	verify(userRepo).save(user1);
    }
    
    /*
     * TEST INVALID REGISTER USER
     */
    @Test
    @DisplayName("Test Invalid createUser() of UserService")
    public void testInvalidCreateUser() throws Exception {
    	user2 = new User("boopathi", "pasword", "boo@gmail.com", "Boopathi", "Raja", "9655406272");
    	when(userRepo.findByUsername("boopathi")).thenReturn(user1);
    	assertThrows(UserNameAlreadyExistsException.class, () -> {
    		userService.createUser(user2);
    	});
    	verify(userRepo).findByUsername("boopathi");
    }

    /*
     * TEST VALID LOGIN
     */
    @Test
    @DisplayName("Test valid login() of UserService")
    public void testlogin() {
    	when(userRepo.findByUsername("boopathi")).thenReturn(user1);
    	when(tokenService.createToken(user1.getUsername())).thenReturn("token");
    	assertThat(userService.login("boopathi", "pasword").getUser().getUsername()).isEqualTo(response.getUser().getUsername());
    	assertThat(userService.login("boopathi", "pasword").getToken()).isEqualTo(response.getToken());
    	verify(userRepo, times(2)).findByUsername("boopathi");
    	verify(tokenService,times(2)).createToken(user1.getUsername());
    	}
    
    /*
     * TEST INVALID LOGIN FOR USERNAME
     */
    @Test
    @DisplayName("Test Invalid username login() of userService")
    public void testInvalidUsernameLogin() throws Exception {
    	when(userRepo.findByUsername("boo")).thenReturn(null);
    	assertThrows(InvalidUsernameOrPasswordException.class,() -> {
    		userService.login("boo", "pasword");
    	});
    	verify(userRepo).findByUsername("boo");
    }
    
    /*
     * TEST INVALID LOGIN FOR PASSWORD
     */
    @Test
    @DisplayName("Test Invalid password login() of userService")
    public void testInvalidPasswordLogin() throws Exception {
    	when(userRepo.findByUsername("boopathi")).thenReturn(user1);
    	when(tokenService.createToken("boo")).thenReturn(null);
    	assertThrows(InvalidUsernameOrPasswordException.class,() -> {
    		userService.login("boopathi", "pas");
    	});
    	verify(userRepo).findByUsername("boopathi");
    }
    
    /*
     * TEST FORGOT PASSWORD
     */
    @Test
    @DisplayName("Test forgotPassword() of userservice")
    public void testForgotPassword() {
    	when(userRepo.findByUsername("boopathi")).thenReturn(user1);
    	assertTrue(userService.forgotPassword("boopathi").equals("Forgot password"));
    	verify(userRepo).findByUsername("boopathi");
    }
    
    /*
     * TEST RESET PASSWORD
     */
    @Test
    @DisplayName("Test resetPassword() of userservice")
    public void testResetPassword() {
    	when(userRepo.findByUsername("boopathi")).thenReturn(user1);
    	assertTrue(userService.resetPassword("boopathi", "pasword").equals("Reset password successfully"));
    	verify(userRepo).findByUsername("boopathi");
    }
   
    /*
     * TEST GET ALL USERS
     */
    @Test
    @DisplayName("Test getAllUsers() of UserService")
    public void testGetAllUsers(){
        when(userRepo.findAll()).thenReturn(Arrays.asList(user1));
        assertThat(userService.getAllUsers()).hasSize(1);
    }

    /*
     * TEST VALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test searchByUsername() of UserService")
    public void testSearchByUsername(){
        when(userRepo.findByUsernameContaining("boo")).thenReturn(List.of(user1));
        assertTrue(userService.searchByUsername("boo").equals(Arrays.asList(user1)));
        verify(userRepo,times(2)).findByUsernameContaining("boo");
    }

    /*
     * TEST INVALID GET ALL USERS BY USERNAME
     */
    @Test
    @DisplayName("Test InvalidSearchByUsername() of UserService")
    public void testInvalidSearchByUsername() throws Exception{
        when(userRepo.findByUsernameContaining("raj")).thenReturn(null);
        assertThrows(InvalidUsernameOrPasswordException.class, () -> { userService.searchByUsername( "raj");
        });
        verify(userRepo).findByUsernameContaining("raj");
    }
}