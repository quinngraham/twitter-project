package org.twitter_project;


import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;

import java.util.*;

/**
 * This class handles all of the API calls to twitter.
 * Considering making this class a little smaller. Deleting some redundant methods.
 * @author Quinn Graham
 */
public class TwitterHandler {
    private final TwitterApi apiInstance;
    //private final Set<String> tweetFields;
    private final Set<String> userFields;
    private final Set<String> tweetFields;

    public TwitterHandler(){
        //connect to the twitter API
        //THIS WILL ERROR OUT FOR SOME REASON BUT ITS STILL WORKING
        this.apiInstance = new TwitterApi(new TwitterCredentialsBearer(
                System.getenv("TWITTER_BEARER_TOKEN")));


        //fields that i want returned from the query
        this.userFields = new HashSet<>();
        userFields.add("id");

        this.tweetFields = new HashSet<>();
        tweetFields.add("created_at");
        tweetFields.add("author_id");
    }


    /**
     *
     * @param userID The user ID who's following list we want
     * @return Set retVal, null if the user does not follow anyone
     */
    public Set<String> getFollowing(String userID){
        Set<String> retVal = new HashSet<>();
        try{
            Get2UsersIdFollowingResponse response = apiInstance
                    .users()
                    .usersIdFollowing(userID)
                    .maxResults(1000)
                    .execute();
            if(response.getErrors() != null && response.getErrors().size() > 0){
                System.out.println("Error(s):");
                response.getErrors().forEach(e -> {
                    System.out.println("<---------------->");
                    System.out.println(e.toString());
                    if(e instanceof ResourceUnauthorizedProblem){
                        System.out.print(e.getTitle() + " %n\t");
                        System.out.println(e.getDetail());
                    }
                });
            }
            else{
                Objects.requireNonNull(response.getData())
                        .forEach(e -> retVal.add(e.getId()));
                if(retVal.isEmpty()){
                    return null;
                }
                return retVal;
            }
        }
        catch(ApiException e){
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets the userID (returned as a string) by username
     *
     * @param username String username to look up the ID of
     * @return String id
     */
    public String getIdByUsername(String username){
        try{
            Get2UsersByUsernameUsernameResponse result = apiInstance
                    .users()
                    .findUserByUsername(username)
                    .userFields(userFields)
                    .execute();
            if(result.getErrors() != null && result.getErrors().size() > 0){
                System.out.println("Error(s):");
                result.getErrors().forEach(e -> {
                    System.out.println("<---------------->");
                    System.out.println(e.toString());
                    if(e instanceof ResourceUnauthorizedProblem){
                        System.out.print(e.getTitle() + " %n\t");
                        System.out.println(e.getDetail());
                    }
                });
            }
            else{
                return Objects.requireNonNull(result.getData()).getId();

            }
        }
        catch(Exception e){
            if(e.getClass() == ApiException.class){
                System.err.println("Status code: " + ((ApiException) e).getCode());
                System.err.println("Reason: " + ((ApiException) e).getResponseBody());
                System.err.println("Response headers: " + ((ApiException) e).getResponseHeaders());
                e.printStackTrace();
            } else{
                e.printStackTrace();
            }
        }
        return "Unable to find ID";
    }

    /**
     *
     * @param ids Set of ids to grab most recent tweet of.
     * @return HashMap with ID + date of most recent tweet as K V pair.
     */
    public HashMap<String, String> getMostRecentTweet(Set<String> ids){
        HashMap<String, String> mostRecentTweets = new HashMap<>();

        for(String id : ids){
            try{
                Get2TweetsSearchRecentResponse result = apiInstance
                        .tweets()
                        .tweetsRecentSearch("from:" + id)
                        .tweetFields(tweetFields)
                        .execute();

                if(result.getErrors() != null && result.getErrors().size() > 0){
                    System.out.println("Error(s):");
                    result.getErrors().forEach(e -> {
                        System.out.println("<---------------->");
                        System.out.println(e.toString());
                        if(e instanceof ResourceUnauthorizedProblem){
                            System.out.print(e.getTitle() + " %n\t");
                            System.out.println(e.getDetail());
                        }
                    });
                    //need to change what exception i throw here. Could make a custom wrapper
                    throw new Exception("Error grabbing the most recent tweet in getMostRecentTweet.");
                }
                else{
                    //TODO implement the logic to parse the date
                    //add to the HashMap
                    if(result.getData() != null){
                        System.out.println(result.getData().toString());
                    }



                }
            }
            catch(Exception e){
                if(e.getClass() == ApiException.class){
                    System.err.println("Status code: " + ((ApiException) e).getCode());
                    System.err.println("Reason: " + ((ApiException) e).getResponseBody());
                    System.err.println("Response headers: " + ((ApiException) e).getResponseHeaders());
                    e.printStackTrace();
                } else{
                    e.printStackTrace();
                }
                return null;
            }
        }
        return mostRecentTweets;
    }

    public Set<String> processMostRecentTweet(HashMap<String, String> mostRecent, String date){
        Set<String> outOfDateAccounts = new HashSet<>();

        return outOfDateAccounts;
    }





}
