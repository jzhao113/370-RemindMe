# Design Information

## 1)A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.

To realize this requirement, the classes eachList, regularReminder, and locationReminder were created. regularReminder and locationReminder were created off abstract class reminder. eachList holds an ArrayList of reminder(s) and has methods for the addition, and removal of reminders from the ArrayList. In addition, eachList allows for the editing of a reminder through several methods which allow the type, title, check off, and description to be changed. This is done by the eachList methods calling upon a corresponding method in reminder. To edit a reminder, the reminder has be specified and has to exist.

## 2) The application must contain a database (DB) of reminders and corresponding data.

This requirement is not shown in the UML diagram. It is assumed that a DB is connected to this piece of software which will allow the storage of all data. In addition, the DB does not directly affect the design through this UML diagram thus its not added.

## 3) Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).

To realize this requirement, the class User, reminderType, regularReminder, and locationReminder were created. regularReminder and locationReminder as stated before created off abstract class reminder. The main part of this implementation comes from the connection between User and reminderType. reminderType holds an ArrayList of the different types of reminders that user wants. A reminderType object is created in User. By default the reminderType "default" is placed in, and cannot be deleted. Through User, the user is able to add or delete different types. When a reminder wants to be added, User will check if that reminder type exists. If it does there is no issue, if not the user will be asked to either create that type or the reminder type is set as "default".

## 4). Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB.

To realize this requirement, specific methods were created in User. User holds an ArrayList of eachList, thus it holds every reminder within it, because of this we have a search function built in it which searches the entire ArrayList and its sub arrays. If the reminder is not found, the user is asked to create a new reminder through a standard call of addReminder in User. The user has to enter a reminderType, title, description and which eachList to place it in.

## 5)The reminders must be saved automatically and immediately after they are modified.

This requirement is realized through eachList, User and reminders. eachList holds an ArrayList of reminders, so whenever User decides to add a reminder; the new reminder is placed into a eachList. User then holds an ArrayList of every eachList, thus every reminder is stored in the form of a 2d ArrayList. When a modification is made its through the process of locating the reminder in the 2d ArrayList and changing its details through the built in functions in reminders. Thus saving and immediately changing the reminder if a modifcation is made.

## 6)Users must be able to check off reminders in the list (without deleting them).

This requirement is realized through the abstract class reminders. reminders specifies that every child reminder has to have a built in function which allows check off to be marked. This function is called checkOff.

## 7) Users must also be able to clear all the check-off marks in the reminder list at once.

This requirement is realized through User, eachList, and reminder. User has a method called checkOffAll which communicates to the eachList that it is in to go through every reminder in it, and change the check-off to false through the setter of checkOff.

## 8) Check-off marks for the reminder list are persistent and must also be saved immediately

This requirement is realized through User and eachList. User has a method called checkOffTheList which calls the setter of the eachList User is inside to change the check-off state.

## 9)The application must present the reminders grouped by type.

This requirement is realized through User and reminder. Whenever a type is added to reminderType, an eachList with the title of that type is added typeList. Users can enter these eachLists in typeList, and use them like totalList's eachLists. The only restrictions is when they want to add directly to a eachList in typeList, the type of the reminder must match the title of the typeList eachList, or it cannot be added. In addition, the user cannot create a totalList eachList with the same name as a reminder type. Lastly, when a user is adding to a totalList eachList, a copy of the reminder is placed into the correct typeList eachList.

## 10) The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.

This requirement is realized through eachList and User. The user can create new eachLists which are the reminder lists. Every eachList created contains a title which the user can change and update. In addition, each one could be removed. The only eachLists which cannot have their title changed are the eachLists part of typeLists, in it. To add, eachLists part of typeLists cannot be deleted through the removal of a normal eachList. To remove eachLists part of typeLists the function removeReminderType must be called.

## 11)The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.

This requirement is realized through User and alert. User holds an ArrayList of alerts that the user can add and remove to. Each alert is created through the method addAlert in User which askes the user to specify which reminder that alert is tied to, in addition time. Once the alarm is created, the user can also select if they want this alarm to repeat or not to repeat, and can also add a date to the alert (and remove date if needed).

## 12)Option to set up reminder based on location

This requirement is realized through User, eachList, abstract class reminder, and its child classes regularReminder and locationReminder. When a user creates a reminder they have a choice to select if they want that reminder to mark that location. Depending on their choice, the eachList they're in will create a regularReminder or a locationReminder for them. The locationReminder has an extra information called "geoLocation" which is the location for the reminder. The user can edit "geoLocation" after the locationReminder is created.

## 13)The User Interface (UI) must be intuitive and responsive

This requirement doesn't affect the design in this UML diagram, thus it was not included. It is assumed that the UI will be intuitive and responsive when this piece of software is being built. In addition, the modeling for the UI may be better if it was modelled in another UML diagram, as this diagram isn't affected by it and should focus on the other core features.

## Extra Stuff
The main bulk of the program is ran from the User class. The user has to enter a eachList to be able to access the reminders. The user when finished that eachList will leave it and could go select another one to enter. This is done by the functions enterList and outList. This means that there is no reminder that is not categorized into a eachList. There are two ArrayLists of eachLists in User. The first one being totalList, this is the ArrayList of non-reminder type titled eachLists. The other one typeList which is an ArrayList of remidner typed titled eachLists. The typeList is used to help categorize reminders by type, meanwhile totalList is used to help categorize reminders by the list the user wants it to be in. A reminder in totalList is also a reminder in typeList, but a typeList reminder may or may not be a reminder in totalList.
