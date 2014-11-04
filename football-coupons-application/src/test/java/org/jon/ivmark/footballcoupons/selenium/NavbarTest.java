package org.jon.ivmark.footballcoupons.selenium;

import org.junit.Test;

public class NavbarTest extends BaseSeleniumTest {

    @Test
    public void assertThatNavbarIsDisplayed() {
        indexPage.waitUntil("#navbar").areDisplayed();
    }
}
