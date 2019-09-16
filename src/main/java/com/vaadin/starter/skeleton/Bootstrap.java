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
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        hikariConfig.setMinimumIdle(0);

        JdbiOrm.setDataSource(new HikariDataSource(hikariConfig));

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
        JdbiOrm.destroy();
    }
}
