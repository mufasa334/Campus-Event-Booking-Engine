package org.example;

public class UserClass {
    private int userID;
    private String name;
    private String email;
    private String type;


    public UserClass(int userID, String name, String email, String type){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.type = type;

    }

    //Need different methods that change depending on the user's type
    public void printSummery(){
        //would print to the user interface in the future for now, here is an example in the terminal
        System.out.println("User ID: " + userID);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("User Type (Student/Staff/Guest): " + type);
    }

    public String returnType(){
        return type;
    }

}
