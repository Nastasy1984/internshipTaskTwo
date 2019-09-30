package my.app.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

public class UserControllerTest {

	private ObjectMapper mapper;
	private CloseableHttpClient client;
	
	@Before
    public void setUp(){
		client = HttpClients.createDefault();
		mapper = new ObjectMapper();
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
	public void GetUsersListTest() throws IOException{
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		CloseableHttpResponse response = client.execute(httpGet);
		String string = EntityUtils.toString(response.getEntity());
		StringReader reader = new StringReader(string);
		User[] usersArr = mapper.readValue(reader, User[].class);
		int respCode = response.getStatusLine().getStatusCode();
		assertEquals(new Integer(1), usersArr[0].getId());
		assertEquals(200, respCode);
	}
	
	
	@Test
	public void GetUserByIdTest() throws IOException{
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/user/1");
		CloseableHttpResponse response = client.execute(httpGet);
		String string = EntityUtils.toString(response.getEntity());
		StringReader reader = new StringReader(string);
		User[] usersArr = mapper.readValue(reader, User[].class);
		int respCode = response.getStatusLine().getStatusCode();
		assertEquals(new Integer(1), usersArr[0].getId());
		assertEquals(200, respCode);
	}
	
	@Test
	public void updateUserTest() throws IOException{
		String url = "http://localhost:8080/SpringRest/user/1";
		
		User user = new User ("Tom", "Tomson");
		user.seteMail("tomson@tom.com");
		user.setId(1);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, user);
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(writer.toString());
		httpPut.setEntity(stringEntity);
		CloseableHttpResponse response = client.execute(httpPut);
		User userUpdated = mapper.readValue(response.getEntity().getContent(), User.class);
		int respCode = response.getStatusLine().getStatusCode();
		user.setId(1);
		assertEquals(user, userUpdated);
		assertEquals(200, respCode);
		
	}
	
	
	
}
