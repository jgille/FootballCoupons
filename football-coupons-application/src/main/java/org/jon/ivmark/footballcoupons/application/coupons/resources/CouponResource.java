package org.jon.ivmark.footballcoupons.application.coupons.resources;

import org.jon.ivmark.footballcoupons.api.coupons.NewCouponDto;
import org.jon.ivmark.footballcoupons.application.coupons.CouponRepository;
import org.jon.ivmark.footballcoupons.application.coupons.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates.Coupon;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/{gameId}/coupons")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class CouponResource {

    private final CouponDtoConverter couponDtoConverter;
    private final CouponRepository couponRepository;

    public CouponResource(CouponDtoConverter couponDtoConverter, CouponRepository couponRepository) {
        this.couponDtoConverter = couponDtoConverter;
        this.couponRepository = couponRepository;
    }

    @POST
    public Response createCoupon(@PathParam("gameId") String gameId,
                                 @Valid NewCouponDto newCouponDto) throws URISyntaxException {
        Coupon coupon = couponDtoConverter.asCoupon(newCouponDto);
        couponRepository.createCoupon(gameId, coupon);
        return Response.created(new URI(coupon.getCouponId().getValue())).build();
    }
}
