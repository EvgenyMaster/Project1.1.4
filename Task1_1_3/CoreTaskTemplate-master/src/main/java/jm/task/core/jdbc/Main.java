package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        JdbcTaskMain(userService);
    }

    public static void JdbcTaskMain(UserServiceImpl userService) {
        userService.createUsersTable();
        try {
            userService.saveUser("andy", "jefferson", (byte) 12);
            System.out.println("User с именем – andy добавлен в базу данных");
            userService.saveUser("andrey", "gibonov", (byte) 22);
            System.out.println("User с именем – andrey добавлен в базу данных");
            userService.saveUser("vasya", "oblomov", (byte) 43);
            System.out.println("User с именем – vasya добавлен в базу данных");
            userService.saveUser("ivan", "ivanov", (byte) 1);
            System.out.println("User с именем – ivan добавлен в базу данных");
            System.out.println(userService.getAllUsers());
            userService.cleanUsersTable();
        } catch (SQLException quiteCloser) {
            /*NOP*/
        }
        userService.dropUsersTable();
    }
}