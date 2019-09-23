package my.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
			int respCode = response.getStatusLine().getStatusCode();
			if (respCode != 200) {
				return null;
			}
			
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
		String url = "http://localhost:8080/SpringRest/userln/";
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
			int respCode = response.getStatusLine().getStatusCode();
			if (respCode != 200) {
				return null;
			}
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
				String string = EntityUtils.toString(response.getEntity());
				StringReader reader = new StringReader(string);
		        ObjectMapper mapper = new ObjectMapper();
		        User userAdded = mapper.readValue(reader, User.class);
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
	
	public int deleteUser(Integer id) {
		CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		HttpDelete httpDelete = new HttpDelete(url);
		try {
			CloseableHttpResponse response = client.execute(httpDelete);
			int respCode = response.getStatusLine().getStatusCode();
			return respCode;
			
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
		return 0;
	}
	
	
	public User updateUser(Integer id, String firstName, String lastName) {
		CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		
		User userGotten = new User (firstName, lastName);
		userGotten.setId(id);
		StringWriter writer = new StringWriter();
		ObjectMapper mapperToJson = new ObjectMapper();
		try {
			mapperToJson.writeValue(writer, userGotten);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		HttpPut httpPut = new HttpPut(url);

		try {	
			httpPut.setHeader("Accept","application/json");
			httpPut.setHeader("Content-type","application/json");
			StringEntity stringEntity = new StringEntity(writer.toString());
			httpPut.setEntity(stringEntity);
			CloseableHttpResponse response = client.execute(httpPut);
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
		    ObjectMapper mapperFromJson = new ObjectMapper();
		    User userUpdated = mapperFromJson.readValue(reader, User.class);
		    return userUpdated;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
}
