package ru.msu.cmc.webprac.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BorrowingBookTest extends SeleniumTestBase {

    @Test
    public void testBorrowBook() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Пользователь открывает страницу с выдачей книг читателям
        driver.get("http://localhost:8080/");
        WebElement bookButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/borrowings/borrow']"))
        );
        bookButton.click();

        // 2. Пользователь находит нужного читателя
        WebElement readerCardNumInput = driver.findElement(By.id("readerCardNum"));
        readerCardNumInput.sendKeys("L12345");

        // 3. Выбирает книгу из списка доступных экземпляров (выбираем книгу "Братья Карамазовы")
        WebElement copyIdSelect = driver.findElement(By.id("copyId"));
        copyIdSelect.click();
        WebElement copyOption = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//option[contains(text(),'Братья Карамазовы')]"))
                );
        copyOption.click();

        // 4. Выбирает дату выдачи (по умолчанию сегодняшняя)

        WebElement submitButton = driver.findElement(By.id("submitButton"));
        submitButton.click();

        // Ожидание появления сообщения об успешной выдаче
        WebElement successMessage = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        // Система сохраняет информацию о выдаче и выводит сообщение с id выданного экземпляра
        String messageText = successMessage.getText();
        assertTrue(messageText.contains("Книга с ID экземпляра"),
                "Сообщение об успешной выдаче не отображается!"
        );
    }
}
