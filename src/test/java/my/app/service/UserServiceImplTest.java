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
/*
		data = new ArrayList<>();
		User aUser = new User ("AaFirstName", "AaLastName");
		aUser.setId(1);
		aUser.seteMail("a@a");
		aUser.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));
		List<String> aNum = new ArrayList<>(Arrays.asList("11", "11", "13", "14"));
		aUser.setPhoneNumbers(aNum);
		data.add(aUser);
		
		User bUser = new User ("Bb'First-Name", "Bb'Last-Name");
		bUser.setId(2);
		bUser.setCreatedOn(LocalDateTime.of(2005, 5, 5, 5, 5));
		List<String> bNum = new ArrayList<>(Arrays.asList("21"));
		bUser.setPhoneNumbers(bNum);
		data.add(bUser);
		*/
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
	
	public void containsId_ReturnsTrue() {
		LOG.info("containsId_ReturnsTrue method was invoked");
		when(userRepository.containsId(1)).thenReturn(true);
		assertEquals(userServiceImpl.containsId(1), true); 
		verify(userRepository).containsId(1);
		verifyNoMoreInteractions(userRepository);
	}
	
}
