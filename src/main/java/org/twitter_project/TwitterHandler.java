package org.twitter_project;


import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsIdResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;

import java.util.HashSet;
import java.util.Set;

public class TwitterHandler {

    public TwitterHandler(){
        //Do something on init
    }

    public void setup(){
        //twitter init stuff
        TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
                System.getenv("TWITTER_OAUTH2_CLIENT_ID"),
                System.getenv("TWITTER_OAUTH2_CLIENT_SECRET"),
                System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN"),
                System.getenv("TWITTER_OAUTH2_REFRESH_TOKEN")));

        //Convenience set to make looking stuff up easier
        //TODO modify this to be what i want
        Set<String> tweetFields = new HashSet<>();
        tweetFields.add("author_id");
        tweetFields.add("id");
        tweetFields.add("created_at");

        try{
            //find tweets by id?
            Get2TweetsIdResponse result = apiInstance
                    .tweets()
                    .findTweetById("20")
                    .tweetFields(tweetFields)
                    .execute();
            //when would there be a scenario where one of these is true and the other isn't??
            if(result.getErrors() != null && result.getErrors().size() > 0){
                System.out.println("Error:");
                result.getErrors().forEach(e -> {
                    System.out.println(e.toString());
                    if(e instanceof ResourceUnauthorizedProblem){
                        System.out.print(e.getTitle() + " %n\t");
                        System.out.println(e.getDetail());
                    }
                });
            }
            else{
                //leaving the tostring call in there just to be safe
                System.out.println("findTweetById - tweet text: " + result.toString());
            }
        }
        catch(ApiException e){
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
