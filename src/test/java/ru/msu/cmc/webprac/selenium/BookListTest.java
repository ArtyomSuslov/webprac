package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BookListTest extends SeleniumTestBase {

    @Test
    public void testBookSearchFlow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Пользователь заходит на страницу со списком книг
        driver.get("http://localhost:8080/");

        // Находим и кликаем на кнопку "Книги"
        WebElement bookButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/books']"))
        );
        bookButton.click();

        // 2. Вводим параметры поиска (например, по названию книги)
        WebElement searchInput = driver.findElement(By.name("title"));
        searchInput.sendKeys("Капитанская дочка");

        // 3. Нажимаем кнопку поиска
        WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
        searchButton.click();

        // 4. Ожидаем появления таблицы с результатами поиска
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));

        // 5. Пользователь просматривает информацию о книгах (проверка наличия книги в таблице)
        WebElement table = driver.findElement(By.tagName("table"));
        Assertions.assertNotNull(table);

        // Перезагружаем таблицу, чтобы убедиться, что она содержит искомую книгу
        Assertions.assertTrue(table.getText().contains("Капитанская дочка"));
    }
}
