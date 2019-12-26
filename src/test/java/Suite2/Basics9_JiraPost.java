package Suite2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Files.resources;
import Suite1.Basics_GMapGetTest;
import Utility.ReUsableMethods;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Basics9_JiraPost {
	Properties prop = new Properties();
	public Logger log = LogManager.getLogger(Basics9_JiraPost.class.getName());
	
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
	public void JiraAPI() throws IOException {

		// Creating the issue and grabbing the issue id
		// Case 1
		RestAssured.baseURI = prop.getProperty("JIRAHOST");
		String jsonpostpayload = GenerateStringFromResource(prop.getProperty("pathCreatingIssueJson"));
		Response res = given().header("Content-Type", "application/json")
				.header("Cookie", "JSESSIONID=" + ReUsableMethods.CreatingSession()).body(jsonpostpayload).when()
				.post(resources.JIRACreateIssuePostData()).then().assertThat().statusCode(201).and()
				.contentType(ContentType.JSON).and().extract().response();
		JsonPath js = ReUsableMethods.rawToJSON(res);
		String id = js.get("id");
		System.out.println("Successfully created the Issue with the id : " + id);

		// Adding the comments and grabbing the comment id
		// Case 2
		RestAssured.baseURI = prop.getProperty("JIRAHOST");
		String addcommentspayload = GenerateStringFromResource(prop.getProperty("pathAddingCommentsJson"));
		Response res1 = given().header("Content-Type", "application/json")
				.header("Cookie", "JSESSIONID=" + ReUsableMethods.CreatingSession()).body(addcommentspayload).when()
				.post(resources.JIRACreateIssuePostData() + "/" + id + "/comment").then().assertThat().statusCode(201)
				.and().contentType(ContentType.JSON).and().extract().response();
		JsonPath js1 = ReUsableMethods.rawToJSON(res1);
		String Commentid = js1.get("id");
		System.out.println("Successfully added the comments to the id : " + Commentid);

		// Updating the comments for the grabbed commentid
		// Case 3
		RestAssured.baseURI = prop.getProperty("JIRAHOST");
		String updatecommentspayload = GenerateStringFromResource(prop.getProperty("pathUpdateCommentsJson"));
		given().header("Content-Type", "application/json")
				.header("Cookie", "JSESSIONID=" + ReUsableMethods.CreatingSession()).body(updatecommentspayload).when()
				.put(resources.JIRACreateIssuePostData() + "/" + id + "/comment/" + Commentid).then().assertThat()
				.statusCode(200).and().contentType(ContentType.JSON).extract().response();
		System.out.println("Comments updated for the Comment id : " + Commentid);

		// Deleting the comments and grabbing the comment id
		// Case 4
		RestAssured.baseURI = prop.getProperty("JIRAHOST");
		given().header("Content-Type", "application/json")
				.header("Cookie", "JSESSIONID=" + ReUsableMethods.CreatingSession()).when()
				.delete(resources.JIRACreateIssuePostData() + "/" + id + "/comment/" + Commentid).then().assertThat()
				.statusCode(204).and().contentType(ContentType.JSON);
		System.out.println("Comments been deleted for the Comment id : " + Commentid);
		log.debug("Completed " + this.getClass().getName());

	}

	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));

	}

}
