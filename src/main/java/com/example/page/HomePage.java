package com.example.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class HomePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String url) {
        driver.get(url);
        logger.info("Открыт URL: {}", url);
    }

    public void clickOnTestingElement() {
        WebElement testingElement = driver.findElement(By.xpath("//div[text()='Тестирование']"));
        testingElement.click();
        logger.info("Кликнуто на элемент 'Тестирование'");
    }

    public int getCourseCardCount() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-18q05a6-1.bwGwUO")));
        List<WebElement> cards = container.findElements(By.tagName("a"));
        int cardCount = cards.size();
        logger.info("Количество карточек курсов: {}", cardCount);
        return cardCount;
    }

    public void clickOnFirstCourseCard() {
        WebElement firstCourseCard = driver.findElement(By.cssSelector(".sc-18q05a6-1.bwGwUO a"));
        firstCourseCard.click();
        logger.info("Кликнуто на первую карточку курса");
    }
}
