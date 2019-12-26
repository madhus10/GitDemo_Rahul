package Suite1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.Payload;
import Files.resources;
import Utility.ExcelDataExtracion;
import Utility.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ExcelDrivenAdBook_DynamicPayloadHashMap {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics7_AddAndDeleteTest.class.getName());

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
	public void postdataAddAndDeleteBook() throws IOException {
		HashMap<String, Object> map = new HashMap();
		ExcelDataExtracion xdata = new ExcelDataExtracion();
		ArrayList list1 = xdata.getData("RestAddBook", "RestAssured");
		map.put("name", list1.get(1));
		map.put("isbn", list1.get(2));
		map.put("aisle", list1.get(3));
		map.put("author", list1.get(4));

		System.out.println(map);
		RestAssured.baseURI = prop.getProperty("LibraryHOST");
		Response res = given().header("Content-Type", "application/json").body(map).when()
				.post(resources.PlacePostdataLibrary()).then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).and().extract().response();
		System.out.println(res.asString());

		JsonPath js = ReUsableMethods.rawToJSON(res);
		String ID = js.get("ID");
		System.out.println(ID);
		log.info("Successfully added the ID : " + ID);

		// Task to delete Request
		String s1 = "{\r\n" + " \r\n" + "\"ID\" : \"" + ID + "\"\r\n" + " \r\n" + "} \r\n" + "";
		given().header("Content-Type", "application/json").body(s1).when().post(resources.PlaceDeleteDataLibrary())
				.then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("msg", equalTo("book is successfully deleted"));
		System.out.println("deleted the id"+ID);
		log.info("Successfully deleted the ID : " + ID);
		log.debug("Completed " + this.getClass().getName());

	}

}
