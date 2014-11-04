package org.jon.ivmark.footballcoupons.selenium.pages;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.wait.FluentWaitMatcher;

import java.util.concurrent.TimeUnit;

public class IndexPage extends FluentPage {

    @Override
    public String getBaseUrl() {
        return "http://localhost:8050";
    }

    @Override
    public String getUrl() {
        return getBaseUrl() + "/";
    }

    public FluentWaitMatcher waitUntil(String id) {
        return await().atMost(2, TimeUnit.SECONDS).until(id);
    }
}
