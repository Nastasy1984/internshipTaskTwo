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



	public List<User> getUsersList(){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			//TODO delete! for checking
			System.out.println(response);
			System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());

			String string = EntityUtils.toString(response.getEntity());
			System.out.println(string);
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        System.out.println(users.toString());
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
	
	//TODO delete! for checking
	public static void main(String[] args) {
		PageService aPageService = new PageService();
		aPageService.getUsersList();
	}
	
}
