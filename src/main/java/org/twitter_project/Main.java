package org.twitter_project;



public class Main {
    public static void main(String[] args) {
        TwitterHandler twitterHandler = new TwitterHandler();

        try{
            twitterHandler.findTweetById(20);
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}