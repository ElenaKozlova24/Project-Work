package com.example.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalendarPage {
    private static final Logger logger = LogManager.getLogger(CalendarPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public CalendarPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToCalendar() {
        WebElement trainingElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='Обучение' and text()='Обучение']"))
        );
        trainingElement.click();

        WebElement dropdownElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='sc-i28ik1-0 bmVffP sc-1youhxc-0 dwrtLP']"))
        );

        WebElement calendarElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='sc-1pgqitk-0 dNitgt' and @href='https://otus.ru/events/near']"))
        );
        calendarElement.click();
        logger.info("Переход к календарю");
    }

    public void verifyEventCards() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));
        List<WebElement> eventCards = driver.findElements(By.cssSelector("div.dod_new-event-content"));

        int i = 0;
        for (WebElement eventCard : eventCards) {
            i++;
            try {
                assertTrue(eventCard.isDisplayed(), "Карточка мероприятия не отображается");
            } catch (Exception e) {
                logger.error("Ошибка при проверке отображения карточки мероприятия: {}", e.getMessage());
            }

            List<WebElement> dateElements = eventCard.findElements(By.cssSelector("span.dod_new-event__date-text"));

            assertTrue(dateElements.get(0).isDisplayed(), "Элемент с датой не отображается");
            String dateText = dateElements.get(0).getText();
            assertTrue(!dateText.isEmpty(), "Элемент с датой не содержит текст");

            try {
                LocalDate eventDate = LocalDate.parse(dateText + " " + LocalDate.now().getYear(), DateTimeFormatter.ofPattern("d MMMM yyyy"));
                LocalDate minDate = LocalDate.of(LocalDate.now().getYear(), 8, 16);
                if (eventDate.isAfter(minDate)) {
                    logger.info("Дата мероприятия больше 16 августа: {}", dateText);
                } else {
                    logger.info("Дата мероприятия меньше 16 августа: {}", dateText);
                }
            } catch (DateTimeParseException e) {
                logger.error("Ошибка парсинга даты: {}", dateText);
            }
        }

        WebElement dropdown = driver.findElement(By.className("dod_new-events-dropdown"));
        dropdown.click();

        WebElement webinarLink = driver.findElement(By.xpath("//a[@title='Открытый вебинар']"));
        webinarLink.click();

        List<WebElement> eventCards1 = driver.findElements(By.cssSelector("div.dod_new-event-content"));

        for (WebElement eventCard1 : eventCards1) {
            WebElement typeText = eventCard1.findElement(By.cssSelector("div.dod_new-type__text"));
            String text = typeText.getText();
            if (!"Открытые вебинары".equals(text)) {
                logger.warn("На карточке указано: {}", text);
            } else {
                logger.info("eventCard На карточке указано: Открытые вебинары");
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
