package Utility;

import java.io.IOException;
import java.util.ArrayList;

public class Sample1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ExcelDataExtracion ext = new ExcelDataExtracion();
		ArrayList a = ext.getData("Profile","TestData1");
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i));
		}
	}

}
