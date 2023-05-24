Information about this application.

    - Appointment Scheduler application, designed to organise upcoming appointments using data provided by sql database.

    - Made by: Joseph Johnson, Contact: 'jjo1743@wgu.edu', Version: 1.0.0, Date: 05/25/22

    - IDE version: IntelliJ IDEA Community Edition 2020.3.2

    - java jdk 15.0.2

    - Javafx - sdk - 15.0.1

    - Directions for program;

        The program starts by asking you to log in with a user name and password. The user name comes from a database,
        named 'test'. The password is identical to the user name. The program then takes you to the main screen where
        the appointments are displayed and the user is alerted if there is an appointment within 15 minutes of sign on,
        or not. The add appointment and update appointment buttons allow you to insert new, or update old, appointments
        and save them to the database. All time and date inputs are local, but stored in UTC. When updating or adding
        always enter your local time for the appointments. There is also a customer records screen in which the user
        can add, update, and delete customers. The appointment view can be filtered in three ways, by clicking on the
        radio buttons at the top of the 'appointments' screen; View All, View by Week, and View by Month.

    - A3F Addition Report: A total for appointments made since creation.

    - SQL Driver version: mysql-connector-java:8.0.27