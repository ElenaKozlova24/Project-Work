package com.example.tests;

import com.example.page.HomePage;
import com.example.page.CalendarPage;
import com.example.page.CoursePage;
import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.fail;

public class InfoAboutCoursesTest {
    private static final Logger logger = LogManager.getLogger(InfoAboutCoursesTest.class);
    private WebDriver driver;
    private HomePage homePage;
    private CoursePage coursePage;
    private CalendarPage calendarPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = WebDriverFactory.createNewDriver("chrome", options);
        homePage = new HomePage(driver);
        coursePage = new CoursePage(driver);
        calendarPage = new CalendarPage(driver);
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

        homePage.open("https://otus.ru/");

        homePage.clickOnTestingElement();

        int cardCount = homePage.getCourseCardCount();
        logger.info("Количество карточек: " + cardCount);

        if (cardCount > 0) {
            homePage.clickOnFirstCourseCard();
            coursePage.verifyCoursePage();
            coursePage.verifyCourseDuration();
            coursePage.verifyCourseFormat();

            calendarPage.navigateToCalendar();
            calendarPage.verifyEventCards();
        } else {
            fail("Нет карточек курсов");
        }

        logger.info("Тест завершен успешно");
    }
}
