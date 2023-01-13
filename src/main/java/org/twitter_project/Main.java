package org.twitter_project;


import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        TwitterHandler twitterHandler = new TwitterHandler();
        boolean cont = true;
        Scanner s = new Scanner(System.in);
        try{
            String username;
            String userID;
            String userInput;
            Set<String> following;
            while(cont){
                username = twitterHandler.getUsernameFromUser();
                userID = twitterHandler.getIdByUsername(username);
                following = twitterHandler.getFollowing(userID);
                //as of now this is an incorrect function name
                twitterHandler.printInactiveFollows(following);
                System.out.println("Would you like to query another user?");
                System.out.print("----->: ");
                userInput = s.nextLine();
                if(userInput.isEmpty() || userInput.charAt(0) != 'y')
                    cont = false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }



}