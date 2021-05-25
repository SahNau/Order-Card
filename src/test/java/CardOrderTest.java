import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.By.cssSelector;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    // На серверах сборки чаще всего нет графического интерфейса, поэтому запуская браузер в режиме headless мы отключаем графический интерфейс (при этот все процессы браузера продолжают работать так же)
    // Включение headless режима при использовании selenium необходимо реализовать в коде во время создания экземпляра webdriver:
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    // закрываем браузер (очищаем ресурсы)
    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    // заявка отправляется успешно
    @Test
    void shouldTestSuccess() {
        driver.get("http://localhost:7777");
        // указываем элемент, который будет основой для теста (форму заявки всю)
        WebElement form = driver.findElement(cssSelector("form[class='form form_size_m form_theme_alfa-on-white']"));
        // даём значения `data-test-id` и внутри него ищите нужный  `input` - используем вложенность для селекторов. В данном случаем строка имя и фамилия имеет идентификатор NAME
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иван Андреевич");
        // тут используем индификатор phone
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+79325450351");
        // добавляем элемент чекбокса
        driver.findElement(cssSelector("[data-test-id=agreement] ")).click();
        // делаем клик на кнопку отправить, он у нас имеет значение span и идентификатор button
        driver.findElement(cssSelector("span[class=button__text]")).click();
        // получаем окно с текстом о заявке
        String message = driver.findElement(cssSelector("[data-test-id='order-success']")).getText();
        // ожидаемый результат
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.strip());
    }

    // заявка не отправится, т.к неверные данные в поле ФИО/Имя
    @Test
    void shouldTestFailedName() {
        driver.get("http://localhost:7777");
        WebElement form = driver.findElement(cssSelector("form[class='form form_size_m form_theme_alfa-on-white']"));
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Ivan Andreevich");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("+79325450351");
        driver.findElement(cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(cssSelector("span[class=button__text]")).click();
        String message = driver.findElement(cssSelector("span [class=input__sub]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", message.strip());
    }

    // заявка не отправится, т.к неверные данные в поле Телефон
    @Test
    void shouldTestFailedPhone() {
        driver.get("http://localhost:7777");
        WebElement form = driver.findElement(cssSelector("form[class='form form_size_m form_theme_alfa-on-white']"));
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иван Андреевич");
        driver.findElement(cssSelector("[data-test-id=phone] input")).sendKeys("79325450351");
        driver.findElement(cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(cssSelector("span[class=button__text]")).click();
        String message = driver.findElement(cssSelector("#root > div > form > div:nth-child(2) > span > span > span.input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.strip());
    }
}