import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class LoginPageTest {
    private static final String BASE_URL = "enter your login url";  //example https://st2016.inv.bg
    private static final String EMAIL = "enter your email here";
    private static final String PASSWORD = "enter your password";
    private WebDriver driver = null;

    @BeforeAll //This happens before all tests
    static void beforeAll() {
        WebDriverManager.chromedriver().setup(); //This will download and configure chrome web driver
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("Starting test with name:" + testInfo.getDisplayName());
        System.out.println("Starting chrome browser");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false); //This tells selenium to run the browser in headless mode based on boolean flag
        driver = new ChromeDriver(options); //Creates new chrome driver instance
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //Implicitly waits 10 seconds for elements to appear
        driver.manage().window().maximize(); //Maximizes browser window
        System.out.println("Navigating to:" + BASE_URL);
        driver.navigate().to(BASE_URL); //Navigates to Login page
    }

    @AfterEach
    void afterEach() {
        System.out.println("Closing chrome browser instance");
        driver.quit(); //Closes the chrome driver instance
    }

    @Test
    @DisplayName("Can login with valid credentials")
    void canLoginWithValidCredentials() {
        WebElement heading = driver.findElement(By.cssSelector("h1")); //Locates the heading 1 element
        Assertions.assertEquals("Вход в inv.bg", heading.getText()); //Make sure h1 text is OK
        WebElement emailField = driver.findElement(By.id("loginusername")); //Locates the email field
        emailField.sendKeys(EMAIL); //Enters email
        WebElement passwordField = driver.findElement(By.name("password")); //Locates the password field
        passwordField.sendKeys(PASSWORD); //Enters password
        WebElement loginButton = driver.findElement(By.id("loginsubmit")); //Locates the login button
        loginButton.click(); //Clicks Login button
        WebElement homepageHeadline = driver.findElement(By.xpath("//div[@id='headline']//h2"));
        Assertions.assertEquals("Система за фактуриране", homepageHeadline.getText()); //Checks the headline text
        WebElement userPanel = driver.findElement(By.cssSelector("div.userpanel-header")); //Locates the user panel section
        Assertions.assertEquals(EMAIL, userPanel.getText()); //Checks the logged user (user panel)
    }


    @Test
    @DisplayName("Can logout successfully")
    void canLogoutSuccessfully() {
        //Login in the system
        WebElement heading = driver.findElement(By.cssSelector("h1")); //Locates the heading 1 element
        Assertions.assertEquals("Вход в inv.bg", heading.getText()); //Make sure h1 text is OK
        WebElement emailField = driver.findElement(By.id("loginusername")); //Locates the email field
        emailField.sendKeys(EMAIL); //Enters email
        WebElement passwordField = driver.findElement(By.name("password")); //Locates the password field
        passwordField.sendKeys(PASSWORD); //Enters password
        WebElement loginButton = driver.findElement(By.id("loginsubmit")); //Locates the login button
        loginButton.click(); //Clicks Login button
        WebElement homepageHeadline = driver.findElement(By.xpath("//div[@id='headline']//h2"));
        Assertions.assertEquals("Система за фактуриране", homepageHeadline.getText()); //Checks the headline text
        WebElement userPanel = driver.findElement(By.cssSelector("div.userpanel-header")); //Locates the user panel section
        Assertions.assertEquals(EMAIL, userPanel.getText()); //Checks the logged user (user panel)
        //Logout
        userPanel.click(); //Clicks the user panel so that the logout link is visible
        WebElement logoutLink = driver.findElement(By.cssSelector("a.selenium-button-logout")); //Locates the hyperlink for logout
        logoutLink.click(); //Clicks the logout link
        WebElement logoutSuccess = driver.findElement(By.id("okmsg"));
        Assertions.assertEquals("Вие излязохте от акаунта си.", logoutSuccess.getText().trim());

    }


    @Test
    @DisplayName("Cant login with blank credentials")
    void cantLoginWithBlankCredentials() {
        WebElement heading = driver.findElement(By.cssSelector("h1")); //Locates the heading 1 element
        Assertions.assertEquals("Вход в inv.bg", heading.getText()); //Make sure h1 text is OK
        WebElement loginButton = driver.findElement(By.id("loginsubmit")); //Locates the login button
        loginButton.click(); //Clicks Login button
        WebElement loginError = driver.findElement(By.cssSelector("div.selenium-error-block")); //Locates the error message
        Assertions.assertEquals("Моля, попълнете вашия email", loginError.getText().trim()); //Checks the error message
    }


    @Test
    @DisplayName("Cant login with invalid password")
    void cantLoginWithInvalidPassword() {
        WebElement heading = driver.findElement(By.cssSelector("h1")); //Locates the heading 1 element
        Assertions.assertEquals("Вход в inv.bg", heading.getText()); //Make sure h1 text is OK
        WebElement emailField = driver.findElement(By.id("loginusername")); //Locates the email field
        emailField.sendKeys(EMAIL); //Enters email
        WebElement passwordField = driver.findElement(By.name("password")); //Locates the password field
        passwordField.sendKeys(PASSWORD + 1); //Enters invalid password 1234561
        WebElement loginButton = driver.findElement(By.id("loginsubmit")); //Locates the login button
        loginButton.click(); //Clicks Login button
        WebElement loginError = driver.findElement(By.cssSelector("div.selenium-error-block")); //Locates the error message
        Assertions.assertEquals("Грешно потребителско име или парола. Моля, опитайте отново.", loginError.getText().trim()); //Checks the error message
    }

    @Test
    @DisplayName("Cant login with not-existing user")
    void cantLoginWithNotExistingUser() {
        WebElement heading = driver.findElement(By.cssSelector("h1")); //Locates the heading 1 element
        Assertions.assertEquals("Вход в inv.bg", heading.getText()); //Make sure h1 text is OK
        WebElement emailField = driver.findElement(By.id("loginusername")); //Locates the email field
        emailField.sendKeys("qa1@qaground.com"); //Enters not existing email address
        WebElement passwordField = driver.findElement(By.name("password")); //Locates the password field
        passwordField.sendKeys(PASSWORD); //Enters invalid password 1234561
        WebElement loginButton = driver.findElement(By.id("loginsubmit")); //Locates the login button
        loginButton.click(); //Clicks Login button
        WebElement loginError = driver.findElement(By.cssSelector("div.selenium-error-block")); //Locates the error message
        Assertions.assertEquals("Грешно потребителско име или парола. Моля, опитайте отново.", loginError.getText().trim()); //Checks the error message
    }
}
