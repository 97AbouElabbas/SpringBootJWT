package com.allianz.jwt.security.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.allianz.jwt.dal.repositories.UserRepo;

@Service
public class AppUserDetailsService implements UserDetailsService{
	@Autowired
	UserRepo userrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.allianz.jwt.dal.entities.User user = userrepo.findByUsername(username).get(0);
		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

}
