package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ClickableRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
public class MainView extends VerticalLayout {

    public MainView() {

        DataProvider<Person, Void> dp = DataProvider.fromCallbacks(new CallbackDataProvider.FetchCallback<Person, Void>() {
            @Override
            public Stream<Person> fetch(Query<Person, Void> query) {
                log.info("Fetching Person: " + query.getOffset() + " " + query.getLimit());
                return Person.dao.findAll((long) query.getOffset(), (long) query.getLimit()).stream();
            }
        }, new CallbackDataProvider.CountCallback<Person, Void>() {
            @Override
            public int count(Query<Person, Void> query) {
                return (int) Person.dao.count();
            }
        });

        final Grid<Person> personGrid = new Grid<>(Person.class);
        personGrid.setDataProvider(dp);
        add(personGrid);
        personGrid.addColumn(new NativeButtonRenderer<>("Delete", item -> {
            item.delete();
            personGrid.getDataProvider().refreshAll();
        })).setKey("delete");
    }

    private static final Logger log = LoggerFactory.getLogger(MainView.class);
}
