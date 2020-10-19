 DESIGN INFORMATION
 1.A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.
 //I added the ReminderList class which allows users sto add reminders, delete reminders and edit reminders in a list
 
2.The application must contain a database (DB) of ​reminders ​and corresponding d​ ata​.
//I added ReminderDatabase and the DataManager class that allows user to add and manuplate the items from the database.

3.Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).
//The application must look in its DB for items with similar names and ask the users, for each of them, whether that is the item they intended to add. If a match cannot be found, the application must ask the user to select a type for the item and then save the new item, together with its type, in its DB. Added a ReminderType class to distinguish between different types. 

4.Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB.
//The ReminderList class allows the user to addReminderName which will allow user to add an item by name. DataManager will check for matches. If user confirms reminderType, it will be set to that type, if no match they will set the reminder manually.

5.The reminders must be saved automatically and immediately after they are modified.

6.Users must be able to check off reminders in the list (without deleting them).
// the checkOffReminder method will allow users to check off items without deleting them. If the user wants to delete then they use the deleteReminder methotd

7.Users must also be able to clear all the check-off marks in the reminder list at once.
//the clearCheckOffReminder method will allow user to clear off existing check off marks

8.Check-off marks for the reminder list are persistent and must also be saved immediately.

9.The application must present the reminders grouped by type.
//in DataManager user can view reminders and sort by type

10.The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.
//added DateOfReminder attribute the uses the Date class. Also implemented the ListManager that will allow users to create,rename,select and delete

11.The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.
//the Date class allows this option

12.Extra Credit:​ Option to set up reminder based on location. 
//Not implemented

13.The User Interface (UI) must be intuitive and responsive.
