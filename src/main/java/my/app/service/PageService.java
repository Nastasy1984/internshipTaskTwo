package my.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

//this service sends JSON request from page controller to user controller and send it's response to page controller
@Component
public class PageService {


	//DONE WORKS
	public List<User> getUsersList(){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	//DONE WORKS
	public List<User> getUserById(Integer id) {
		CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		HttpGet httpGet = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//DONE WORKS
	public List<User> getUserByLastName(String lastName) {
		CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + lastName;
		HttpGet httpGet = new HttpGet(url);
	    URI uri;
		try {
			uri = new URIBuilder(httpGet.getURI()).addParameter("lastName", lastName).build();
			((HttpRequestBase) httpGet).setURI(uri);
			
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	    		
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//DONE WORKS 
	//TODO Exeptions
	public User addUser(String firstName, String lastName) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:8080/SpringRest/add");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("firstName", firstName));
	    params.add(new BasicNameValuePair("lastName", lastName));
	    try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			try {
				CloseableHttpResponse response = client.execute(httpPost);
				//TODO delete
				System.out.println(response.toString());
				String string = EntityUtils.toString(response.getEntity());
				
				//TODO delete we got wrong type "text/html"
				String contentMimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
				System.out.println("PESP TYPE");
				System.out.println(contentMimeType);
				
				//TODO delete
				System.out.println("WE GOT");
				System.out.println(string);
				
				StringReader reader = new StringReader(string);
		        ObjectMapper mapper = new ObjectMapper();
		        User userAdded = mapper.readValue(reader, User.class);
		        
				//TODO delete
				System.out.println(userAdded.toString());
				
		        return userAdded;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

	    
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//TODO delete
	public static void main(String[] args) {
		PageService aPageService = new PageService();
		//aPageService.getUserByLastName("Averina");
		aPageService.addUser("Kot", "Kotov");
	}
	
	
}
