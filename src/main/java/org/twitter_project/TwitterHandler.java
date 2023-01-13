package org.twitter_project;


import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2UsersByUsernameUsernameResponse;
import com.twitter.clientlib.model.Get2UsersIdFollowingResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;
import java.util.*;

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
        //userFields.add("errors");
    }

    /**
     * Need to modify this to take a date to establish what "inactive" means
     *
     */
    public void printInactiveFollows(Set<String> following){
        int i = 0;
        for(String s : following){
            i++;
            System.out.print(s + ", ");
            if(i % 10 == 9){
                System.out.println();
            }
        }
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
                        .forEach(e -> retVal.add(e.getUsername()));
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
     * Just loops through until the user confirms that the username they entered
     * is correct
     * @return String username
     */
    public String getUsernameFromUser(){
        //need to get user input, so they can change what username they want
        String username;
        String userInput;
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Please enter the *exact* username you would like to query: ");
            username = scanner.nextLine();
            if(username.isEmpty()){
                System.out.println("Enter SOMETHING dude");
                continue;
            }
            System.out.println("<------------------>");
            System.out.println("You have entered \"" + username + "\" as a username. Is this correct?");
            userInput = scanner.nextLine();
            if(!userInput.isEmpty() && userInput.toLowerCase().charAt(0) == 'y'){
                return username;
            }
            else{
                System.out.println("A blank username isn't a username you weenie");
            }
        }

    }

}
