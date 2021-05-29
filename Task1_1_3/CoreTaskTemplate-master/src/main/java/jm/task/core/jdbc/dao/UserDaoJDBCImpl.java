package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Logger logger = Logger.getLogger("UserDaoJDBCImpl");

    private static final String INSERT_USERS_SQL = new String("INSERT INTO public.project_jm(name, lastname, age) VALUES(?, ?, ?);");
    private static final String CLEANUP_TABLE_SQL = new String("truncate table project_jm;");
    private static final String SELECT_USER_BY_ID = new String("SELECT id,name,email,pass,role FROM public.project_jm WHERE id =?");
    private static final String SELECT_ALL_USERS = new String("SELECT * FROM public.project_jm");
    private static final String DELETE_USERS_SQL = new String("DELETE FROM public.project_jm WHERE id = ?;");
    private static final String DROP_TABLE_SQL = new String("DROP TABLE IF EXISTS \"project_jm\";");
    private static final String UPDATE_USERS_SQL = new String("UPDATE public.project_jm SET name = ?,email= ?, pass =?, role =? WHERE id = ?;");
    private static final String CREATE_TABLE_SQL = new String("CREATE TABLE IF NOT EXISTS \"project_jm\" " +
            "(id serial constraint jmproject_pk primary key," +
            "name varchar(40)," +
            "lastname varchar(40)," +
            "age int not null" +
            ");");

    private final int indexId = 1;
    private final int indexIdUpdate = 4;
    private final int indexName = 1;
    private final int indexLastName = 2;
    private final int indexAge = 3;


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = new Util().getPostgresqlConnection();
             PreparedStatement prepStat = connection.prepareStatement(CREATE_TABLE_SQL)) {
            prepStat.executeUpdate();
        } catch (SQLException exception) {
            logger.warning("Context :  createUsersTable , create statement" +
                    "Problem : create table error");
        }
    }

    public void dropUsersTable() {
        try (Connection connection = new Util().getPostgresqlConnection();
             PreparedStatement prepStat = connection.prepareStatement(DROP_TABLE_SQL)) {
            prepStat.executeUpdate();
        } catch (SQLException exception) {
            logger.warning("Context : dropUsersTable , create statement" +
                    "Problem : drop table error");
        }
    }

    public void saveUser(User user) throws SQLException {
        Connection connection = null;
        try {
            connection = new Util().getPostgresqlConnection();
            connection.setAutoCommit(false);
            PreparedStatement prepStat = connection.prepareStatement(INSERT_USERS_SQL);
            prepStat.setString(indexName, user.getName());
            prepStat.setString(indexLastName, user.getLastName());
            prepStat.setInt(indexAge, user.getAge());
            prepStat.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.warning(("Context :  saveUser" +
                    "Problem : Create connection error, user don't save"));
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void removeUserById(long id) throws SQLException {
        Connection connection = null;
        try {
            connection = new Util().getPostgresqlConnection();
            connection.setAutoCommit(false);
            PreparedStatement prepStat = connection.prepareStatement(DELETE_USERS_SQL);
            prepStat.setLong(indexId, id);
            prepStat.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.warning("Context : removeUserById, create statement" +
                    "Problem : create statement error,user don't removed");
            throw new SQLException();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = new Util().getPostgresqlConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = prepStat.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                byte age = rs.getByte("age");
                users.add(new User(id, name, lastname, age));
            }
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        try (Connection connection = new Util().getPostgresqlConnection();
             PreparedStatement prepStat = connection.prepareStatement(CLEANUP_TABLE_SQL)) {
            prepStat.executeUpdate();
        }

    }
}
