package org.jon.ivmark.footballcoupons.application.greeting.services;

import org.jon.ivmark.footballcoupons.api.GreetingDto;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GreetingServiceTest {

    private final GreetingService service = new GreetingService();

    @Test
    public void greetMe() {
        GreetingDto greeting = service.greetMe("John");
        assertThat(greeting.greeting, is("Hello John!"));
        assertThat(greeting.username, is("John"));
    }

}