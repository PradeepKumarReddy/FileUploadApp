package com.hibu.app.fileupload.rest;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/multifiles")
//http://localhost:8098/fileupload/multiform.html
//http://localhost:8098/fileupload/rest/application.wadl
public class JerseyMultipartFileUpload {

	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://Users/IB1272/Desktop/Upload_Files/";

	/**
	 * Upload a File
	 */

	@POST
	@Path("/upload_old")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFileOld(FormDataMultiPart form) {
		System.out.println(" uploadFile " + form);
		 FormDataBodyPart filePart = form.getField("file");

		 ContentDisposition headerOfFilePart =  filePart.getContentDisposition();

		 InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		 String filePath = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();

		// save the file to the server
		saveFile(fileInputStream, filePath);

		String output = "File saved to server location using FormDataMultiPart : " + filePath;

		return Response.status(200).entity(output).build();

	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadFile(InputStream form) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(form));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + crunchifyBuilder.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(crunchifyBuilder.toString()).build();
	}

	// save uploaded file to a defined location on the server
	private void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(
					serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}

			outpuStream.flush();
			outpuStream.close();

			uploadedInputStream.close();
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