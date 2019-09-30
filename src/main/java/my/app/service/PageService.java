package my.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.model.User;

//this service sends JSON request from page controller to user controller and send it's response to page controller
@Component
public class PageService {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.service.PageService.class.getName());
	private ObjectMapper mapper;
	private CloseableHttpClient client;
	
	@Autowired
	public PageService(ObjectMapper mapper, CloseableHttpClient client) {
		LOG.info("PageService was created");
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
		LOG.info("cleanUp method was invoked");
    	client.close();
    }
	
	//DONE WORKS
	public List<User> getUsersList(){
		LOG.info("getUsersList method was invoked");
		//CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		try {
			CloseableHttpResponse response = client.execute(httpGet);
	        User[] usersArr = mapper.readValue(response.getEntity().getContent(), User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        LOG.debug("getUsersList method got response with users: {}", users);
	        return users;
			
		} catch (ClientProtocolException e) {
	        LOG.error("getUsersList method caught: {}", e.getClass().getName());
		} catch (IOException e) {
	        LOG.error("getUsersList method caught: {}", e.getClass().getName());
		}
        return null;
	}
	
	//DONE WORKS
	public List<User> getUserById(Integer id) {
		LOG.info("getUserById method was invoked");
		//CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		HttpGet httpGet = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			String string = EntityUtils.toString(response.getEntity());
			int respCode = response.getStatusLine().getStatusCode(); 
			LOG.debug("getUserById method got response code: {}", respCode);
			if (respCode != 200) {
				LOG.warn("getUserById method sending null");
				return null;
			}
			
			StringReader reader = new StringReader(string);
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        LOG.debug("getUserById method got users: {}", users.toString());
	        return users;
			
		} catch (ClientProtocolException e) {
	        LOG.error("getUserById method caught: {}", e.getClass().getName());
		} catch (IOException e) {
	        LOG.error("getUserById method caught: {}", e.getClass().getName());
		}
		return null;
	}
	
	//DONE WORKS
	public List<User> getUserByLastName(String lastName) {
		LOG.info("getUserByLastName method was invoked");
		//CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/userln/";
		HttpGet httpGet = new HttpGet(url);
	    URI uri;
		try {
			uri = new URIBuilder(httpGet.getURI()).addParameter("lastName", lastName).build();
			((HttpRequestBase) httpGet).setURI(uri);
			
		} catch (URISyntaxException e) {
			LOG.error("getUserByLastName method caught: {}", e.getClass().getName());
		}
	    		
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			int respCode = response.getStatusLine().getStatusCode();
			LOG.debug("getUserByLastName method got response code: {}", respCode);
			if (respCode != 200) {
				LOG.warn("getUserByLastName method sending null");
				return null;
			}
			String string = EntityUtils.toString(response.getEntity());
			StringReader reader = new StringReader(string);
	        User[] usersArr = mapper.readValue(reader, User[].class);
	        List<User> users = Arrays.asList(usersArr);
	        LOG.debug("getUserByLastName method got users: {}", users.toString());
	        return users;
			
		} catch (ClientProtocolException e) {
			LOG.error("getUserByLastName method caught: {}", e.getClass().getName());
		} catch (IOException e) {
			LOG.error("getUserByLastName method caught: {}", e.getClass().getName());
		}

		return null;
	}
	
	//DONE WORKS 
	public User addUser(String firstName, String lastName, String eMail, List<String> numbers) {
		LOG.info("addUser method was invoked");
		HttpPost httpPost = new HttpPost("http://localhost:8080/SpringRest/add");
		User userGotten = new User (firstName, lastName);
		userGotten.seteMail(eMail);
		userGotten.setPhoneNumbers(numbers);
		StringWriter writer = new StringWriter();      
		
		try {
			mapper.writeValue(writer, userGotten);
		} catch (IOException e) {
			LOG.error("addUser method caught: {}", e.getClass().getName());
		}

		try {	
			httpPost.setHeader("Accept","application/json");
			httpPost.setHeader("Content-type","application/json");
			StringEntity stringEntity = new StringEntity(writer.toString());
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = client.execute(httpPost);
		    User userAdded = mapper.readValue(response.getEntity().getContent(), User.class);
		    LOG.debug("addUser method got user: {}", userAdded.toString());
		    return userAdded;
		} catch (IOException e) {
			LOG.error("addUser method caught: {}", e.getClass().getName());
		}
		LOG.debug("addUser method returns null");
		return null;
	}
	
	public int deleteUser(Integer id) {
		LOG.info("deleteUser method was invoked");
		//CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		HttpDelete httpDelete = new HttpDelete(url);
		try {
			CloseableHttpResponse response = client.execute(httpDelete);
			int respCode = response.getStatusLine().getStatusCode();
			LOG.debug("deleteUser method got response code: {}", respCode);
			return respCode;
			
		} catch (ClientProtocolException e) {
			LOG.error("deleteUser method caught: {}", e.getClass().getName());
		} catch (IOException e) {
			LOG.error("deleteUser method caught: {}", e.getClass().getName());
		}
		LOG.debug("deleteUser method returns 0");
		return 0;
	}
	
	
	public User updateUser(Integer id, String firstName, String lastName, String eMail, List<String> numbers) {
		LOG.info("updateUser method was invoked");
		//CloseableHttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SpringRest/user/" + id;
		User userGotten = new User (firstName, lastName);
		userGotten.setId(id);
		userGotten.seteMail(eMail);
		userGotten.setPhoneNumbers(numbers);
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, userGotten);
		} catch (IOException e) {
			LOG.error("updateUser method caught: {}", e.getClass().getName());
		}

		HttpPut httpPut = new HttpPut(url);

		try {	
			httpPut.setHeader("Accept","application/json");
			httpPut.setHeader("Content-type","application/json");
			StringEntity stringEntity = new StringEntity(writer.toString());
			httpPut.setEntity(stringEntity);
			CloseableHttpResponse response = client.execute(httpPut);
			LOG.debug("updateUser method got response code: {}", response.getStatusLine().getStatusCode());
		    User userUpdated = mapper.readValue(response.getEntity().getContent(), User.class);
		    LOG.debug("updateUser method got user: {}", userUpdated.toString());
		    return userUpdated;
		} catch (IOException e) {
			LOG.error("updateUser method caught {}", e.getClass().getName());
		}
		
		LOG.debug("updateUser method returns null");
		return null;
	}
	
}
