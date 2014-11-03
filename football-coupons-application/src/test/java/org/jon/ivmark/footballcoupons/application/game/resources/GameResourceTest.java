package org.jon.ivmark.footballcoupons.application.game.resources;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.jon.ivmark.footballcoupons.api.game.NewGameDto;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GameResourceTest {

    private static final GameService gameService = mock(GameService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
                                                                     .addResource(new GameResource(gameService))
                                                                     .build();

    @Before
    public void setup() {
        reset(gameService);
    }

    @Test
    public void createValidCoupon() {
        NewGameDto newGameDto = new NewGameDto();
        newGameDto.game_name = "Test";

        ClientResponse response = resources.client().resource("/games").type(APPLICATION_JSON_TYPE)
                                     .post(ClientResponse.class, newGameDto);

        assertThat(response.getStatus(), is(HttpStatus.CREATED_201));
        assertThat(response.getLocation(), notNullValue());
        verify(gameService).createGame(any(GameId.class), eq(newGameDto.game_name));
    }

}