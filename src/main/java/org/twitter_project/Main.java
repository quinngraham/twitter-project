package org.twitter_project;


import java.util.HashMap;
import java.util.Set;

public class Main {


    public static void main(String[] args) {


        TwitterHandler twitterHandler = new TwitterHandler();
        UserIO userIO = new UserIO();

        try{
            String username;
            String userID;
            Set<String> outOfDateAccts;
            Set<String> following;
            HashMap<String, String> mostRecentTweet;

            while(true){
                username = userIO.getUsernameFromUser();
                //this means the user doesn't want to query anymore
                if(username == null){
                    return;
                }
                userID = twitterHandler.getIdByUsername(username);
                following = twitterHandler.getFollowing(userID);
                mostRecentTweet = twitterHandler.getMostRecentTweet(following);
                //might need to create a new function in UserIO to get the date range user wants
                outOfDateAccts = twitterHandler.processMostRecentTweet(mostRecentTweet, "dateFormat");
                userIO.printInactiveFollows(outOfDateAccts);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}