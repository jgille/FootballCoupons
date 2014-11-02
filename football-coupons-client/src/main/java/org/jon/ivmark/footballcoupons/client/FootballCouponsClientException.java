package org.jon.ivmark.footballcoupons.client;

public class FootballCouponsClientException extends RuntimeException {
    public FootballCouponsClientException(String message, Exception cause) {
        super(message, cause);
    }

    public FootballCouponsClientException(String message) {
        super(message);
    }
}