package Utility;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;

import Files.resources;
import Suite1.OAuth;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class ReUsableMethods extends OAuth{
	public static XmlPath rawToXML(Response r) {
		String respString = r.asString();
		XmlPath x = new XmlPath(respString);
		return x;
	}

	public static JsonPath rawToJSON(Response r) {
		String respString = r.asString();
		JsonPath x = new JsonPath(respString);
		return x;

	}

	public static String CreatingSession() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				"D:\\Madhu\\Workspace\\SampleProject\\src\\main\\java\\Files\\env.properties");
		prop.load(fis);
		RestAssured.baseURI = prop.getProperty("JIRAHOST");
		Response res = given().header("Content-Type", "application/json")
				.body("{ \"username\": \""+username+"\", \"password\": \"\"+password+\"\" }").when()
				.post(resources.JIRASessionPostData()).then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).and().extract().response();
		JsonPath js = ReUsableMethods.rawToJSON(res);
		String cookie = js.get("session.value");
		System.out.println("Successfully created the Cookie : " + cookie);
		return cookie;
	}

}
