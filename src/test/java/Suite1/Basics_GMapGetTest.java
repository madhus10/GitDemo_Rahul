package Suite1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import Files.resources;

public class Basics_GMapGetTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics_GMapGetTest.class.getName());

	@BeforeTest
	public void getData() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\env.properties");
		log.debug("Started " + this.getClass().getName());
		try {
			prop.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void getDataGoogleAPI() {
		log.info("HOST Information: " + prop.getProperty("GoogleHOST"));
		RestAssured.baseURI = prop.getProperty("GoogleHOST");
		given().param("location", "-33.867052,151.1957362").param("radius", "1500")
				.param("key", prop.getProperty("getkey")).when().get(resources.Placegetdata()).then().assertThat()
				.statusCode(200).and().contentType(ContentType.JSON).and()
				.body("results[6].name", equalTo("The Sebel Quay West Suites Sydney")).and()
				.header("Server", "scaffolding on HTTPServer2").extract().response();
		log.debug("Completed " + this.getClass().getName());

	}

}
