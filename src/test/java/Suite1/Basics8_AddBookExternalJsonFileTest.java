package Suite1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import Files.resources;
import Files.Payload;
import Utility.ReUsableMethods;

public class Basics8_AddBookExternalJsonFileTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics8_AddBookExternalJsonFileTest.class.getName());

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
	public void postdataAddBookExternalJsonFile() throws IOException {
		RestAssured.baseURI = prop.getProperty("LibraryHOST");
		String jsonpostpayload = GenerateStringFromResource(prop.getProperty("pathJson"));
		Response res = given().header("Content-Type", "application/json").body(jsonpostpayload).when()
				.post(resources.PlacePostdataLibrary()).then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).and().extract().response();
		JsonPath js = ReUsableMethods.rawToJSON(res);
		String ID = js.get("ID");
		log.info("Successfully added the ID : " + ID);

		// Task to delete Request
		String s1 = "{\r\n" + " \r\n" + "\"ID\" : \"" + ID + "\"\r\n" + " \r\n" + "} \r\n" + "";
		given().header("Content-Type", "application/json").body(s1).when().post(resources.PlaceDeleteDataLibrary())
				.then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("msg", equalTo("book is successfully deleted"));
		log.info("Successfully deleted the ID : " + ID);
		log.debug("Completed " + this.getClass().getName());
	}

	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));

	}
}
