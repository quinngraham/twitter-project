package org.twitter_project;


import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsIdResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;
import java.util.HashSet;
import java.util.Set;

public class TwitterHandler {


    public TwitterHandler(){
        //Do something on init
    }

    //public not sure what to return here, might need to make a helper function to
    //turn the data i get into a list or something


    public void findTweetById(int idVal){
        //twitter init stuff
        //this throws a FileNotFound Exception every single time i run it regardless of having
        //valid credentials. not a single clue why
        TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsBearer(
                System.getenv("TWITTER_BEARER_TOKEN")));

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
                    .findTweetById(Integer.toString(idVal))
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
