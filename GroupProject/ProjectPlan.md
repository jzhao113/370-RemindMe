# Project Plan

**Author**: Team6b

## Introduction

Reminder is an Android application that allows users to manage reminders. Users can download the app which then they will have access to create lists consisting of reminders the user wants to be aware of. Users can add reminders to a list, delete reminders, and edit reminders in a list which will be safely stored. Users can also specify the types of reminders as well as check off reminders when they are complete in which they have an option to set up alerts with a day and time to alert the user of their reminder. Users can also set up reminders based on location. Reminder serves to help manage schedules and improve productivity for our users!

## Process Description

To implement our processes we broke down into three activity pages where the user can:

- user.java
	* In this activity, users will have the ability to add list or reminders, displat list of reminders, search for reminders, actively track location for reminders if needed, and send alerts for reminders. They can also select and deselect reminders as well as edit the reminders or list name, or delete reminders. 
- search.java
	* In this activity, the user can see their existing reminders they have created that are still being tracked in the search bar. 
- reminder.java
	* In this activity, when a user creates a reminder they are able to specify list name and description of reminder. 

Extra helper activities were:

- DatabaseHelper.java
	* In this activity, we managed our database to store or delete reminders or reminders list. Users can update list and and reminder types as well as update the status and check if it already exists.
- TimePickerFragment.java
	* In this activity, we get an instance of a calender in which users can select a date and time.
- AlertReciever.java
	* In this activity, we have a broadcast reciever which listens to notifications a user sets.
- Notification.java
	* In this activity, we create notifications.
- NotificationHelper.java
	* In this activity, get channel notifcations and share a message to user of their notifcations from the database.
- MyExListAdapter.java
	* In this activity, sets view of list and reminders.


## Team

- Joseph Htet, Akhil Khanna, Wadgma Masab, and Jeffrey Zhao
- Project Manager: Lead the team and keep track of project progress and organization. Make sure tasks are assigned. Document project report. 
- Front-End Developer: In charge of designing the application user interface. 
- Back-End Developer: In charge of functionality of the application and building database and ensure the application and database communicate properly. 
	
- 	|   | Role  |   
	|---|---|
	|Joseph Htet   | Front-End Developer   |  
	|Akhil Khanna   | Back-End Developer   |   
	|Wadgma Masab   | Project Manager/ Front-End Developer  | 
	|Jeffrey Zhao  | Back-End Developer   |
