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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SizeTest {
    private static final Logger logger = LogManager.getLogger(InfoAboutCoursesTest.class);
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
    public void testClickOnTestingElementAndCheckCourseCount() {
        logger.info("Начало теста");

        // Откройте сайт
        String url = "https://otus.ru/";
        driver.get(url);
        logger.info("Открыт URL: {}", url);

        // Найдите элемент с текстом "Тестирование"
        WebElement testingElement = driver.findElement(By.xpath("//div[text()='Тестирование']"));

        // Кликните на элемент
        testingElement.click();

        // Подождите, пока загрузятся карточки курсов
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-18q05a6-1.bwGwUO")));

        // Найдите все карточки внутри этого элемента
        List<WebElement> cards = container.findElements(By.tagName("a"));

        // Посчитайте количество карточек
        int cardCount = cards.size();

        // Выведите количество карточек
        System.out.println("На странице отображаются карточки. Количество карточек: " + cardCount);

        // Кликнуть на первую карточку курса
        if (!cards.isEmpty()) {
            WebElement firstCourseCard = cards.get(0);
            firstCourseCard.click();
            logger.info("Кликнуто на первую карточку курса");

            // Подождите, пока загрузится страница курса
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));

            // Проверьте, что заголовок страницы курса отображается
            WebElement courseTitle = driver.findElement(By.cssSelector("h1"));
            assertTrue(courseTitle.isDisplayed(), "Заголовок страницы курса не отображается");
            logger.info("Заголовок страницы курса отображается: {}", courseTitle.getText());

            // Подождите, пока элемент с описанием курса станет видимым
            WebElement courseDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sc-s2pydo-3.jfNqTr.dZDxRw > p")));
            assertTrue(courseDescription.isDisplayed(), "Описание курса не отображается");
            logger.info("Описание курса отображается: {}", courseDescription.getText());

            // Проверьте, что название курса и описание соответствуют ожидаемым значениям
            String expectedTitle = "Python QA Engineer";
            String expectedDescription = "Курс по автоматизации тестирования на Python: освойте фреймворк PyTest, автоматизируйте тесты UI и API";
            assertEquals(expectedTitle, courseTitle.getText(), "Название курса не соответствует ожидаемому");
            assertEquals(expectedDescription, courseDescription.getText(), "Описание курса не соответствует ожидаемому");

            // Найдите элемент с информацией о длительности курса с использованием XPath
            WebElement durationElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[2]/p"));

            // Получите текст элемента
            String text = durationElement.getText();
            logger.info("Текст элемента с информацией о длительности курса: {}", text);

            // Проверьте, содержит ли текст "5 месяцев"
            assertEquals("5 месяцев", text, "Текст не соответствует ожидаемому: " + text);

            WebElement formElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[3]/p"));
            // Получите текст элемента
            String format = formElement.getText();
            logger.info("Текст элемента с информацией о форме обучения: {}", format);

            WebElement education = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__next\"]/div[1]/div[3]/div/nav/div[2]")));
            education.click();
            logger.info("Clicked on element by XPath");

            // Найти элемент с классом "sc-1pgqitk-0 dNitgt" и текстом "Календарь мероприятий" и кликнуть на него
            WebElement calendarLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='sc-1pgqitk-0 dNitgt' and text()='Календарь мероприятий']")));
            calendarLink.click();
            logger.info("Clicked on 'Календарь мероприятий' link");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));

            // Найти все карточки мероприятий на странице
            List<WebElement> eventCards = driver.findElements(By.cssSelector("div.dod_new-event-content"));

            // Проверить, что карточки отображаются
            for (WebElement eventCard : eventCards) {
                assertTrue(eventCard.isDisplayed(), "Карточка мероприятия не отображается");
                logger.info("Карточка мероприятия отображается");
            }
        }
    }
}
