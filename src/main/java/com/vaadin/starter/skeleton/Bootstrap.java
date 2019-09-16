package com.vaadin.starter.skeleton;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.time.LocalDate;

import static com.gitlab.mvysny.jdbiorm.JdbiOrm.jdbi;

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

        jdbi().useTransaction(handle -> {
            for (int i = 0; i < 1000; i++) {
                final Person person = new Person("Jon Lord" + i, 42);
                person.setDateOfBirth(LocalDate.of(1970, 1, 12));
                person.setMaritalStatus(Person.MaritalStatus.Divorced);
                person.save();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        JdbiOrm.destroy();
    }
}
