package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class MyGUI {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            User[] people = new User[999];
            Event[] funs = new Event[999];
            int[] ucount = new int[1];
            int[] ecount = new int[1];

            StringBuilder sbUser = new StringBuilder();
            sbUser.append(String.format("%-30s %-30s %-30s %-30s%n", "ID", "NAME", "EMAIL", "TYPE"));
            sbUser.append("------------------------------------------------------------------------------------------\n");

            StringBuilder sbEvent = new StringBuilder();
            sbEvent.append(String.format("%-30s %-30s %-30s %-30s %-30s %-30s%n", "ID", "TITLE", "DATE", "LOCATION", "CAPACITY", "TYPE"));
            sbEvent.append("--------------------------------------------------------------------------------------------------------------------\n");

            StringBuilder sbBooker = new StringBuilder();
            sbBooker.append(String.format("%-30s %-30s %-30s%n", "PERSON", "EVENT", "STATUS"));
            sbBooker.append("------------------------------------------------------------------------------------------\n");

            //Users
            JTabbedPane tabs = new JTabbedPane();
            JPanel users = new JPanel();
            JButton button = new JButton("Confirm");
            JTextField userID = new JTextField(15);
            JTextField name = new JTextField(15);
            JTextField email = new JTextField(15);
            JTextField type = new JTextField(15);
            JLabel output = new JLabel("                           ID                                          NAME                                         EMAIL                                            TYPE                                                                              ");
            JTextArea display = new JTextArea(40,40);
            display.setEditable(false);

            //Events
            JPanel events = new JPanel();
            JButton button2 = new JButton("Confirm");
            JLabel output2 = new JLabel("ID   TITLE  DATE  LOCATION   CAPACITY   TYPE                                                                                                                                                               ");
            JTextField eventID = new JTextField(10);
            JTextField title = new JTextField(10);
            JTextField date = new JTextField(10);
            JTextField location = new JTextField(10);
            JTextField capacity = new JTextField(10);
            JTextField etype = new JTextField(10);
            JTextArea display2 = new JTextArea(40,60);


            //Booking
            JPanel booking = new JPanel();
            JButton button3 = new JButton("Confirm");
            JLabel output3 = new JLabel("                                                                                                                         PERSON BOOKING               EVENT                                                                                                                                                                                                                                ");
            JTextField bookee = new JTextField(15);
            JTextField booker = new JTextField(15);
            JTextArea display3 = new JTextArea(40,60);

            //cancelling
            JPanel cancelling = new JPanel();
            JButton button4 = new JButton("Confirm");
            JLabel output4 = new JLabel("                                                                                                                                                        NUMBER OF BOOKING TO DELETE                                                                                                                                                                                                                                 ");
            JTextField bookee2 = new JTextField(15);
            JTextField booker2 = new JTextField(15);
            JTextArea display4 = new JTextArea(40,60);


            display.setText(sbUser.toString());
            display2.setText(sbEvent.toString());
            display3.setText(sbBooker.toString());
            display4.setText(sbBooker.toString());

            button.addActionListener(e -> {
                if(type.getText().equals("Guest")) {
                    people[ucount[0]] = new Guest(userID.getText(), name.getText(), email.getText());
                    ucount[0]++;
                }
                if(type.getText().equals("Student")) {
                    people[ucount[0]] = new Student(userID.getText(), name.getText(), email.getText());
                    ucount[0]++;
                }
                if(type.getText().equals("Staff")) {
                    people[ucount[0]] = new Staff(userID.getText(), name.getText(), email.getText());
                    ucount[0]++;
                }
                sbUser.append(String.format("%-30s",people[ucount[0] - 1].getUserId()));
                sbUser.append(String.format("%-30s",people[ucount[0] - 1].getName()));
                sbUser.append(String.format("%-30s",people[ucount[0] - 1].getEmail()));
                sbUser.append(String.format("%-30s",people[ucount[0] - 1].getUserType().toString()));
                sbUser.append("\n");
                display.setText(sbUser.toString());
            });

            button2.addActionListener(e -> {
                if(etype.getText().equals("Concert")) {
                    funs[ecount[0]] = new Concert(eventID.getText(), title.getText(), LocalDateTime.parse(date.getText()), location.getText(), Integer.parseInt(capacity.getText()), etype.getText());
                    ecount[0]++;
                }
                if(etype.getText().equals("Workshop")) {
                    funs[ecount[0]] = new Workshop(eventID.getText(), title.getText(), LocalDateTime.parse(date.getText()), location.getText(), Integer.parseInt(capacity.getText()), etype.getText());
                    ecount[0]++;
                }
                if(etype.getText().equals("Seminar")) {
                    funs[ecount[0]] = new Seminar(eventID.getText(), title.getText(), LocalDateTime.parse(date.getText()), location.getText(), Integer.parseInt(capacity.getText()), etype.getText());
                    ecount[0]++;
                }
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getEventId()));
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getTitle()));
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getDateTime()));
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getLocation()));
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getCapacity()));
                sbEvent.append(String.format("%-30s",funs[ecount[0] - 1].getEventType().toString()));
                sbEvent.append("\n");
                display2.setText(sbEvent.toString());

            });

            button3.addActionListener(e -> {
                int j = 0, k = 0;
                boolean work = false, work2 = false;

                for(int i = 0; i < ucount[0]; i++) {
                    if(bookee.getText().equals(people[i].getName())) {
                        j = i;
                        work = true;
                    }
                }

                for(int i = 0; i < ecount[0]; i++) {
                    if(booker.getText().equals(funs[i].getTitle())) {
                        k = i;
                        work2 = true;
                    }
                }

                if(work && work2) {
                    funs[j].getManager().addUser(people[k].getName());
                    sbBooker.append(String.format("%-30s", people[j].getName()));
                    sbBooker.append(String.format("%-30s", funs[k].getTitle()));
                    sbBooker.append(String.format("%-30s", "Confirmed"));
                    sbBooker.append("\n");
                    display3.setText(sbBooker.toString());
                    display4.setText(sbBooker.toString());
                }
            });

            button4.addActionListener(e -> {
                int index1 = 0, index2 = 0, count = 0;

                for(int i = 0; i < sbBooker.length(); i++) {

                    if(sbBooker.charAt(i) == '\n') {
                        count++;
                        if(count == Integer.parseInt(bookee2.getText()) + 1) { index1 = i; }
                        else if(count == Integer.parseInt(bookee2.getText()) + 2) { index2 = i; }
                    }
                }

                sbBooker.replace(index1,index2,"");
                display3.setText(sbBooker.toString());
                display4.setText(sbBooker.toString());
            });

            users.setLayout(new java.awt.FlowLayout());
            users.add(output);
            users.add(userID);
            users.add(name);
            users.add(email);
            users.add(type);
            users.add(button);
            users.add(display);

            events.setLayout(new java.awt.FlowLayout());
            events.add(output2);
            events.add(eventID);
            events.add(title);
            events.add(date);
            events.add(location);
            events.add(capacity);
            events.add(etype);
            events.add(button2);
            events.add(display2);

            booking.setLayout(new java.awt.FlowLayout());
            booking.add(output3);
            booking.add(bookee);
            booking.add(booker);
            booking.add(button3);
            booking.add(display3);

            cancelling.setLayout(new java.awt.FlowLayout());
            cancelling.add(output4);
            cancelling.add(bookee2);
            cancelling.add(button4);
            cancelling.add(display4);


            tabs.addTab("Users", users);
            tabs.addTab("Events", events);
            tabs.addTab("Booking", booking);
            tabs.addTab("Cancelling", cancelling);

            frame.add(tabs);


            frame.setVisible(true);
        });
    }
}