import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MacSafari {

    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");

        String host = "<Your cloud Name>.perfectomobile.com"; //Example - demo
        String myToken = "<Your security token>";

        //Safari options
        SafariOptions browserOptions = new SafariOptions();
        browserOptions.setPlatformName("MAC");
        browserOptions.setBrowserVersion("15");

        //Perfecto Options
        Map<String, Object> perfectoOptions = new HashMap<>();
        perfectoOptions.put("securityToken", myToken);
        perfectoOptions.put("scriptName", "MAC Safari Selenium4 Test");
        perfectoOptions.put("platformVersion", "macOS Monterey");
        perfectoOptions.put("location", "NA-US-BOS");
        perfectoOptions.put("resolution", "1280x1024");
        browserOptions.setCapability("perfecto:options", perfectoOptions);

        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), browserOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Test", "1.0"))
                .withJob(new Job("Test", 45))
                .withContextTags("Web")
                .withWebDriver(driver)
                .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

        try {
            reportiumClient.testStart("Selenium4MacSafari", new TestContext("T1", "T2"));
            driver.get("http://expensetracker.perfectomobile.com");
            driver.findElement(By.cssSelector("input[name='login_email']")).sendKeys("test@perfecto.com");
            driver.findElement(By.xpath("//input[@name='login_password']")).sendKeys("test123");
            driver.findElement(By.xpath("//ion-button[@id='login_login_btn']")).click();

            Thread.sleep(2000);

            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        } finally {
            try {
                driver.quit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Run ended");
    }

}
