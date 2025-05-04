package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookDeletionTest extends SeleniumTestBase {

    @Test
    public void testDeleteBook() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Перейти на страницу книги "Тестовая книга"
        driver.get("http://localhost:8080");
        WebElement booksLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Книги")));
        booksLink.click();

        List<WebElement> rows = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("table tbody tr"))
        );
        WebElement targetRow = rows.stream()
                .filter(row -> row.findElement(By.cssSelector("td")).getText().contains("Тестовая книга"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Тестовая книга не найдена"));

        targetRow.findElement(By.linkText("Подробнее")).click();

        // 2. Нажать кнопку "Удалить книгу"
        WebElement deleteButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-danger"))
        );
        deleteButton.click();

        // 3. Подтверждение удаления
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();  // Подтверждаем удаление в вылезающем окне

        // 4. Ждем редирект на страницу со всеми книгами
        wait.until(ExpectedConditions.urlContains("/books"));

        // Проверяем, что мы действительно на странице со всеми книгами
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/books"), "Ожидаемый редирект на страницу списка книг не произошел.");

        // 5. Проверяем, что книга исчезла из списка
        rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("table tbody tr")));
        boolean isBookStillPresent = rows.stream()
                .anyMatch(row -> row.findElement(By.cssSelector("td")).getText().contains("Тестовая книга"));

        assertFalse(isBookStillPresent, "Книга должна была быть удалена и исчезнуть из списка.");
    }
}
