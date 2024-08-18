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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfoAboutCoursesTest {
    private static final Logger logger = LogManager.getLogger(InfoAboutCoursesTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = WebDriverFactory.createNewDriver("chrome", options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

        openSite("https://otus.ru/");
        clickOnTestingElement();
        int cardCount = getCourseCardCount();
        System.out.println("Количество карточек: " + cardCount);

        if (cardCount > 0) {
            clickOnFirstCourseCard();
            verifyCoursePage();
            verifyCourseDuration();
            verifyCourseFormat();
            navigateToCalendar();
            verifyEventCards();
            filterWebinars();
            verifyWebinarCards();
        }
    }

    private void verifyWebinarCards() {
    }

    private void filterWebinars() {
    }

    private void openSite(String url) {
        driver.get(url);
        logger.info("Открыт URL: {}", url);
    }

    private void clickOnTestingElement() {
        WebElement testingElement = driver.findElement(By.xpath("//div[text()='Тестирование']"));
        testingElement.click();
    }

    private int getCourseCardCount() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-18q05a6-1.bwGwUO")));
        List<WebElement> cards = container.findElements(By.tagName("a"));
        return cards.size();
    }

    private void clickOnFirstCourseCard() {
        WebElement firstCourseCard = driver.findElement(By.cssSelector(".sc-18q05a6-1.bwGwUO a"));
        firstCourseCard.click();
        logger.info("Кликнуто на первую карточку курса");
    }

    private void verifyCoursePage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
        WebElement courseTitle = driver.findElement(By.cssSelector("h1"));
        assertTrue(courseTitle.isDisplayed(), "Заголовок страницы курса не отображается");
        logger.info("Заголовок страницы курса отображается: {}", courseTitle.getText());

        WebElement courseDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sc-s2pydo-3.jfNqTr.dZDxRw > p")));
        assertTrue(courseDescription.isDisplayed(), "Описание курса не отображается");
        logger.info("Описание курса отображается: {}", courseDescription.getText());

        //String expectedTitle = "Python QA Engineer";
       // String expectedDescription = "Курс по автоматизации тестирования на Python: освойте фреймворк PyTest, автоматизируйте тесты UI и API";
       // assertEquals(expectedTitle, courseTitle.getText(), "Название курса не соответствует ожидаемому");
       // assertEquals(expectedDescription, courseDescription.getText(), "Описание курса не соответствует ожидаемому");
    }

    private void verifyCourseDuration() {
        WebElement durationElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[2]/p"));
        String text = durationElement.getText();
        logger.info("Текст элемента с информацией о длительности курса: {}", text);
        assertEquals("5 месяцев", text, "Текст не соответствует ожидаемому: " + text);
    }

    private void verifyCourseFormat() {
        WebElement formElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[3]/p"));
        String format = formElement.getText();
        logger.info("Текст элемента с информацией о форме обучения: {}", format);
    }

    private void navigateToCalendar() {
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
    }

    private void verifyEventCards() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.dod_new-event-content")));
        List<WebElement> eventCards = driver.findElements(By.cssSelector("div.dod_new-event-content"));

        int i = 0;
        for (WebElement eventCard : eventCards) {
            i++;
            try {
                assertTrue(eventCard.isDisplayed(), "Карточка мероприятия не отображается");
            } catch (Exception e) {
                System.out.println("i = " + i);
                System.out.println("error : " + e);
            }
            logger.info("Карточка мероприятия отображается");

            List<WebElement> dateElements = eventCard.findElements(By.cssSelector("span.dod_new-event__date-text"));

            assertTrue(dateElements.get(0).isDisplayed(), "Элемент с датой не отображается");
            String dateText = dateElements.get(0).getText();
            assertTrue(!dateText.isEmpty(), "Элемент с датой не содержит текст");
            logger.info("Элемент с датой отображается и содержит текст: {}", dateText);

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

            // Найдите элемент с title="Открытый вебинар" и кликните на него
            WebElement webinarLink = driver.findElement(By.xpath("//a[@title='Открытый вебинар']"));
           webinarLink.click();

            List<WebElement> eventCards1 = driver.findElements(By.cssSelector("div.dod_new-event-content"));

            // Проверьте, что на каждой карточке указанно "Открытые вебинары"
            for (WebElement eventCard1 : eventCards1) {
                WebElement typeText = eventCard1.findElement(By.cssSelector("div.dod_new-type__text"));
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
