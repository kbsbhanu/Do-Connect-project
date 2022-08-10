package com.wipro.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.springboot.dto.SignupRequest;
import com.wipro.springboot.dto.UserDto;
import com.wipro.springboot.entity.User;
import com.wipro.springboot.repository.IUserRepository;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	public UserDto createUser(SignupRequest signupRequest) {
		User user = new User(signupRequest.getEmail(), new BCryptPasswordEncoder().encode(signupRequest.getPassword()),
				signupRequest.getName(), 2);
		user = userRepository.save(user);
		if (user == null)
			return null;

		return user.mapUsertoUserDto();
	}

	public Boolean hasUserWithEmail(String email) {
		return userRepository.findFirstByEmail(email) != null;
	}

	@Override
	public void createAdminAccount() {
		User adminAccount = userRepository.findByRole(1);
		if (null == adminAccount) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setName("Admin");
			user.setRole(1);
			userRepository.save(user);
		}
	}
}
