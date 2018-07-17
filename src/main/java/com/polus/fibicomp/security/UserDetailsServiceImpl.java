package com.polus.fibicomp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.dao.LoginDao;
import com.polus.fibicomp.pojo.PrincipalBo;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private LoginDao loginDao;

	public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		PrincipalBo principal = applicationUserRepository.findByPrincipalName(username);
		if (principal == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(principal.getPrincipalName(), principal.getPassword(), emptyList());
		// return new User(principal.getPrincipalName(), principal.getPassword(), getAuthorities(principal.getPrincipalName()));
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String personId) {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		// loginDao.isUnitAdmin(personId);
		list.add(new SimpleGrantedAuthority("ADMIN"));
		list.add(new SimpleGrantedAuthority("UNIT_ADMIN"));

		return list;
	}

}
