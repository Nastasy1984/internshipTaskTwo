package my.app.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import my.app.model.User;
import my.app.repository.UserRepository;

public class UserServiceImplTest {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.service.UserServiceImplTest.class.getName());
	@Mock
    private UserRepository userRepository;
	
	@InjectMocks
    private UserServiceImpl userServiceImpl;
	private List<User> data;
	
	@Before
    public void setUp(){		
		LOG.info("setUp method was invoked");
		MockitoAnnotations.initMocks(this);
		Mockito.clearInvocations(userRepository);
    }
	
	@Test
	public void save_HappyPath() {
		LOG.info("save_HappyPath method was invoked");
		
		User userGotten = new User ("Cc", "Cc");
		List<String> numsGotten = new ArrayList<>(Arrays.asList("11", "11"));
		userGotten.setPhoneNumbers(numsGotten);
		User expectedUser = new User ("Cc", "Cc");
		List<String> expectedNums = new ArrayList<>(Arrays.asList("11"));
		expectedUser.setPhoneNumbers(expectedNums);
		
		when(userRepository.save(expectedUser)).thenReturn(expectedUser);
		
		User actualUser = userServiceImpl.save(userGotten);
		
		assertEquals(actualUser, expectedUser);
		
		verify(userRepository).save(expectedUser);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void save_Fail() {
		LOG.info("save_Fail method was invoked");
		
		User userGotten = new User ("Cc", "Cc");
		List<String> numsGotten = new ArrayList<>(Arrays.asList("11", "11"));
		userGotten.setPhoneNumbers(numsGotten);
		User expectedUser = new User ("Cc", "Cc");
		List<String> expectedNums = new ArrayList<>(Arrays.asList("11"));
		expectedUser.setPhoneNumbers(expectedNums);
		
		when(userRepository.save(expectedUser)).thenReturn(null);
		User actualUser = userServiceImpl.save(userGotten);
		
		assertEquals(actualUser, null);
		
		verify(userRepository).save(expectedUser);
      	verifyNoMoreInteractions(userRepository);
	}
	
	
	@Test
	public void update_HappyPath() {
		LOG.info("update_HappyPath method was invoked");
		
		User userGotten = new User ("Cc", "Cc");
		List<String> numsGotten = new ArrayList<>(Arrays.asList("11", "11"));
		userGotten.setPhoneNumbers(numsGotten);
		userGotten.setId(1);
		User expectedUser = new User ("Cc", "Cc");
		List<String> expectedNums = new ArrayList<>(Arrays.asList("11"));
		expectedUser.setPhoneNumbers(expectedNums);
		expectedUser.setId(1);
		
		when(userRepository.update(expectedUser)).thenReturn(expectedUser);
		
		User actualUser = userServiceImpl.update(userGotten);
		
		assertEquals(actualUser, expectedUser);
		
		verify(userRepository).update(expectedUser);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void update_UserIsNull() {
		LOG.info("update_UserIsNull method was invoked");
		
		User userGotten = null;
		User actualUser = userServiceImpl.update(userGotten);
		
		assertEquals(actualUser, null);
		
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void update_UserIdIsAbsent() {
		LOG.info("update_UserIdIsAbsent method was invoked");
		
		User userGotten = new User ("Cc", "Cc");
		User actualUser = userServiceImpl.update(userGotten);
		
		assertEquals(actualUser, null);
		
      	verifyNoMoreInteractions(userRepository);
	}
	
	
	@Test
	public void update_Fail() {
		LOG.info("update_Fail method was invoked");
		
		User userGotten = new User ("Cc", "Cc");
		List<String> numsGotten = new ArrayList<>(Arrays.asList("11", "11"));
		userGotten.setPhoneNumbers(numsGotten);
		userGotten.setId(1);
		User expectedUser = new User ("Cc", "Cc");
		List<String> expectedNums = new ArrayList<>(Arrays.asList("11"));
		expectedUser.setPhoneNumbers(expectedNums);
		expectedUser.setId(1);
		
		when(userRepository.update(expectedUser)).thenReturn(null);
		
		User actualUser = userServiceImpl.update(userGotten);
		
		assertEquals(actualUser, null);
		
		verify(userRepository).update(expectedUser);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void deleteById_HappyPath() {
		LOG.info("deleteById_HappyPath method was invoked");
		
		User user = new User ("Cc", "Cc");
		doNothing().when(userRepository).delete(any(User.class));
		
		when(userRepository.getById(1)).thenReturn(user);
		
		userServiceImpl.deleteById(1);
		
		verify(userRepository).delete(user);
		verify(userRepository).getById(1);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void deleteById_IdIsNull() {
		LOG.info("deleteById_IdIsNull method was invoked");
		
		doNothing().when(userRepository).delete(any(User.class));
		
		userServiceImpl.deleteById(null);
		
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void containsId_ReturnsTrue() {
		LOG.info("containsId_ReturnsTrue method was invoked");
		
		when(userRepository.containsId(1)).thenReturn(true);
		
		assertEquals(userServiceImpl.containsId(1), true); 
		
		verify(userRepository).containsId(1);
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void containsId_IdIsNull() {
		LOG.info("containsId_IdIsNul method was invoked");

		assertEquals(userServiceImpl.containsId(null), false); 
		
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void containsId_ReturnsFalse() {
		LOG.info("containsId_ReturnsFalse method was invoked");
		
		when(userRepository.containsId(5)).thenReturn(false);

		assertEquals(userServiceImpl.containsId(5), false); 
		
		verify(userRepository).containsId(5);
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getAllAsList_HappyPath() {
		LOG.info("getAllAsList_HappyPath method was invoked");
		List <User> users = new ArrayList<>();
		users.add(new User("a", "a"));
		users.add(new User("b", "b"));
		
		when(userRepository.getAll()).thenReturn(users);
		
		assertEquals(userServiceImpl.getAllAsList(), users);
		
		verify(userRepository).getAll();
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getAllAsList_ReturnsNull() {
		LOG.info("getAllAsList_ReturnsNull method was invoked");
		
		when(userRepository.getAll()).thenReturn(null);
		
		assertEquals(userServiceImpl.getAllAsList(), null);
		
		verify(userRepository).getAll();
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getById_HappyPath() {
		LOG.info("getById_HappyPath method was invoked");
		
		User user = new User ("Cc", "Cc");
		
		when(userRepository.getById(1)).thenReturn(user);
		
		assertEquals(userServiceImpl.getById(1), user);
		
		verify(userRepository).getById(1);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getById_ReturnsNull() {
		LOG.info("getById_ReturnsNull method was invoked");
		
		when(userRepository.getById(any(Integer.class))).thenReturn(null);
		
		assertEquals(userServiceImpl.getById(1), null);
		
		verify(userRepository).getById(1);
      	verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void getByLastName_HappyPath() {
		LOG.info("getByLastName_HappyPath method was invoked");
		
		User user = new User ("Cc", "Cc");

		when(userRepository.getByLastName("Cc")).thenReturn(new ArrayList<>(Arrays.asList(user)));
		
		assertEquals(userServiceImpl.getByLastName("Cc"), new ArrayList<>(Arrays.asList(user)));
		
		verify(userRepository).getByLastName("Cc");
      	verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void getByLastName_ReturnsNull() {
		LOG.info("getByLastName_ReturnsNull method was invoked");
		
		when(userRepository.getByLastName("Cc")).thenReturn(null);
		
		assertEquals(userServiceImpl.getByLastName("Cc"), null);
		
		verify(userRepository).getByLastName("Cc");
      	verifyNoMoreInteractions(userRepository);
	}
}
