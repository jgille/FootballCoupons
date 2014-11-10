package org.jon.ivmark.footballcoupons.application.game.resources;

import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jon.ivmark.footballcoupons.api.game.NewCouponDto;
import org.jon.ivmark.footballcoupons.application.auth.dto.User;
import org.jon.ivmark.footballcoupons.application.game.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
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
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CouponResourceTest {

    @Mock
    private GameService gameService;

    @Mock
    private CouponDtoConverter couponDtoConverter;

    private CouponResource resource;

    @Before
    public void init() {
        resource = new CouponResource(couponDtoConverter, gameService);
    }

    @Test
    public void createValidCoupon() throws URISyntaxException {
        GameId gameId = new GameId("game1");
        Coupon coupon = Coupon.newCoupon("test", DateTime.now(), 10);
        NewCouponDto newCouponDto = asCouponDto(coupon);
        when(couponDtoConverter.asCoupon(newCouponDto)).thenReturn(coupon);

        User user = new User("Test", true);

        Response response = resource.createCoupon(user, gameId, newCouponDto);

        assertThat(response.getStatus(), is(HttpStatus.CREATED_201));
        verify(gameService).saveCoupon(gameId, coupon);
    }

    @Test(expected = WebApplicationException.class)
    public void assertThatOnlyAdminUserCanCreateCoupon() throws URISyntaxException {
        GameId gameId = new GameId("game1");
        Coupon coupon = Coupon.newCoupon("test", DateTime.now(), 10);
        NewCouponDto newCouponDto = asCouponDto(coupon);

        User user = new User("Test", false);

        resource.createCoupon(user, gameId, newCouponDto);
    }

    private NewCouponDto asCouponDto(Coupon coupon) {
        NewCouponDto newCouponDto = new NewCouponDto();
        newCouponDto.coupon_name = coupon.getCouponName();
        newCouponDto.number_of_matches = coupon.getMatches().size();
        newCouponDto.coupon_must_be_submitted_before = coupon.getCouponMustBeSubmittedBefore();
        return newCouponDto;
    }

}