package my.app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import my.app.model.User;

@Component
public class UserRepositoryImpl implements UserRepository{

	private Map<Integer,User> users = new HashMap<>();
	
	public UserRepositoryImpl() {
		//creating 3 users for probe
		User user1 = new User("Anya","Averina");
		user1.setId(1);
		User user2 = new User("Boris","Borisov");
		user2.setId(2);
		User user3 = new User("Viktor","Viktorov");
		user3.setId(3);
		User user4 = new User("Gvidon","Gvidonov");
		user4.setId(4);
		this.users.put(user1.getId(), user1);
		this.users.put(user2.getId(), user2);
		this.users.put(user3.getId(), user3);
		this.users.put(user4.getId(), user4);
	}

	@Override
	public void save(User user) {
		if (user.getId() == 0) {
		//imitation of generating new id (maximum id + 1)
			Set<Integer> setKeySet = users.keySet();
			int max = 0;
			//looking for maximum id in users
			for (Integer key: setKeySet) {
				if (key > max) {
					max = key;
				}
			}
			int newId = max + 1;
			user.setId(newId);
		}
		users.put(user.getId(),user);
	}

	@Override
	public void delete(User user) {
		users.remove(user.getId());
	}

	@Override
	public Map<Integer,User> getAll() {
		return users;
	}

	@Override
	public User getById(Integer id) {
		return users.get(id);
	}
	
	@Override
	public List<User> getByLastName(String lastName) {
		if (lastName != null) {
			List<User> listValuesList = new ArrayList<User>(users.values());
			List<User> resultList = new ArrayList<User>();
			for (User user: listValuesList) {
				if (user.getLastName().equals(lastName)){
					resultList.add(user);
				}
			}
			return resultList;
		}
		return null;
	};
	
	/*simple checking
	public static void main(String[] args) {
		UserRepositoryImpl aImpl = new UserRepositoryImpl();
		List<User> myList = aImpl.getByLastName("Averina");
		for (User user : myList) {
			System.out.println(user);
		}
	}
	*/
}