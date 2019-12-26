package Files;

import java.net.URI;

public class resources {

	public static String PlacePostdata() {
		String postresources = "/maps/api/place/add/json";
		return postresources;
	}
	public static String PlacePostdataxml() {
		String postresources = "/maps/api/place/add/xml";
		return postresources;
	}


	public static String Placedeletedata() {
		String postresources = "/maps/api/place/delete/json";
		return postresources;
	}

	public static String Placegetdata() {
		String getresources = "/maps/api/place/nearbysearch/json";
		return getresources;
	}
	
	public static String PlacePostdataLibrary() {
		String postresources = "/Library/Addbook.php";
		return postresources;
	}
	public static String PlaceDeleteDataLibrary() {
		String postresources1 = "/Library/DeleteBook.php";
		return postresources1;
	}
	public static String JIRASessionPostData() {
		String postresources2 = "/rest/auth/1/session";
		return postresources2;
	}
	public static String JIRACreateIssuePostData() {
		String postresources3 = "/rest/api/2/issue";
		return postresources3;
	}

}
