// package bt.edu.gcit.usermicroservice.security;

// import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2UserService;
// import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.ProviderManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import java.util.Arrays;

// @Configuration
// @EnableWebSecurity
// public class ShopmeSecurityConfig {

//     @Autowired
//     private JwtRequestFilter jwtRequestFilter;

//     @Autowired
//     private UserDetailsService userDetailsService;

//     @Autowired
//     private CustomerOAuth2UserService oAuth2UserService;

//     @Autowired
//     private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

//     public ShopmeSecurityConfig() {
//         System.out.println("ShopmeSecurityConfig created");
//     }

//     /**
//      * Bean for custom AuthenticationManager.
//      * @return AuthenticationManager instance.
//      */
//     @Bean
//     public AuthenticationManager customAuthenticationManager() {
//         return new ProviderManager(Arrays.asList(authProvider()));
//     }

//     /**
//      * Bean for PasswordEncoder.
//      * @return PasswordEncoder instance (BCrypt).
//      */
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     /**
//      * Bean for AuthenticationProvider that uses DaoAuthenticationProvider.
//      * @return AuthenticationProvider instance.
//      */
//     @Bean
//     public AuthenticationProvider authProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     /**
//      * Bean for SecurityFilterChain to configure HTTP security.
//      * @param http HttpSecurity to configure.
//      * @return Configured SecurityFilterChain.
//      * @throws Exception if any security configuration error occurs.
//      */
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(configurer -> configurer
//                 // Auth routes
//                 .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()

//                 // User management routes (Admin access only)
//                 .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()

//                 // Countries & States routes (Public)
//                 .requestMatchers("/api/countries/**").permitAll()
//                 .requestMatchers("/api/states/**").permitAll()

//                 // Customer Registration and Verification routes (Public)
//                 .requestMatchers(HttpMethod.POST, "/api/customer/register").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/customer/isEmailUnique").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/customer/code/{code}").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/customer/enable/{id}").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/customer/sendVerificationEmail").permitAll()

//                 // Any other request requires authentication
//                 .anyRequest().authenticated()
//             )
//             .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

//             // OAuth2 Login configuration
//             .oauth2Login()
//                 .userInfoEndpoint()
//                     .userService(oAuth2UserService)
//                 .and()
//                 .successHandler(oAuth2LoginSuccessHandler)

//             .csrf().disable();

//         return http.build();
//     }
// }





package bt.edu.gcit.usermicroservice.security;

import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2UserService;
import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class ShopmeSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public ShopmeSecurityConfig() {
        System.out.println("ShopmeSecurityConfig created");
    }

    /**
     * Bean for custom AuthenticationManager.
     * @return AuthenticationManager instance.
     */
    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    /**
     * Bean for PasswordEncoder.
     * @return PasswordEncoder instance (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for AuthenticationProvider that uses DaoAuthenticationProvider.
     * @return AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean for SecurityFilterChain to configure HTTP security.
     * @param http HttpSecurity to configure.
     * @return Configured SecurityFilterChain.
     * @throws Exception if any security configuration error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(configurer -> configurer
                // Auth routes
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()

                // User management routes (Admin access only)
                .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()

                // Countries & States routes (Public)
                .requestMatchers("/api/countries/**").permitAll()
                .requestMatchers("/api/states/**").permitAll()

                // Customer Registration and Verification routes (Public)
                .requestMatchers(HttpMethod.POST, "/api/customer/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/customer/isEmailUnique").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/customer/code/{code}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/customer/enable/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/customer/sendVerificationEmail").permitAll()

                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

            // OAuth2 Login configuration
            .oauth2Login()
                .userInfoEndpoint()
                    .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)

            .and() // <-- Add this to return to HttpSecurity
            .csrf().disable(); // <-- Now this should work properly.

        return http.build();
    }
}
