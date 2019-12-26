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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import Files.resources;
import Files.Payload;
import Utility.ReUsableMethods;

public class Basics4_GMapPostXMLTest {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics4_GMapPostXMLTest.class.getName());

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
	public void postdataGoogleAPIXML() throws IOException {
		RestAssured.baseURI = prop.getProperty("HOST");
		String xmlpostpayload = GenerateStringFromResource(prop.getProperty("pathXml"));
		Response res = given().queryParam("key", prop.getProperty("key")).body(xmlpostpayload).when()
				.post(resources.PlacePostdataxml()).then().assertThat().statusCode(200).and()
				.contentType(ContentType.XML).extract().response();
		XmlPath x = ReUsableMethods.rawToXML(res);
		log.info(x.get("response.status"));
		log.debug("Completed " + this.getClass().getName());

	}

	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));

	}

}
