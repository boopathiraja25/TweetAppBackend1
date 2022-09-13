package com.tweetapp.service;

import java.util.List;

import com.tweetapp.exception.InvalidUsernameOrPasswordException;
import com.tweetapp.exception.UserNameAlreadyExistsException;
import com.tweetapp.model.User;
import com.tweetapp.pojo.UserResponse;

public interface UserService {
	User createUser(User user) throws UserNameAlreadyExistsException;
    UserResponse login(String username, String password) throws InvalidUsernameOrPasswordException;
    String forgotPassword(String username);
    String resetPassword(String username, String password);
    List<User> getAllUsers();
    List<User> searchByUsername(String username) throws InvalidUsernameOrPasswordException;
}
