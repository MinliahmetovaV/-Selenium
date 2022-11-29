package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditCardTest {
    private WebDriver driver;


    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @Test
    void shouldSendForm() {
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79043278563");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());

    }

    @Test
    void shouldNotSendFormWithEmptyFieldName() {
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79043278563");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSendFormWithEmptyPhone() {
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSendFormWithNameOnLatin() {
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("Will Smith");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79043278563");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendFormNameFilledWithNumbers() {
        driver.findElement(By.cssSelector("span[data-test-id='name'] input")).sendKeys("55555");
        driver.findElement(By.cssSelector("span[data-test-id='phone'] input")).sendKeys("+79043278563");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendFormNameFilledSpace() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(" ");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+790432708596");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldSendFormWithDoubleName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сергей Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79043270859");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSendFormNameFilledSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("!#$");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+790432708596");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendFormWithPhoneFilled10Digits() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7904327085");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSendFormWithPhoneFilled12Digits() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+790432708581");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSendFormWithPhoneFilledLetters() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7904327аар");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSendFormWithPhoneFilledSpace() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(" ");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSendFormWithPhoneFilledSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("!№?");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSendFormWithOutClickAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79043270856");
        driver.findElement(By.cssSelector(".button_view_extra")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid ")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }


    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }


}
