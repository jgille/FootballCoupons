package org.jon.ivmark.footballcoupons.application.greeting.resources;

import org.jon.ivmark.footballcoupons.api.GreetingDto;
import org.jon.ivmark.footballcoupons.application.greeting.services.GreetingService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GreetingResourceTest {

    private static final GreetingService service = mock(GreetingService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new GreetingResource(service))
            .build();

    private GreetingDto expectedGreeting;

    @Before
    public void setup() {
        reset(service);

        expectedGreeting = new GreetingDto();
        expectedGreeting.username = "blah";
        expectedGreeting.greeting = "Hello blah!";
        when(service.greetMe(eq(expectedGreeting.username))).thenReturn(expectedGreeting);
    }

    @Test
    public void greet() {
        GreetingDto actual =
                resources.client().resource("/greeting/" + expectedGreeting.username).get(GreetingDto.class);
        assertThat(actual.greeting, is(expectedGreeting.greeting));
        assertThat(actual.username, is(expectedGreeting.username));
        verify(service).greetMe(expectedGreeting.username);
    }

}