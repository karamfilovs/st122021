import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class ItemPageTest {
    private static final String BASE_URL = "Enter your base url here";  //example https://st2016.inv.bg
    private static final String ITEM_PAGE_URL = "/objects/manage";
    private static final String EMAIL = "Enter your email here";
    private static final String PASSWORD = "Enter your password here";
    private WebDriver driver = null;

    @BeforeAll //This happens before all tests
    static void beforeAll() {
        WebDriverManager.chromedriver().setup(); //This will download and configure web driver for chrome browser
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("Starting test with name:" + testInfo.getDisplayName()); //Prints the test name
        System.out.println("Starting chrome browser");
        driver = new ChromeDriver(); //Creates new chrome driver instance
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
    @DisplayName("Can search for existing items by name - full match")
    void canSearchForExistingItemsByNameFullMatch() {
        String itemName = "Created via API2";
        Item item = new Item(itemName, "30", "gr."); //Configuring java object which represents item
        ItemAPI.createItem(item); //Creates the item using API call
        login(); //Successful login
        driver.navigate().to(BASE_URL + ITEM_PAGE_URL); //Navigates to item page
        WebElement itemPageHeadline = driver.findElement(By.xpath("//div[@id='headline2']//h2")); //Locates the item page headline
        Assertions.assertEquals("Артикули", itemPageHeadline.getText()); //checks the headline text
        //Search for existing item
        WebElement expandSearchButton = driver.findElement(By.id("searchbtn")); //Locates expand search button
        expandSearchButton.click(); //Clicks the expand search button
        WebElement itemNameField = driver.findElement(By.name("nm")); //Locates the item name field
        itemNameField.sendKeys(itemName); //Enters text in the name field
        WebElement searchButton = driver.findElement(By.name("s")); //Locates the search button
        searchButton.click();  //Clicks the search button
        WebElement tableRow = driver.findElement(By.cssSelector("a.faktura_id")); //Locates the table row
        Assertions.assertTrue(tableRow.getText().contains(itemName), "The item name was not found");
    }



    @Test
    @DisplayName("Can search for existing items - partial match")
    void canSearchItemsPartialMatch(){
        String itemName = "Very good coffee";
        Item item = new Item(itemName, "30", "gr."); //Configuring java object which represents item
        ItemAPI.createItem(item); //Creates the item using API call
        login(); //Successful login
        driver.navigate().to(BASE_URL + ITEM_PAGE_URL); //Navigates to Item page
        WebElement itemPageHeadline = driver.findElement(By.xpath("//div[@id='headline2']//h2")); //Locates the item page headline
        Assertions.assertEquals("Артикули", itemPageHeadline.getText()); //Checks the headline text
        //Search for existing item
        WebElement expandSearchButton = driver.findElement(By.id("searchbtn")); //Locates expand search button
        expandSearchButton.click(); //Clicks the expand search button
        WebElement itemNameField = driver.findElement(By.name("nm")); //Locates item name field
        itemNameField.sendKeys("Very"); //Enters text in the name field
        WebElement searchButton = driver.findElement(By.name("s")); //Locates the trigger search button
        searchButton.click(); //Clicks the search button
        WebElement tableRow = driver.findElement(By.cssSelector("a.faktura_id")); //Locates the first effective(not the column name) table row
        Assertions.assertTrue(tableRow.getText().contains(itemName), "The item name was not found"); //Checks the text inside table row
        //Search for existing item
        driver.navigate().to(BASE_URL + ITEM_PAGE_URL);
        WebElement expandSearchButton2 = driver.findElement(By.id("searchbtn"));
        expandSearchButton2.click();
        WebElement itemNameField2 = driver.findElement(By.name("nm"));
        itemNameField2.sendKeys("good");
        WebElement searchButton2 = driver.findElement(By.name("s"));
        searchButton2.click();
        WebElement tableRow2 = driver.findElement(By.cssSelector("a.faktura_id"));
        Assertions.assertTrue(tableRow2.getText().contains(itemName), "The item name was not found");
    }


    @Test
    @DisplayName("Correct text is displayed if no items are found in the table") //Все още нямате добавени артикули.
    void correctTextIsDisplayedIfNoItemsExist(){
        //Homework
        //Add the other steps here
        WebElement emptyListMessage = driver.findElement(By.id("emptylist")); //Locates the empty list message
        //Add assert here to make sure the message text is OK
    }

    @Test
    @DisplayName("Can search items by price")
    void canSearchItemsByPrice(){
        //Homework
        //Add the other steps here


        //This section enters the price range from - to without clicking the search button
        WebElement priceFrom  = driver.findElement(By.name("pr1")); //Locates the price from
        priceFrom.sendKeys("10"); //Enters specific price
        WebElement priceTo  = driver.findElement(By.name("pr2")); //Locates the price to
        priceFrom.sendKeys("20"); //Enters specific price
        //Add assert to make sure the text in the row found is as expected
    }







    private void login() {
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
}
