import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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





}
