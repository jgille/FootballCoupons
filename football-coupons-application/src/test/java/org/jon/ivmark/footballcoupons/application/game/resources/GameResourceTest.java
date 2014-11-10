package org.jon.ivmark.footballcoupons.application.game.resources;

import org.eclipse.jetty.http.HttpStatus;
import org.jon.ivmark.footballcoupons.api.game.NewGameDto;
import org.jon.ivmark.footballcoupons.application.auth.dto.User;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GameResourceTest {

    @Mock
    private GameService gameService;

    private GameResource resource;

    @Before
    public void setup() {
        resource = new GameResource(gameService);
    }

    @Test
    public void createValidCoupon() throws URISyntaxException {
        NewGameDto newGameDto = new NewGameDto();
        newGameDto.game_name = "Test";

        User user = new User("Test", true);
        Response response = resource.createGame(user, newGameDto);

        assertThat(response.getStatus(), is(HttpStatus.CREATED_201));
        verify(gameService).createGame(any(GameId.class), eq(newGameDto.game_name));
    }

    @Test(expected = WebApplicationException.class)
    public void assertThatOnlyAdminUserCanCreateCoupon() throws URISyntaxException {
        NewGameDto newGameDto = new NewGameDto();
        newGameDto.game_name = "Test";

        User user = new User("Test", false);
        resource.createGame(user, newGameDto);
    }

}