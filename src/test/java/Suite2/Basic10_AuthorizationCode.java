package Suite2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import POJO.Api;
import POJO.GetCourses;
import POJO.WebAutomation;
import Suite1.Basics_GMapGetTest;
import Suite1.OAuth;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class Basic10_AuthorizationCode extends OAuth {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basic10_AuthorizationCode.class.getName());

	@BeforeTest
	public void getdetails() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\env.properties");
		log.debug("Started " + this.getClass().getName());
		prop.load(fis);
	}

	@Test
	public void geAutorizationCode() throws InterruptedException {

		// launching the brwser and getting the Authorixation Code
		System.setProperty("webdriver.chrome.driver", "D:\\Madhu\\Softwares\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(
				"https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&state=verifydss&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.xpath("//input[@type=\"email\"]")).sendKeys("pinkey.madhu@gmail.com");
		driver.findElement(By.xpath("//input[@type=\"email\"]")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys(password);
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		String CurrentUrl = driver.getCurrentUrl();
		String codevalue = CurrentUrl.split("&")[1];
		System.out.println(codevalue);
		// driver.quit();

		// Fetching the access code
		RestAssured.baseURI = prop.getProperty("ACCESS_TOKEN_URL");
		String accessTokenResponse = given().urlEncodingEnabled(false).queryParams("code", codevalue)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().post("/oauth2/v4/token").asString();
		System.out.println(accessTokenResponse);
		JsonPath x = new JsonPath(accessTokenResponse);
		String access_token = x.get("access_token");
		System.out.println(access_token);
		// Sending the Request

		RestAssured.baseURI = prop.getProperty("Redirect_URL");
		String resp = given().queryParam("access_token", access_token).when().get("/getCourse.php").asString();
		System.out.println(resp);

		// Using POJO Classes:

		// Sending the Request

		RestAssured.baseURI = prop.getProperty("Redirect_URL");
		GetCourses gc = given().queryParam("access_token", access_token).when().log().all().get("/getCourse.php")
				.as(GetCourses.class);
		System.out.println("LinkedIn value is : " + gc.getLinkedIn());
		System.out.println("Url value is : " + gc.getUrl());
		System.out.println("Instructor value is : " + gc.getInstructor());
		System.out.println("services value is : " + gc.getServices());
		System.out.println("expertise value is : " + gc.getExpertise());
		// get course Tite of API

		List<Api> lisCour = gc.getCourses().getApi();
		for (int i = 0; i < lisCour.size(); i++) {
			if (lisCour.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				String price = lisCour.get(i).getPrice();
				System.out.println(price);
			}
		}

		// print all the course title of WebAutmation
		List<WebAutomation> webcourses = gc.getCourses().getWebAutomation();
		for (int i = 0; i < webcourses.size(); i++) {
			System.out.println("Course Title of index " + i + " is" + webcourses.get(i).getCourseTitle());

		}
		log.debug("Completed " + this.getClass().getName());
	}

}
