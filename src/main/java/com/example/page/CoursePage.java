package com.example.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoursePage {
    private static final Logger logger = LogManager.getLogger(CoursePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public CoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void verifyCoursePage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
        WebElement courseTitle = driver.findElement(By.cssSelector("h1"));
        assertTrue(courseTitle.isDisplayed(), "Заголовок страницы курса не отображается");
        logger.info("Заголовок страницы курса отображается: {}", courseTitle.getText());

        WebElement courseDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sc-s2pydo-3.jfNqTr.dZDxRw > p")));
        assertTrue(courseDescription.isDisplayed(), "Описание курса не отображается");
        logger.info("Описание курса отображается: {}", courseDescription.getText());
    }

    public void verifyCourseDuration() {
        WebElement durationElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[2]/p"));
        String text = durationElement.getText();
        logger.info("Текст элемента с информацией о длительности курса: {}", text);
        assertEquals("5 месяцев", text, "Текст не соответствует ожидаемому: " + text);
    }

    public void verifyCourseFormat() {
        WebElement formElement = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/main/div/section/div[3]/div/div[3]/p"));
        String format = formElement.getText();
        logger.info("Текст элемента с информацией о форме обучения: {}", format);
    }
}
