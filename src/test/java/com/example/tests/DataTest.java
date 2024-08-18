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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataTest {
    private static final Logger logger = LogManager.getLogger(DataTest.class);
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
        String url = "https://otus.ru/";
        driver.get(url);
        logger.info("Открыт URL: {}", url);


        // Найти и кликнуть на элемент с текстом "Обучение"

        WebElement trainingElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='Обучение' and text()='Обучение']"))
        );
        trainingElement.click();

        // Дождаться появления выпадающего списка
        WebElement dropdownElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='sc-i28ik1-0 bmVffP sc-1youhxc-0 dwrtLP']"))
        );

        // Найти и кликнуть на элемент "Календарь мероприятий" в выпадающем списке
        WebElement calendarElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='sc-1pgqitk-0 dNitgt' and @href='https://otus.ru/events/near']"))
        );
        calendarElement.click();

        // Подождите, пока загрузятся карточки мероприятий
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));

        // Найти все карточки мероприятий на странице
        List<WebElement> eventCards = driver.findElements(By.cssSelector("div.dod_new-event-content"));

        // Проверить, что карточки отображаются
        for (WebElement eventCard : eventCards) {
            assertTrue(eventCard.isDisplayed(), "Карточка мероприятия не отображается");
            logger.info("Карточка мероприятия отображается");

            // Найти все элементы с датой внутри карточки
            List<WebElement> dateElements = eventCard.findElements(By.cssSelector("span.dod_new-event__date-text"));

            // Проверить, что каждый элемент с датой содержит текст и что дата больше 16 августа
            for (WebElement dateElement : dateElements) {
                assertTrue(dateElement.isDisplayed(), "Элемент с датой не отображается");
                String dateText = dateElement.getText();
                assertTrue(!dateText.isEmpty(), "Элемент с датой не содержит текст");
                logger.info("Элемент с датой отображается и содержит текст: {}", dateText);

                // Проверить, что дата больше 16 августа
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
        }

        logger.info("Тест завершен успешно");
    }
}
