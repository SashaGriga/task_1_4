package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl extends Util implements UserDao {

    public UserDaoHibernateImpl() {}

    private SessionFactory factory = Util.getInstance().getSessionFactory();
    private Transaction transaction= null;
    @Override
    public void createUsersTable() {
        try(Session session = factory.openSession()) {
            transaction= session.beginTransaction();
            session.createSQLQuery("create table if not exists Users (" +
                    "ID bigint primary key auto_increment," +
                    "Name varchar(15) not null," +
                    "LastName varchar(15)," +
                    "Age tinyint)").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try(Session session = factory.openSession()) {
            transaction= session.beginTransaction();
            session.createSQLQuery("drop table if exists Users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = factory.openSession()){
            transaction= session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User " + name + " добавлен");
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();

        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = factory.openSession()){
            transaction= session.beginTransaction();
            User user = new User();
            user.setId(id);
            session.delete(user);
            transaction.commit();
            System.out.println("User " + id + " удалён");
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Session session = factory.openSession()){
            transaction= session.beginTransaction();
            list = session.createQuery("from User").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = factory.openSession()){
            transaction= session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE Users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction!= null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}