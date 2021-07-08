package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
	private static final String URL = "jdbc:mysql://localhost:3306/mydbtest?serverTimezone=UTC";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	public Util() {}
	private static Util instance;
	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
	}
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Соединение не установлено");
			e.printStackTrace();
		}
		return connection;
	}

	private static SessionFactory sessionFactory;
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				Properties properties = new Properties();
				properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
				properties.put(Environment.URL, URL);
				properties.put(Environment.USER, USERNAME);
				properties.put(Environment.PASS, PASSWORD);
				properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

				configuration.setProperties(properties);
				configuration.addAnnotatedClass(User.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}