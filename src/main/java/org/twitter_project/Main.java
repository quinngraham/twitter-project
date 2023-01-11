package org.twitter_project;


import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TwitterHandler twitterHandler = new TwitterHandler();

        Map<String, String> env = System.getenv();


        try{
            twitterHandler.setup();
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}