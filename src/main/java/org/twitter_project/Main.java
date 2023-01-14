package org.twitter_project;


import java.util.Scanner;
import java.util.Set;

public class Main {


    public static void main(String[] args) {


        TwitterHandler twitterHandler = new TwitterHandler();
        UserIO userIO = new UserIO();

        try{
            String username;
            String userID;
            Set<String> following;

            while(true){
                username = userIO.getUsernameFromUser();
                if(username == null){
                    return;
                }
                userID = twitterHandler.getIdByUsername(username);
                following = twitterHandler.getFollowing(userID);
                //as of now this is an incorrect function name
                userIO.printInactiveFollows(following);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}