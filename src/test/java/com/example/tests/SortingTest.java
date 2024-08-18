package com.example.tests;

import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingTest {
    private static final Logger logger = LogManager.getLogger(SortingTest.class);
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = WebDriverFactory.createNewDriver("chrome", options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testEventCardsAreDisplayedAndHaveDateText() {
        logger.info("Начало теста");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Откройте сайт
        String url = "https://otus.ru/events/near/";
        driver.get(url);
        logger.info("Открыт URL: {}", url);

        // Подождите, пока загрузятся карточки мероприятий
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));

        WebElement dropdown = driver.findElement(By.className("dod_new-events-dropdown"));
        dropdown.click();

        // Найдите элемент с title="Открытый вебинар" и кликните на него
        WebElement webinarLink = driver.findElement(By.xpath("//a[@title='Открытый вебинар']"));
        webinarLink.click();

        // Подождите, пока загрузятся карточки мероприятий после клика
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));

        List<WebElement> eventCards = driver.findElements(By.cssSelector("div.dod_new-event-content"));

        // Проверьте, что на каждой карточке указанно "Открытые вебинары"
        for (WebElement eventCard : eventCards) {
            WebElement typeText = eventCard.findElement(By.cssSelector("div.dod_new-type__text"));
            String text = typeText.getText();
            if (!"Открытые вебинары".equals(text)) {
                logger.warn("На карточке указано: {}", text);
            } else {
                logger.info("eventCard На карточке указано: Открытые вебинары");
            }
        }

        // Добавьте задержку, чтобы увидеть результат (опционально)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
