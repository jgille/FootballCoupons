package org.jon.ivmark.footballcoupons.application.coupons.resources;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.jon.ivmark.footballcoupons.api.coupons.NewCouponDto;
import org.jon.ivmark.footballcoupons.application.coupons.CouponRepository;
import org.jon.ivmark.footballcoupons.application.coupons.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates.Coupon;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.net.URISyntaxException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CouponResourceTest {

    private static final CouponRepository couponRepository = mock(CouponRepository.class);

    private static CouponDtoConverter couponDtoConverter = mock(CouponDtoConverter.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
                                                                     .addResource(new CouponResource(couponDtoConverter, couponRepository))
                                                                     .build();

    @Before
    public void setup() {
        reset(couponRepository);
        reset(couponDtoConverter);
    }

    @Test
    public void createValidCoupon() {
        Coupon coupon = Coupon.newCoupon("test", DateTime.now(), 10);
        NewCouponDto newCouponDto = validNewCouponDto();
        when(couponDtoConverter.asCoupon(newCouponDto)).thenReturn(coupon);

        ClientResponse response = resources.client().resource("/game1/coupons").type(APPLICATION_JSON_TYPE)
                                     .post(ClientResponse.class, newCouponDto);

        assertThat(response.getStatus(), is(HttpStatus.CREATED_201));
        assertThat(response.getLocation(), notNullValue());
        verify(couponRepository).createCoupon("game1", coupon);
    }

    private NewCouponDto validNewCouponDto() {
        NewCouponDto newCouponDto = new NewCouponDto();
        newCouponDto.coupon_name = "test";
        newCouponDto.number_of_matches = 10;
        newCouponDto.coupon_must_be_submitted_before = DateTime.now(DateTimeZone.UTC);
        return newCouponDto;
    }

}