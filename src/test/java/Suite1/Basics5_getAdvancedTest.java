package Suite1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
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
import Utility.ReUsableMethods;

public class Basics5_getAdvancedTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics5_getAdvancedTest.class.getName());

	@BeforeTest
	public void getData() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\env.properties");
		log.debug("Started " + this.getClass().getName());
		try {
			prop.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void getdataGoogleAPIResultValues() {

		RestAssured.baseURI = prop.getProperty("GoogleHOST");
		Response resp = given().param("location", "-33.867052,151.1957362").param("radius", "1500")
				.param("key", prop.getProperty("getkey")).log().all().when().get(resources.Placegetdata()).then()
				.assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				// .body("results[6].name", equalTo("The Sebel Quay West Suites Sydney")).and()
				.header("Server", "scaffolding on HTTPServer2").log().body().extract().response();
		JsonPath js = ReUsableMethods.rawToJSON(resp);
		log.info("Size of the status array is : " + js.get("results.size()"));
		int count = js.get("results.size()");
		for (int i = 0; i < count; i++) {
			log.info("Name in " + i + "   index is : ");
			log.info(js.get("results[" + i + "].name"));

		}
		log.debug("Completed " + this.getClass().getName());

	}

}
