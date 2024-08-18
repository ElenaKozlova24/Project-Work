package factory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
public class WebDriverFactory {
    public static WebDriver createNewDriver(String webDriverName, Object... options) {
        switch (webDriverName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                WebDriverManager.chromedriver().driverVersion("126.0.6478.126").setup();
                if (options.length > 0 && options[0] instanceof ChromeOptions) {
                    return new ChromeDriver((ChromeOptions) options[0]);
                } else {
                    return new ChromeDriver();
                }
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                if (options.length > 0 && options[0] instanceof FirefoxOptions) {
                    return new FirefoxDriver((FirefoxOptions) options[0]);
                } else {
                    return new FirefoxDriver();
                }
            default:
                throw new RuntimeException("Unsupported browser: " + webDriverName);
        }
    }
}