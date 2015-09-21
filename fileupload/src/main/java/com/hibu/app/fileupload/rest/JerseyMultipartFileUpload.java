package com.hibu.app.fileupload.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/multifiles")
// http://localhost:8098/fileupload/multiform.html
// http://localhost:8098/fileupload/rest/application.wadl
// http://10.204.70.60:8098/fileupload/multiform.html

/*Send To Server

{

“primaryKey”:”1234”,
“salesRepName”:”yyyyyyyyy”,
“businessName”:”zzzzzzzzz”,
“phoneNo”:”898989889898",
"emailId”:”vinay@gmail.com",
“imageName":"vinay.png”,
imageData”:”xxxxxxxxx”
}
Response From Server
{
“primaryKey”:”1234”,
“status”:”Success”
}

*/
public class JerseyMultipartFileUpload {

	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://Users/IB1272/Desktop/Upload_Files/";
	private static final String FILEUPLOAD_SUCCESS = "Image Uploaded Successfully";
	private static final String FILEUPLOAD_FAILURE = "Image Uploaded Failed";

	/**
	 * Upload a File
	 */

	@POST
	@Path("/upload_old")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFileOld(FormDataMultiPart form) {
		System.out.println(" uploadFile " + form);
		FormDataBodyPart filePart = form.getField("file");

		ContentDisposition headerOfFilePart = filePart.getContentDisposition();

		InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		String filePath = SERVER_UPLOAD_LOCATION_FOLDER
				+ headerOfFilePart.getFileName();

		// save the file to the server
		saveFile(fileInputStream, filePath);

		String output = "File saved to server location using FormDataMultiPart : "
				+ filePath;

		return Response.status(200).entity(FILEUPLOAD_SUCCESS).build();

	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadFile(InputStream form) {
		StringBuilder inputBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(form));
			String line = null;
			while ((line = in.readLine()) != null) {
				inputBuilder.append(line);
			}
			// return HTTP response 200 in case of success
			JSONObject jsonObject = convertAsJSON(inputBuilder);

			byte[] imageBytes = Base64.decode((String) jsonObject.get("imageData"));
			saveFile(imageBytes, (String)jsonObject.get("fileName"));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400).entity(FILEUPLOAD_FAILURE).build();
		}

		return Response.status(200).entity(FILEUPLOAD_SUCCESS).build();
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

	public JSONObject convertAsJSON(StringBuilder input) throws JSONException {
		final JSONObject obj = new JSONObject(input.toString());
		return obj;
	}

	// save uploaded file to a defined location on the server
	static void saveFile(byte[] imageBytes, String fileName) throws Exception {

		try {
			// convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream("C:\\Users\\IB1272\\Desktop\\Upload_Files\\"+ fileName);
			fileOuputStream.write(imageBytes);
			fileOuputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Failed to Save File/Image");
		}

	}

}