package org.jon.ivmark.footballcoupons.application.greeting.services;

import org.jon.ivmark.footballcoupons.api.GreetingDto;

public class GreetingService {

    public GreetingDto greetMe(String user) {
        String greeting = String.format("Hello %s!", user);
        GreetingDto greetingDto = new GreetingDto();
        greetingDto.username = user;
        greetingDto.greeting = greeting;
        return greetingDto;
    }
}
