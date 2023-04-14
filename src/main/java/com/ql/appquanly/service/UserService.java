package com.ql.appquanly.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ql.appquanly.model.AppUser;
import com.ql.appquanly.model.Role;
import com.ql.appquanly.repository.RoleRepo;
import com.ql.appquanly.repository.UserRepo;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        AppUser user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");

        } else {

            List<Role> listRole = (List<Role>) user.getListRole();

            List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
            if (listRole != null) {
                for (Role role : listRole) {
                    // ROLE_USER, ROLE_ADMIN,..
                    GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                    System.out.println(role.getRoleName());
                    grantList.add(authority);
                }
            }
            UserDetails userDetails = (UserDetails) new User(user.getUsername(), user.getPassword(), grantList);
            return userDetails;
        }
    }

}
