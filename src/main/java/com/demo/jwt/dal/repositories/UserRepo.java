package com.demo.jwt.dal.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.jwt.dal.entities.User;


public interface UserRepo extends JpaRepository<User, Integer>{
	List<User> findByUsername(String username);
	List<User> findByUsernameAndPassword(String username, String password);
}
