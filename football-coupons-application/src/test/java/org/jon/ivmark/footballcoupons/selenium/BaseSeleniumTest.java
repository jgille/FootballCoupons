package org.jon.ivmark.footballcoupons.selenium;

import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.adapter.util.SharedDriver;
import org.fluentlenium.core.annotation.Page;
import org.jon.ivmark.footballcoupons.selenium.pages.IndexPage;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.openqa.selenium.phantomjs.PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY;

@SharedDriver(type = SharedDriver.SharedType.PER_METHOD)
public abstract class BaseSeleniumTest extends FluentTest {

    @Page
    public IndexPage indexPage;

    @Before
    public void init() {
        goTo(indexPage);
    }

    @Override
    public WebDriver getDefaultDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, getPhantomExecutablePath());
        PhantomJSDriver phantomJSDriver = new PhantomJSDriver(caps);
        phantomJSDriver.manage().window().maximize();
        return phantomJSDriver;
    }

    private String getPhantomExecutablePath() {
        return "phantomjs/phantomjs";
    }

}
