package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static jm.task.core.jdbc.util.PropertyLoader.getLoadedProperty;

public class Util {
    public static Configuration getPostgresqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.setProperty("hibernate.dialect", getLoadedProperty("psql.dialect"));
        configuration.setProperty("hibernate.connection.driver_class", getLoadedProperty("psql.driver"));
        configuration.setProperty("hibernate.connection.url", getLoadedProperty("psql.connectionUrl"));
        configuration.setProperty("hibernate.connection.username", getLoadedProperty("psql.username"));
        configuration.setProperty("hibernate.connection.password", getLoadedProperty("psql.password"));
        configuration.setProperty("hibernate.show_sql", getLoadedProperty("hibernate_show_sql"));
        configuration.setProperty("hibernate.hbm2ddl.auto", getLoadedProperty("hibernate_hbm2ddl_auto"));
        return configuration;
    }

    public static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public Connection getPostgresqlConnection() throws SQLException {
        try {
            DriverManager.registerDriver((Driver) Class.forName(getLoadedProperty("psql.driver")).newInstance());
            String url = getLoadedProperty("psql.jdbc.url");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new SQLException("Context : getPostgresqlConnection, Problem: getConnection(url) failed");
        }
    }
}
