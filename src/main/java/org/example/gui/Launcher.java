package org.example.gui;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }

    Event[] events = new Event[999];
    User[] users = new User[999];
    int choice = 0, eventCounter = 0, userCounter = 0;


    while(choice != 4) { //make choice take in stiff from the gui

        // 1 = make event, 2 - make user, 3 - boooking, 4 -  exit app
        switch(choice) {

            case 1:


                switch(choice) {

                    //Make input on type of event here
                    case 1:
                        /// Make it ask for parameters here and put it in the brackets
                        events[eventCounter] = new Concert();
                        break;
                    case 2:
                        events[eventCounter] = new Seminar();
                        break;
                    case 3:
                        events[eventCounter] = new Workshop();
                        break;
                }
                break;


            case 2:

                //make input on user type here
                switch(choice) {

                    //Make input on type of user here
                    case 1:
                        /// Make it ask for parameters here and put it in the brackets
                        users[userCounter] = new Guest();
                        break;
                    case 2:
                        users[userCounter] = new Student();
                        break;
                    case 3:
                        users[userCounter] = new Staff();
                        break;
                }
                break;

            case 3:

                //make the booking work here

                // case 4 should just end it
        }

    }
}
