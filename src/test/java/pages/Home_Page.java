package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class Home_Page {
    WebDriver driver;
    WebDriverWait wait;

    public Home_Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void acceptCookies() {
        try {
            WebElement cookieBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Accept All Cookies')]")
                )
            );
            cookieBtn.click();
        } catch (Exception e) {}
    }

    public void hideChatbotViaJS() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "document.querySelectorAll('.paradox-widget-frame, #paradox-chat-widget, [id*=\"chat\"], [class*=\"chat\"]').forEach(el => el.remove());"
        );
    }

    public void clickCareers() {
        hideChatbotViaJS();
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[normalize-space()='Careers']"))).click();
    }

    public void searchJob(String job) {
        hideChatbotViaJS();
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[contains(@placeholder,'Search')]")));
        search.sendKeys(job, Keys.ENTER);
    }

    public void selectFirstJob() {
        hideChatbotViaJS();
        WebElement job = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//a[contains(@aria-label,'Job ID')])[1]")));
        job.click();
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.tagName("h1"))).getText().trim();
    }

    public String getLocation() {
        String locationText = "Location not found";
        try {
            locationText = driver.findElement(By.xpath("//*[contains(@data-ph-at-id,'job-location-text')]")).getText().trim();
        } catch (Exception e1) {
            try {
                locationText = driver.findElement(By.xpath("//span[contains(text(),'Bangalore')]")).getText().trim();
            } catch (Exception e2) {
                try {
                    List<WebElement> els = driver.findElements(By.xpath("//*[contains(text(),'India')]"));
                    if (!els.isEmpty()) locationText = els.get(0).getText().trim();
                } catch (Exception e3) {}
            }
        }
        System.out.println(">>> Captured Location from page: " + locationText);
        return locationText;
    }

    public String getJobId() {
        try {
            return driver.findElement(By.xpath("//*[@data-ph-at-id='job-id-text']")).getText().trim();
        } catch (Exception e) {
            return "Job ID not found";
        }
    }

    public void clickApply() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Step 1: Hide/close chatbot overlay
        hideChatbotViaJS();
        try {
            WebElement closeBtn = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(
                    By.xpath("Close widget chat")
                ));
            js.executeScript("arguments[0].click();", closeBtn);
            System.out.println("Chatbot closed successfully");
        } catch (Exception e) {
            System.out.println("No chatbot close button found");
        }

        // Step 2: Try multiple strategies for Apply Now button
        WebElement applyBtn = null;
        try {
            applyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Apply Now')]")
            ));
            System.out.println("Apply Now found as <button>");
        } catch (Exception e1) {
            try {
                applyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@aria-label,'Apply Now')]")
                ));
                System.out.println("Apply Now found as <a aria-label>");
            } catch (Exception e2) {
                try {
                    applyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//ppc-content[contains(text(),'Apply Now')]/ancestor::a")
                    ));
                    System.out.println("Apply Now found via <ppc-content>");
                } catch (Exception e3) {
                    throw new RuntimeException("FAILED - Apply Now button not found");
                }
            }
        }

        // Step 3: Scroll into view and click//
        js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", applyBtn);
        try { Thread.sleep(1000); } catch (Exception e) {}
        js.executeScript("arguments[0].click();", applyBtn);

        System.out.println("Clicked Apply Now successfully");
    }

}
