package org.jon.ivmark.footballcoupons.application.game.resources;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jon.ivmark.footballcoupons.api.game.NewCouponDto;
import org.jon.ivmark.footballcoupons.application.game.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class CouponResourceTest {

    private static final GameService gameService = mock(GameService.class);

    private static CouponDtoConverter couponDtoConverter = mock(CouponDtoConverter.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
                                                                     .addResource(new CouponResource(couponDtoConverter, gameService))
                                                                     .build();

    @Before
    public void setup() {
        reset(gameService);
        reset(couponDtoConverter);
    }

    @Test
    public void createValidCoupon() {
        GameId gameId = new GameId("game1");
        Coupon coupon = Coupon.newCoupon("test", DateTime.now(), 10);
        NewCouponDto newCouponDto = validNewCouponDto();
        when(couponDtoConverter.asCoupon(newCouponDto)).thenReturn(coupon);

        ClientResponse response = resources.client().resource("/games/game1/coupons").type(APPLICATION_JSON_TYPE)
                                     .post(ClientResponse.class, newCouponDto);

        assertThat(response.getStatus(), is(HttpStatus.CREATED_201));
        assertThat(response.getLocation(), notNullValue());
        verify(gameService).addCoupon(gameId, coupon);

    }

    private NewCouponDto validNewCouponDto() {
        NewCouponDto newCouponDto = new NewCouponDto();
        newCouponDto.coupon_name = "test";
        newCouponDto.number_of_matches = 10;
        newCouponDto.coupon_must_be_submitted_before = DateTime.now(DateTimeZone.UTC);
        return newCouponDto;
    }

}