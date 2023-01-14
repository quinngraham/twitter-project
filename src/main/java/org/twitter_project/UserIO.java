package org.twitter_project;

import java.util.Scanner;
import java.util.Set;

public class UserIO{

    private final Scanner genScanner;
    private boolean firstRun;
    public UserIO(){
        genScanner = new Scanner(System.in);
        firstRun = true;
    }


    /**
     * This just basically prints out the contents of a Set of Strings.
     * @param following Following of a user on twitter in String form.
     */
    public void printInactiveFollows(Set<String> following){
        int i = 0;
        for(String s : following){
            i++;
            if(i != following.size()-1)
                System.out.print(s + ", ");
            else
                System.out.print(s);
            if(i % 10 == 9){
                System.out.println();
            }
        }
    }

    /**
     * Just loops through until the user confirms that the username they entered
     * is correct
     * @return String username OR null if user doesn't reply yes
     */
    public String getUsernameFromUser(){
        if(!firstRun){
            System.out.println();
            System.out.println("Would you like to query another user?");
            System.out.print("----->: ");
            String userInput = genScanner.nextLine();
            if(userInput.isEmpty() || userInput.charAt(0) != 'y')
                return null;
        }
        firstRun = false;

        //need to get user input, so they can change what username they want
        String username;
        String userInput;
        while(true){
            System.out.println("Please enter the *exact* username you would " +
                    "like to query (or 'no' to quit): ");
            username = genScanner.nextLine();
            if(username.isEmpty()){
                System.out.println("Enter SOMETHING dude");
                continue;
            } else if(username.charAt(0) == 'n'){
                System.out.println("See ya!");
                return null;
            }
            System.out.println("<------------------>");
            System.out.println("You have entered \"" + username + "\" as a username. Is this correct?");
            userInput = genScanner.nextLine();
            if(!userInput.isEmpty() && userInput.toLowerCase().charAt(0) == 'y'){
                return username;
            }
        }

    }


}
