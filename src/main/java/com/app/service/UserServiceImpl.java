package com.app.service;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.app.entity.User;
import com.app.exception.InvalidDataException;
import com.app.exception.InvalidUserException;
import com.app.exception.NotFoundException;
import com.app.repository.UserRepository;
import com.app.validation.ValidateUser;

@Repository
public class UserServiceImpl implements UserService {
	/** auto wired UserRepository for accessing the methods of user repository */
	@Autowired
	UserRepository repository;

	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create new user with auto generated id as a user_id
	 * 
	 * @param user
	 * @return User object
	 */
	@Override
	public User addUser(User user) throws InvalidDataException {

		try {
			repository.save(user);
		} catch (ConstraintViolationException ex) {

			throw new InvalidDataException("Email should be correct");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidDataException("Email is alerady exist");
		}
		return user;
	}

	/**
	 * Update the old user record.
	 * 
	 * @param user
	 * @return user object null (if the user_id not found).
	 */
	@Override
	public User UpdateUser(User user) throws InvalidDataException {
		return this.repository.save(user);

	}

	/**
	 * In this method we are trying to delete user depending upon user_id
	 * 
	 * @param userId
	 * @return User object(if found)
	 * @throws NotFoundException
	 * 
	 */
	@Override
	public String deleteUser(int id) throws InvalidDataException, NotFoundException {
		// TODO Auto-generated method stub
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new NotFoundException("user with this id does not exits");
		}
		return "User with id " + id + " is deleted";
	}

	/**
	 * In this method we are trying get all the details of specific user depending
	 * upon user_id
	 * 
	 * @param userId
	 * @return User object(if found)
	 * @throws NotFoundException
	 * 
	 */
	@Override
	public User getUserById(Integer id) throws InvalidDataException, NotFoundException {
		// TODO Auto-generated method stub
		User u = repository.findById(id).orElseThrow(() -> new NotFoundException("unable to find user with given id"));
		return u;
	}

	/**
	 * In this method we try to validate that user i.e we check the email and
	 * password of the user
	 * 
	 * @param String email
	 * @param String password
	 * @return User object(if it is correct) if email or pass is incorrect
	 *         throws @throws InvalidUserException
	 */
	@Override
	public User validateUser(String userEmail, String password)
			throws InvalidDataException, NotFoundException, InvalidUserException {
		User u = repository.ValidateUser(userEmail, password);
		if (u == null)
			throw new InvalidUserException("Unable to find user with this email");
		return u;
	}

}