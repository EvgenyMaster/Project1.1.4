package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private Session session;
    private final SessionFactory sessionFactory;
    private final Logger logger = Logger.getLogger("UserDaoHibernateImpl");

    public UserDaoHibernateImpl() {
        sessionFactory = Util.createSessionFactory(Util.getPostgresqlConfiguration());
    }


    @Override
    public void createUsersTable() {

    }

    @Override
    public void dropUsersTable() {

    }

    @Override
    public void saveUser(User user) {
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            logger.warning("Context : insertUser Problem : session save error");
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                /*NOP*/
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            logger.warning("Context : deleteUser Problem : session delete error");
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                /*NOP*/
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> users = new ArrayList<>();
        String sql = "FROM " + User.class.getSimpleName();
        try {
            session = sessionFactory.openSession();
            transaction = session.getTransaction();
            Query query = session.createQuery(sql);
            users = query.list();
            transaction.commit();
        } catch (Exception e) {
            try {
                logger.warning("Context : getUserList Problem : session getAll error");
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Exception ex) {
                /*NOP*/
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {

    }
}
