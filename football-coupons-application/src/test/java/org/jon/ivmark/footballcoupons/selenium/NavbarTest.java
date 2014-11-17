package org.jon.ivmark.footballcoupons.selenium;

import org.junit.Ignore;
import org.junit.Test;

public class NavbarTest extends BaseSeleniumTest {

    @Test
    @Ignore
    public void assertThatNavbarIsDisplayed() {
        indexPage.waitUntil("#navbar").areDisplayed();
    }
}
