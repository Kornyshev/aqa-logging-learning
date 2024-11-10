package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class AppTest {

    private final Steps steps = new Steps();

    @BeforeEach
    void setUp() {
        steps.openApplication();
    }

    @Test
    void someTest() {
        steps.login();
        steps.performUserActions();
        steps.logout();
    }

    @AfterEach
    void tearDown() {
        steps.closeApplication();
    }
}
