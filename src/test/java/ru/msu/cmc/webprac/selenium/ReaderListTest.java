package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ReaderListTest extends SeleniumTestBase {

    @Test
    public void testReaderSearchAndViewDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Пользователь открывает страницу со списком читателей
        driver.get("http://localhost:8080/");
        WebElement bookButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/readers']"))
        );
        bookButton.click();

        // 2. Вводит параметры поиска (например, ФИО)
        WebElement nameInput = driver.findElement(By.name("fullName"));
        nameInput.sendKeys("Иван Иванов");

        WebElement searchBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        searchBtn.click();

        // 3. Система отображает список читателей
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr"))
        );

        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = rows.stream().anyMatch(row -> !row.getText().contains("Читатели не найдены"));
        Assertions.assertTrue(found, "Читатели не найдены");

        // 4. Пользователь может выбрать читателя для просмотра деталей
        WebElement detailsBtn = rows.get(0).findElement(By.linkText("Подробнее"));
        detailsBtn.click();

        // Проверяем, что открылся профиль читателя
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.urlMatches(".*/readers/\\d+")
        );
        Assertions.assertTrue(driver.getCurrentUrl().matches(".*/readers/\\d+"),
                "Не открылась страница деталей читателя");
    }
}

