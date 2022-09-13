package com.tweetapp.model;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Tweet")
public class Tweet {
	@Id
    private String id;
    private String tweetName;

    @CreatedDate
    private LocalDateTime postDate;
    private long likes;
    private User user;
    private List<Tweet> replies;
    private String tweetTags;
}
