package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import kotlin.Unit;
import org.junit.Before;
import org.junit.Test;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Uses the Karibu-Testing framework: https://github.com/mvysny/karibu-testing/tree/master/karibu-testing-v10
 *
 * @author mavi
 */
public class MainViewTest extends AbstractAppLauncher {
    @Before
    public void navigateToMainView() {
        Person.dao.deleteAll();
        UI.getCurrent().navigate("");
    }

    @Test
    public void smokeTest() {
        _assertOne(MainView.class);
    }

    @Test
    public void testGridInitialContents() {
        Bootstrap.generateTestingData();
        final Grid<Person> grid = _get(Grid.class);
        expectRows(grid, 200);
    }

    @Test
    public void testDeletePerson() {
        Person.createDummy(0);
        assertNotNull(Person.dao.findByName("Jon Lord0"));
        final Grid<Person> grid = _get(Grid.class);
        _clickRenderer(grid, 0, "delete", component -> Unit.INSTANCE);
        expectRows(grid, 0);
        assertNull(Person.dao.findByName("Jon Lord0"));
    }
}
