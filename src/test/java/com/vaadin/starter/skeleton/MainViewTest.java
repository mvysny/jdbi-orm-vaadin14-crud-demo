package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import org.junit.Before;
import org.junit.Test;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;

/**
 * Uses the Karibu-Testing framework: https://github.com/mvysny/karibu-testing/tree/master/karibu-testing-v10
 *
 * @author mavi
 */
public class MainViewTest extends AbstractAppLauncher {
    @Before
    public void navigateToMainView() {
        UI.getCurrent().navigate("");
    }

    @Test
    public void smokeTest() {
        _assertOne(MainView.class);
    }

    @Test
    public void testGridInitialContents() {
        final Grid<Person> grid = _get(Grid.class);
        expectRows(grid, 1000);
    }
}
