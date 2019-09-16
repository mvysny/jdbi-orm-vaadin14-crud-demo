package com.vaadin.starter.skeleton;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author mavi
 */
public class AbstractAppLauncher {
    private static Routes routes;

    @BeforeClass
    public static void setup() {
        new Bootstrap().contextInitialized(null);
        // initialize routes only once, to avoid view auto-detection before every test and to speed up the tests
        routes = new Routes().autoDiscoverViews("com.vaadin.starter.skeleton");
    }

    @AfterClass
    public static void teardown() {
        new Bootstrap().contextDestroyed(null);
    }

    @Before
    public void setupVaadin() {
        MockVaadin.setup(routes);
    }

    @After
    public void teardownVaadin() {
        MockVaadin.tearDown();
    }
}
