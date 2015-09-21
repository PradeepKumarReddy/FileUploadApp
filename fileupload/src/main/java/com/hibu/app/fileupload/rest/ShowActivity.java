package com.hibu.app.fileupload.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.core.util.Base64;

public class ShowActivity {

	/*
	 * private final static String JSON_DATA = "{" + "  \"geodata\": [" +
	 * "    {" "	+ " \"id\": \"1\"," + "      \"name\": \"Julie Sherman\","
	 * "	+ " \"gender\" : \"female\"," "	+ "
	 * \"latitude\" : \"37.33774833333334\"," "	+ "
	 * \"longitude\" : \"-121.88670166666667\"" + "    }," "	+ " {" + "
	 * \"id\": \"2\"," "	+ " \"name\": \"Johnny Depp\"," "	+ "
	 * \"gender\" : \"male\"," "	+ " \"latitude\" : \"37.336453\"," "	+ "
	 * \"longitude\" : \"-121.884985\"" + "    }" + "  ]" + "}";
	 */

	/*
	 * public static void main(final String[] argv) throws JSONException {
	 * "final JSONObject obj = new JSONObject(JSON_DATA);
	 * "final JSONArray geodata = obj.getJSONArray("geodata"); "final int n =
	 * geodata.length(); "for (int i = 0; i < n; ++i) { " final JSONObject
	 * person = geodata.getJSONObject(i);
	 * "	System.out.println(person.getInt("id"));
	 * "	System.out.println(person.getString("name"));
	 * "	System.out.println(person.getString("gender"));
	 * "	System.out.println(person.getDouble("latitude"));
	 * "	System.out.println(person.getDouble("longitude")); "} }
	 */

	public static void main(final String[] argv) throws JSONException {
		String filePath = "imageData1.txt";
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		File file = new File(classLoader.getResource(filePath).getFile());

		InputStream is;
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			is = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - " + e.getMessage());
		}
		final JSONObject obj = new JSONObject(crunchifyBuilder.toString());
		// final Object imageData = obj.get("imageData");

		byte[] imageBytes = Base64.decode((String) obj.get("imageData"));
		saveFile(imageBytes);
		//BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

	}

	// save uploaded file to a defined location on the server
	static void saveFile(byte[] imageBytes) {

		try {
			
			//convert array of bytes into file
		    FileOutputStream fileOuputStream = 
	                  new FileOutputStream("C:\\Users\\IB1272\\Desktop\\Upload_Files\\testing4.png"); 
		    fileOuputStream.write(imageBytes);
		    fileOuputStream.close();
		
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public void JSONParse(String JSON_DATA) throws JSONException {
		final JSONObject obj = new JSONObject(JSON_DATA);
		final JSONArray geodata = obj.getJSONArray("imageData");
		final int n = geodata.length();
		for (int i = 0; i < n; ++i) {
			final JSONObject person = geodata.getJSONObject(i);
			System.out.println(person.getInt("id"));
			System.out.println(person.getString("name"));
			System.out.println(person.getString("gender"));
			System.out.println(person.getDouble("latitude"));
			System.out.println(person.getDouble("longitude"));
		}
	}
}