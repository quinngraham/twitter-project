package org.twitter_project;


import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2UsersByUsernameUsernameResponse;
import com.twitter.clientlib.model.Get2UsersIdFollowingResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;
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

    public TwitterHandler(){
        //connect to the twitter API
        //THIS WILL ERROR OUT FOR SOME REASON BUT ITS STILL WORKING
        this.apiInstance = new TwitterApi(new TwitterCredentialsBearer(
                System.getenv("TWITTER_BEARER_TOKEN")));


        //fields that i want returned from the query
        this.userFields = new HashSet<>();
        userFields.add("id");
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

}
