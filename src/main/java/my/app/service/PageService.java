package my.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

//this service sends JSON request from page controller to user controller and send it's response to page controller
@Component
public class PageService {
	//TODO сделать внедрение в виде бина?
	CloseableHttpClient client = HttpClients.createDefault();

	public List<User> getUsersList(){
		//CloseableHttpClient client = HttpClients.createDefault();
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
	
	public User getUserById(Integer id) {
		String URI = "http://localhost:8080/SpringRest/user/" + id;
		HttpGet httpGet = new HttpGet(URI);
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User user = mapper.readValue(reader, User.class);
	        return user;
			
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
	
	
	
	//TODO delete! for checking
	public static void main(String[] args) {
		PageService aPageService = new PageService();
		aPageService.getUsersList();
	}
	
}
