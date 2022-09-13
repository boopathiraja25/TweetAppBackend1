package com.tweetapp.pojo;

import com.tweetapp.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
	private User user;
    private String loginStatus;
    private String token;
}
