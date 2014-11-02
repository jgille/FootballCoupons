package org.jon.ivmark.footballcoupons.application.game.resources;

import org.jon.ivmark.footballcoupons.api.coupons.NewCouponDto;
import org.jon.ivmark.footballcoupons.application.game.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.aggregates.Coupon;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.GameId;

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
    private final GameService gameService;

    public CouponResource(CouponDtoConverter couponDtoConverter, GameService gameService) {
        this.couponDtoConverter = couponDtoConverter;
        this.gameService = gameService;
    }

    @POST
    public Response createCoupon(@PathParam("gameId") GameId gameId,
                                 @Valid NewCouponDto newCouponDto) throws URISyntaxException {
        Coupon coupon = couponDtoConverter.asCoupon(newCouponDto);
        gameService.addCoupon(gameId, coupon);
        return Response.created(new URI(coupon.getCouponId().getValue())).build();
    }
}
