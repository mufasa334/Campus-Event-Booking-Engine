package org.example;
public class Main{
    public static void main (String[] args){

        BookingWaitlistingManager testBook = new BookingWaitlistingManager(5, "1111110","Sports Event");
        testBook.addUser("Todd Jeff");
        testBook.addUser("Jason Todd");
        testBook.addUser("Bruce Wayne");
        testBook.addUser("Clark Kent");
        testBook.addUser("Diana Prince");
        testBook.addUser("Jimmy Neutron");
        testBook.addUser("Tony Stark");

        testBook.cancelBooking("Clark Kent");

        System.out.println(testBook.getStatus("Todd Jeff"));

        testBook.bookWaitlistPrint();


    }
}