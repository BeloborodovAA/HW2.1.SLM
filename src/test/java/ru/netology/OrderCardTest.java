package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); // Для CI обязательно раскомментировать!

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @Test
    public void shouldSubmitFormSuccessfully() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ивайкина Алина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        // 5. Проверяем сообщение об успехе
        WebElement successMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='order-success']")));

        assertTrue(successMessage.isDisplayed(), "Сообщение об успехе не появилось");
    }

    @Test
    public void shouldAcceptRussianLetterYo() {
        // Вводим имя с буквой "ё"
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Пётр Ёлкин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement successMessage = new WebDriverWait(driver, Duration.ofSeconds(4))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='order-success']")));

        assertTrue(successMessage.isDisplayed(), "Приложение не принимает букву 'ё' в имени!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}