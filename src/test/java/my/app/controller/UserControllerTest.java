package my.app.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

public class UserControllerTest {

	CloseableHttpClient client;
	
	@Before
    public void setUp(){
		client = HttpClients.createDefault();
    }
	
	@After
    public void tearDown(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	
	@Test
	public void GetUsersListTest() {
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        User expectedUser = new User("Anya","Averina");
	        expectedUser.setId(1);
	        int respCode = response.getStatusLine().getStatusCode();
	        assertEquals(expectedUser, usersArr[0]);
	        assertEquals(200, respCode);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void GetUserByIdTest() {
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/user/1");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        ObjectMapper mapper = new ObjectMapper();
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        User expectedUser = new User("Anya","Averina");
	        expectedUser.setId(1);
	        int respCode = response.getStatusLine().getStatusCode();
	        assertEquals(expectedUser, usersArr[0]);
	        assertEquals(200, respCode);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
