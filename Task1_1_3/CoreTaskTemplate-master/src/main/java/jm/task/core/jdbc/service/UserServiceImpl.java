package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.sql.SQLException;
import java.util.List;

import static jm.task.core.jdbc.util.PropertyLoader.getLoadedProperty;

public class UserServiceImpl implements UserService {

    private final UserDao userDAO;

    public UserServiceImpl() {
        this.userDAO = createUserDaoFactory();
    }

    public void createUsersTable() {
        userDAO.createUsersTable();
    }

    public void dropUsersTable() {
        userDAO.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        User savingUser = new User(name, lastName, age);
        userDAO.saveUser(savingUser);
    }

    public void removeUserById(long id) throws SQLException {
        userDAO.removeUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void cleanUsersTable() throws SQLException {
        userDAO.cleanUsersTable();
    }

    static UserDao createUserDaoFactory() {
        if (getLoadedProperty("connection").equalsIgnoreCase("hibernate")) {
            return new UserDaoHibernateImpl();
        } else if (getLoadedProperty("connection").equalsIgnoreCase("jdbc")) {
            return new UserDaoJDBCImpl();
        } else {
            throw new RuntimeException("Context : createUserDaoFactory, Problem : Unknown connection type");
        }
    }
}