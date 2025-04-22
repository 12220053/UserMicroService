// package bt.edu.gcit.usermicroservice.security;

// import bt.edu.gcit.usermicroservice.dao.UserDAO;
// import bt.edu.gcit.usermicroservice.entity.User;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// import bt.edu.gcit.usermicroservice.dao.CustomerDAO; // import the new DAO for Customer entities
// import bt.edu.gcit.usermicroservice.entity.Customer; // import the new Customer entity
// import java.util.Collections; // import the Collections class


// @Service
// public class ShopeMeUserDetailsService implements UserDetailsService {

//     private final UserDAO userDAO;

//     @Autowired
//     public ShopeMeUserDetailsService(UserDAO userDAO) {
//         this.userDAO = userDAO;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         System.out.println("Email: " + email); // Print out the email
//         User user = userDAO.findByEmail(email);

//         if (user == null) {
//             throw new UsernameNotFoundException("User not found with email: " + email);
//         }

//         // Map roles to authorities without 'ROLE_' prefix
//         List<GrantedAuthority> authorities = user.getRoles().stream()
//             .map(role -> {
//                 System.out.println("Role: " + role.getName()); // Print out the role name
//                 return new SimpleGrantedAuthority(role.getName());  // role.getName() should be 'Admin', 'User', etc.
//             })
//             .collect(Collectors.toList());

//         System.out.println("Authorities: " + authorities); // Print out the list of authorities
//         System.out.println("User in loadUserByUsername: " + user.getPassword()); // Print out the user password

//         return new org.springframework.security.core.userdetails.User(
//             user.getEmail(),
//             user.getPassword(),
//             authorities
//         );
//     }
// }





package bt.edu.gcit.usermicroservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.dao.CustomerDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.Customer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopeMeUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final CustomerDAO customerDAO;

    @Autowired
    public ShopeMeUserDetailsService(UserDAO userDAO, CustomerDAO customerDAO) {
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Attempt to load a User entity
        User user = userDAO.findByEmail(email);
        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), authorities
            );
        }

        // Attempt to load a Customer entity
        Customer customer = customerDAO.findByEMail(email);
        if (customer != null) {
            return new org.springframework.security.core.userdetails.User(
                    customer.getEmail(), customer.getPassword(), Collections.emptyList()
            );
        }

        // No user found
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
