package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReturnBookTest extends SeleniumTestBase {

    @Test
    public void testReturnBook() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Перейти на страницу возврата книг
        driver.get("http://localhost:8080/borrowings/return");

        // 2. Найти поле поиска и ввести номер читательского билета
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("query")));
        searchInput.sendKeys("L12345");

        // 3. Нажать кнопку "Поиск"
        WebElement searchButton = driver.findElement(By.cssSelector("form button[type='submit']"));
        searchButton.click();

        // 4. Дождаться появления таблицы с результатами
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.table")));

        // 5. Найти строку с книгой "Братья Карамазовы"
        List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
        WebElement targetRow = null;
        for (WebElement row : rows) {
            WebElement titleElement = row.findElement(By.cssSelector("td:first-child span"));
            if (titleElement.getText().contains("Братья Карамазовы")) {
                targetRow = row;
                break;
            }
        }

        assertTrue(targetRow != null, "Не найдена книга 'Братья Карамазовы' для возврата!");

        // 6. Нажать кнопку "Принять"
        WebElement returnForm = targetRow.findElement(By.tagName("form"));
        WebElement submitButton = returnForm.findElement(By.tagName("button"));
        submitButton.click();

        // 7. Проверка flash-сообщения
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("alert-success"))
        );
        String messageText = successMessage.getText();
        assertTrue(messageText.contains("Книга с ID экземпляра"), "Сообщение об успешном возврате не найдено!");
    }
}
