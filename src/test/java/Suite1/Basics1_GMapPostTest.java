package Suite1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import Files.resources;
import Files.Payload;

public class Basics1_GMapPostTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics1_GMapPostTest.class.getName());

	@BeforeTest
	public void getData() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\env.properties");
		log.debug("Started " + this.getClass().getName());
		try {
			prop.load(fis);
			log.info(" TestCases.Basics1_GMapPostTest Poperty File is been loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void postdataGoogleAPI() {
		RestAssured.baseURI = prop.getProperty("HOST");
		given().queryParam("key", prop.getProperty("key")).body(Payload.postpayload()).when()
				.post(resources.PlacePostdata()).then().assertThat().statusCode(200).and().contentType(ContentType.JSON)
				.and().body("status", equalTo("OK"));
		log.info("Post Request Passed");
		log.debug("Completed " + this.getClass().getName());

	}

}
