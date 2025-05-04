package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookCopyTest extends SeleniumTestBase {

    @Test
    public void testAddAndRemoveBookCopy() {
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

        // 2. Получить количество копий ДО добавления
        String xpath = "//div[contains(@class, 'card') and .//h4[contains(text(), 'Экземпляры')]]" +
                "//table//tbody//tr[.//button[contains(@class, 'btn-danger')]]";
        List<WebElement> beforeCopies = driver.findElements(
                By.xpath(xpath)
        );
        int beforeCount = beforeCopies.size();

        // 3. Нажать кнопку "Добавить экземпляр"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("form[action*='/copies'] button.btn-success")));
        addButton.click();

        // 4. Подождать появления новой строки (ожидаем +1)
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath(xpath),
                beforeCount
        ));
        List<WebElement> afterAddCopies = driver.findElements(By.xpath(xpath));
        assertEquals(beforeCount + 1, afterAddCopies.size(),
                "Количество экземпляров должно увеличиться на 1"
        );

        // 5. Удалить только что добавленный экземпляр (последняя строка)
        WebElement lastRow = afterAddCopies.get(afterAddCopies.size() - 1);
        WebElement deleteButton = lastRow.findElement(By.cssSelector("form button.btn-danger"));
        deleteButton.click();

        // (опционально) подтверждаем alert, если он есть
        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (TimeoutException ignored) {}

        // 6. Подождать уменьшения количества строк обратно
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath(xpath),
                beforeCount
        ));
        List<WebElement> finalCopies = driver.findElements(By.xpath(xpath));
        assertEquals(beforeCount, finalCopies.size(), "Количество экземпляров должно вернуться к исходному");
    }
}
