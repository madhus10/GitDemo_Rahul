package Suite1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileInputStream;
import Files.Payload;
import Files.resources;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class Basics3_GMapPostDeleteTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics3_GMapPostDeleteTest.class.getName());

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
	public void postdeltedataGoogleAPI() {
		log.info("HOST Information: " + prop.getProperty("HOST"));
		RestAssured.baseURI = prop.getProperty("HOST");
		// Task 1: Grab the response

		Response response = given().queryParam("key", prop.getProperty("key")).body(Payload.postpayload()).when()
				.post(resources.PlacePostdata()).andReturn().then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).and().body("status", equalTo("OK")).and().extract().response();

		// Task 2:Grab placeid from response

		String responseString = response.asString();
		JsonPath js = new JsonPath(responseString);
		String placeid = js.get("place_id");
		log.info(placeid);

		// Task 3: place the place id in delete Request

		String s1 = "{\r\n" + "    \"place_id\":\"" + placeid + "\"           \r\n" + "}\r\n" + "";
		System.out.println(s1);
		given().queryParam("key", prop.getProperty("key")).body(s1).when().post(resources.Placedeletedata()).then()
				.assertThat().statusCode(200).and().body("status", equalTo("OK"));
		log.debug("Completed " + this.getClass().getName());

	}

}
