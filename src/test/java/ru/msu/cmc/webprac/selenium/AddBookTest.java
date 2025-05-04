package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBookTest extends SeleniumTestBase {

    @Test
    public void testAddBook() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Перейти на главную страницу
        driver.get("http://localhost:8080");

        // 2. Перейти в раздел "Книги" через навигацию
        WebElement booksLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Книги")));
        booksLink.click();

        // 3. Нажать "Добавить книгу"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-success")));
        addButton.click();

        // 4. Заполнить форму добавления книги
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
        driver.findElement(By.id("title")).sendKeys("Тестовая книга");
        driver.findElement(By.id("authors")).sendKeys("Тестов Т.Т.");
        driver.findElement(By.id("publisher")).sendKeys("Издательство Тест");
        driver.findElement(By.id("year")).sendKeys("2007");
        driver.findElement(By.id("isbn")).sendKeys("9876543210123");

        // 5. Отправить форму
        driver.findElement(By.cssSelector("form button[type='submit']")).click();

        // 6. Дождаться редиректа на /books
        wait.until(ExpectedConditions.urlContains("/books"));

        // 7. Проверить, что книга появилась в списке (по названию)
        List<WebElement> titles = driver.findElements(By.xpath("//table//tr/td[1]"));
        boolean found = titles.stream().anyMatch(td -> td.getText().contains("Тестовая книга"));
        assertTrue(found, "Добавленная книга не найдена в списке");
    }
}
