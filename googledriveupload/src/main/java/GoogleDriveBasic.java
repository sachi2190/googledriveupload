/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ssolanki
 */
//package com.jrp.googleapi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.FileList;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

/**
 * A basic class for download and upload text files with google drive API.
 * 
 */
public class GoogleDriveBasic {
  private String redirectURI;
	HttpTransport httpTransport;
	JsonFactory jsonFactory;
	GoogleAuthorizationCodeFlow flow;
	Drive service;

	
	/**
	 * Initialize initials attributes.
	 * 
	 * @param String basic configuration parameters.
	 */
	public GoogleDriveBasic(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URI){
		this.redirectURI=REDIRECT_URI;
		httpTransport = new NetHttpTransport();
		jsonFactory= new JacksonFactory();
		
		flow= new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, 
				CLIENT_ID, CLIENT_SECRET, 
				Arrays.asList(DriveScopes.DRIVE))
				.setAccessType("online")
				.setApprovalPrompt("auto").build();
	}
	
	/**
	 * Get the authorization URL for authorize the application.
	 *
	 * @return String URL for authorize the application.
	 */
	public String getURL(){
		String url = flow.newAuthorizationUrl().setRedirectUri(redirectURI).build();
		return url;
	}
	
	
	/**
	 * Set the authorization code and create the service.
	 *
	 * @param String authorization code.
	 */
	public void setCode(String code) throws IOException{
		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		//Create a new authorized API client
		service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
	}
	
	/**
	 * Upload a text file.
	 *
	 * @param String path of the file.
	 * @return String name of the file in google drive.
	 */
	public String uploadTextFile(String filePath, String title) throws IOException{
		File body = new File();
		body.setTitle(title);
		body.setDescription("A test document");
		body.setMimeType("text/plain");
		java.io.File fileContent = new java.io.File(filePath);
		FileContent mediaContent = new FileContent("text/plain", fileContent);
		File file = service.files().insert(body, mediaContent).execute();
		return file.getId();
	}
        
        /**
	 * Upload an image file.
	 *
	 * @param String path of the file.
	 * @return String name of the file in google drive.
	 */
	public String uploadImageFile(String filePath, String title) throws IOException{
		File body = new File();
		body.setTitle(title);
		body.setDescription("A test Image");
		body.setMimeType("image/jpeg");
		java.io.File fileContent = new java.io.File(filePath);
		FileContent mediaContent = new FileContent("image/jpeg", fileContent);
		File file = service.files().insert(body, mediaContent).execute();
		return file.getId();
	}
	
	/**
	 * Get the content of a file.
	 *
	 * @param File to get the content.
	 * @return String content of the file.
	 */
	public String downloadTextFile(File file) throws IOException{
		GenericUrl url = new GenericUrl(file.getDownloadUrl());
		HttpResponse response = service.getRequestFactory().buildGetRequest(url).execute();
		try {
			return new Scanner(response.getContent()).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
	
	/**
	 * Get the content of a file.
	 *
	 * @param String the file ID.
	 * @return String content of the file.
	 */
	public String downloadTextFile(String fileID) throws IOException{
		File file=service.files().get(fileID).execute();
		return downloadTextFile(file);
	}
	
	/**
	 * Retrieve a list of File resources.
	 *
	 * @param service Drive API service instance.
	 * @return List of File resources.
	 * @author Google
	 * @throws IOException 
	 */
	public List<File> retrieveAllFiles() throws IOException {
		List<File> result = new ArrayList<File>();
		Files.List request = null;

		request = service.files().list();


		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null &&
				request.getPageToken().length() > 0);

		return result;
	}

}
