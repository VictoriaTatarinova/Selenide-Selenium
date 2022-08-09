package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
//        System.setProperty("web-driver.chrome.driver", "./driver/chromedriver");
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    //    positive Test
    @Test
    void shouldSendForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79267777777");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);

    }

    //    negative Tests
    @Test
    void shouldSendLatinName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Petrov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79267777777");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        String actualText = driver.findElement(By.cssSelector("[data-test-id= name] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldSendEmptyFieldName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79267777777");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Поле обязательно для заполнения";

        String actualText = driver.findElement(By.cssSelector("[data-test-id= name] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldSendInvalidPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79267777");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        String actualText = driver.findElement(By.cssSelector("[data-test-id= phone] span.input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldSendWithoutAgreement() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79267777777");
        driver.findElement(By.cssSelector("[type=button]")).click(); //send
        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        String actualText = driver.findElement(By.cssSelector("[data-test-id= agreement].input_invalid.checkbox")).getText().trim();
        assertEquals(expectedText, actualText);


    }


}
