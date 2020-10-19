1. A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.

This requirement is fulfilled by the ReminderList class which keep tracks of all the reminders a user creates.  It keeps track of the reminders using an ArrayList data member and uses the methods addReminder, deleteReminder, and editReminder.

2. The application must contain a database (DB) of reminders and corresponding data. 

The application will contain a DB of reminders.

3. Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).

I designed a ReminderTypes class to handle this requirement. The types are handled by the "types" ArrayList. The user will be able to choose from one of the types and then the "names" hashmap will take in the type as a key and spit out an ArrayList of names, which the user will be able to choose from.

4. Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB. 

Handled by addReminderByName method in ReminderList class.

5. The reminders must be saved automatically and immediately after they are modified.

This will be implemented on the database side.

6. Users must be able to check off reminders in the list (without deleting them).

Reminder class has toggleCheck method to check off reminder.

I satisfied this requirement by adding the "checked" attribute in the Reminder class. This will keep track of whether the user checked off the reminder or not.

7. Users must also be able to clear all the check-off marks in the reminder list at once.

I implemented the clearChecked() method in the ReminderList class, which will look through the reminderList and clear all checked reminders.

8. Check-off marks for the reminder list are persistent and must also be saved immediately.

This will be implemented on the database side.

9. The application must present the reminders grouped by type.

This requirement is satisfied by groupByType method in User class.

10. The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.

This is handled by user class, which has the necessary methods to create, rename, select, or delete reminder lists.

11. The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.

These options are kept track of by the Reminder class with the fields "date", "time", and "repeat". They are set by addReminder method in ReminderList class.

12. Extra Credit: Option to set up reminder based on location.
Not implemented.

13. The User Interface (UI) must be intuitive and responsive.
Not considered because UI doesn't affect design directly.