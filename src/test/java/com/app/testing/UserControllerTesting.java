package com.app.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.controller.UserController;
import com.app.entity.User;
import com.app.exception.InvalidDataException;
import com.app.exception.InvalidUserException;
import com.app.exception.NotFoundException;
import com.app.repository.UserRepository;

@SpringBootTest
class UserControllerTesting {

	/** auto wired UserController for accessing the methods of user controller */
	@Autowired
	private UserController controller;

	/** By using mockbean we avoid updating database while performing test cases */
	@MockBean
	private UserRepository repository;

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * In this method we test is the user is successfully added in database or not
	 * 
	 * @throws InvalidDataException
	 */
	@Test
	void testCaseForAddUSer() throws InvalidDataException {
		User user = new User("Vishwajit", "vishwt@gmail.com", "vishwajit", "user");
		when(repository.save(user)).thenReturn(user);
		assertEquals(new ResponseEntity<User>(user, HttpStatus.CREATED), controller.addUser(user));

		// when we pass incorrect name then it will throw exception
		User u = new User("@#s", "Sapana@gmail.com", "Sapana", "user");
		Throwable exception = assertThrows(InvalidDataException.class, () -> controller.addUser(u));

		// if we pass empty field through object it will throw exception
		User userOne = new User("", "Sapana@gmail.com", "Sapana", "user");
		Throwable ex = assertThrows(InvalidDataException.class, () -> controller.addUser(userOne));

		// if we pass incorrect email format it will throw exception
		User userTwo = new User("sapana", "Sapana", "Sapana", "user");
		Throwable exe = assertThrows(InvalidDataException.class, () -> controller.addUser(userOne));
	}

	/**
	 * In this method we test is the user is successfully added in database or not
	 * 
	 * @throws InvalidDataException
	 */
	@Test
	void testCaseForUpdateUser() throws InvalidDataException {
		User user = new User("shweta", "shweta@gmail.com", "shweta", "user");
		user.setUser_id(4);
		when(repository.save(user)).thenReturn(user);
		assertEquals(new ResponseEntity<User>(user, HttpStatus.OK), controller.updateUser(user));

		// when we pass incorrect name then it will throw exception
		User u = new User("@#s", "Sapana@gmail.com", "Sapana", "user");
		u.setUser_id(12);
		Throwable exception = assertThrows(InvalidDataException.class, () -> controller.updateUser(u));

		// if we pass empty field through object it will throw exception
		User userOne = new User(12, "", "Sapana@gmail.com", "Sapana", "user");
		Throwable ex = assertThrows(InvalidDataException.class, () -> controller.updateUser(userOne));
	}

	/**
	 * In this test case we tried to validate user by passing email id and password
	 * of the user
	 * 
	 * @throws InvalidDataException
	 * @throws InvalidUserException
	 */
	@Test
	void testCaseForValidateUser() throws NotFoundException, InvalidDataException, InvalidUserException {

		User user = new User(2, "Smita", "smita@gmail.com", "smita", "user");

		// if we login through the user which does not exits it throws exception
		Throwable exception = assertThrows(InvalidUserException.class,
				() -> controller.validateUser("Pranali332@gmail.com", "Pranali"));

		// if we pass email id and password of existing user the it returns the user
		User user1 = new User(20, "Pranali Patil", "Pranali@gmail.com", "Pranali", "user");
		when(repository.ValidateUser("Pranali@gmail.com", "Pranali")).thenReturn(user1);
		assertEquals(new ResponseEntity<User>(user1, HttpStatus.OK),
				controller.validateUser("Pranali@gmail.com", "Pranali"));

		// when we pass empty email and password
		Throwable ex = assertThrows(InvalidDataException.class, () -> controller.validateUser("", ""));

	}

	@Test
	void testCaseDeleteUserById() throws NotFoundException, InvalidDataException {

		// when we pass correct userId for this method it return string that user is
		// deleted
		User user = new User(20, "Pranali Patil", "Pranali@gmail.com", "Pranali", "user");
		doNothing().when(repository).deleteById(20);
		assertEquals(new ResponseEntity<String>("User with id 20 is deleted", HttpStatus.OK),
				controller.deleteUser(user.getUser_id()));

	}

	@Test
	void deleteUser() {
		// when we pass incorrect userId for this method it return string that user is
		// deleted
		Throwable exception = assertThrows(EmptyResultDataAccessException.class, () -> controller.deleteUser(12888));
	}

}
