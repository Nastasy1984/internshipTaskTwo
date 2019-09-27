package my.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PreDestroy;

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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

//this service sends JSON request from page controller to user controller and send it's response to page controller
@Component
public class PageService {

	private ObjectMapper mapper;
	private CloseableHttpClient client;
	
	@Autowired
	public PageService(ObjectMapper mapper, CloseableHttpClient client) {
		this.mapper = mapper;
		this.client = client;
	}
	
	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	
	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}
	
    @PreDestroy
    public void cleanUp() throws Exception {
    	client.close();
    }
	
	//DONE WORKS
	public List<User> getUsersList(){
		//CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
	        User[] usersArr = mapper.readValue(response.getEntity().getContent(), User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	//DONE WORKS
	public List<User> getUserById(Integer id) {
		//CloseableHttpClient client = HttpClients.createDefault();
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
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//DONE WORKS
	public List<User> getUserByLastName(String lastName) {
		//CloseableHttpClient client = HttpClients.createDefault();
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
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        return users;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	//DONE WORKS 
	public User addUser(String firstName, String lastName, String eMail, List<String> numbers) {
		//CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:8080/SpringRest/add");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("firstName", firstName));
	    params.add(new BasicNameValuePair("lastName", lastName));
	    params.add(new BasicNameValuePair("eMail", eMail));
	    String[] array = numbers.toArray(new String[numbers.size()]);
	   // params.add(new BasicNameValuePair("numbers", array));
	    try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			try {
				CloseableHttpResponse response = client.execute(httpPost);
				User userAdded = mapper.readValue(response.getEntity().getContent(), User.class);
		        return userAdded;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
	    
		return null;
	}
	
	public int deleteUser(Integer id) {
		//CloseableHttpClient client = HttpClients.createDefault();
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
		
		return 0;
	}
	
	
	public User updateUser(Integer id, String firstName, String lastName, String eMail, List<String> numbers) {
		
		//CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		
		User userGotten = new User (firstName, lastName);
		userGotten.setId(id);
		userGotten.seteMail(eMail);
		userGotten.setPhoneNumbers(numbers);
		
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, userGotten);
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
		    User userUpdated = mapper.readValue(response.getEntity().getContent(), User.class);
			
		    return userUpdated;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		System.out.println("HERE SENDING NULL");
		return null;
	}
	
}
