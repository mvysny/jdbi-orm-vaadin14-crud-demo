package com.vaadin.starter.skeleton;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.time.LocalDate;

import static com.gitlab.mvysny.jdbiorm.JdbiOrm.jdbi;

/**
 * A standard servlet context listener, run by the servlet container such as Tomcat.
 * Configures JDBI-ORM and creates the SQL table for the {@link Person} entity.
 */
@WebListener
public class Bootstrap implements ServletContextListener {
    /**
     * Initializes the application.
     * @param servletContextEvent unused
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 1. Initialize the database.
        // JDBI-ORM requires a JDBC DataSource. We will use
        // the HikariCP connection pool which keeps certain amount of JDBC connections around since they're expensive
        // to construct.
        final HikariConfig hikariConfig = new HikariConfig();
        // We tell HikariCP to use the in-memory H2 database.
        hikariConfig.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        hikariConfig.setMinimumIdle(0);
        // Let's create the DataSource and set it to JDBI-ORM
        JdbiOrm.setDataSource(new HikariDataSource(hikariConfig));
        // Done! The database layer is now ready to be used.

        // 2. Let's prepare the database and create the database tables.
        // Generally you should use FlyWay to migrate your database to newer version,
        // but I wanted to keep things simple here.
        jdbi().useHandle(handle -> handle.createUpdate("create table if not exists Person (\n" +
                "                id bigint primary key auto_increment,\n" +
                "                name varchar not null,\n" +
                "                age integer not null,\n" +
                "                dateOfBirth date,\n" +
                "                created timestamp,\n" +
                "                modified timestamp,\n" +
                "                alive boolean,\n" +
                "                maritalStatus varchar" +
                ")").execute());

        // 3. Generate some example data
        generateTestingData();
    }

    public static void generateTestingData() {
        jdbi().useTransaction(handle -> {
            Person.dao.deleteAll();
            for (int i = 0; i < 200; i++) {
                Person.createDummy(i);
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // Tear down the app. Simply close the JDBI-ORM, which will close the
        // underlying HikariDataSource, which will clean up the pool, close
        // all pooled JDBC connections, stop all threads etc.
        JdbiOrm.destroy();
    }
}
