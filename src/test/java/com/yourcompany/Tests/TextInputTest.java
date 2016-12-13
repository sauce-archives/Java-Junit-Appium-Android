package com.yourcompany.Tests;

import com.yourcompany.Pages.GuineaPigPage;
import org.junit.Test;
import org.openqa.selenium.InvalidElementStateException;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class TextInputTest extends TestBase {

    public TextInputTest(String os,
                         String version, String browser, String deviceName, String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    /**
     * Runs a simple test verifying if the comment input is functional.
     * @throws InvalidElementStateException
     */
    @Test
    public void verifyCommentInputTest() throws InvalidElementStateException {
        String commentInputText = UUID.randomUUID().toString();

        GuineaPigPage page = new GuineaPigPage(driver);

        page.submitComment(commentInputText);

        assertThat(page.getSubmittedCommentText(), containsString(commentInputText));
    }
}