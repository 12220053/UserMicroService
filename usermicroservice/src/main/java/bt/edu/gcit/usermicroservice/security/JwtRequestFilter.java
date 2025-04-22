// package bt.edu.gcit.usermicroservice.security;

// import bt.edu.gcit.usermicroservice.service.JWTUtil;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;
// import java.util.List;

// @Component
// public class JwtRequestFilter extends OncePerRequestFilter {

//     @Autowired
//     private JWTUtil jwtUtil;

//     @Autowired
//     private UserDetailsService userDetailsService;

//     // List of endpoints to exclude from JWT filtering
//     private static final List<String> EXCLUDED_URLS = List.of(
//             "/api/auth/login",
//             "/api/users"
//     );

//     // Spring calls this to decide whether to run doFilterInternal
//     @Override
//     protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//         String path = request.getServletPath();
//         return EXCLUDED_URLS.contains(path);
//     }

//     @Override
//     protected void doFilterInternal(
//             HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain chain
//     ) throws ServletException, IOException {

//         final String authorizationHeader = request.getHeader("Authorization");
//         String username = null;
//         String jwt = null;

//         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//             jwt = authorizationHeader.substring(7);
//             username = jwtUtil.extractUsername(jwt);
//         }

//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//             if (jwtUtil.validateToken(jwt, userDetails)) {
//                 UsernamePasswordAuthenticationToken authenticationToken =
//                         new UsernamePasswordAuthenticationToken(
//                                 userDetails, null, userDetails.getAuthorities());

//                 authenticationToken.setDetails(
//                         new WebAuthenticationDetailsSource().buildDetails(request));

//                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//             }
//         }

//         chain.doFilter(request, response);
//     }
// }



// // package bt.edu.gcit.usermicroservice.security;

// // import bt.edu.gcit.usermicroservice.service.JWTUtil;
// // import jakarta.servlet.FilterChain;
// // import jakarta.servlet.ServletException;
// // import jakarta.servlet.http.HttpServletRequest;
// // import jakarta.servlet.http.HttpServletResponse;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// // import org.springframework.security.core.context.SecurityContextHolder;
// // import org.springframework.security.core.userdetails.UserDetails;
// // import org.springframework.security.core.userdetails.UserDetailsService;
// // import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// // import org.springframework.stereotype.Component;
// // import org.springframework.web.filter.OncePerRequestFilter;

// // import java.io.IOException;

// // @Component
// // public class JwtRequestFilter extends OncePerRequestFilter {

// //     @Autowired
// //     private JWTUtil jwtUtil;

// //     @Autowired
// //     private UserDetailsService userDetailsService;

// //     @Override
// //     protected void doFilterInternal(
// //             HttpServletRequest request,
// //             HttpServletResponse response,
// //             FilterChain chain
// //     ) throws ServletException, IOException {

// //         String path = request.getServletPath();
// //         String method = request.getMethod();

// //         // Skip JWT validation for login and registration
// //         if ((path.equals("/api/auth/login") && method.equals("POST")) ||
// //             (path.equals("/api/users") && method.equals("POST"))) {
// //             chain.doFilter(request, response);
// //             return;
// //         }

// //         final String authorizationHeader = request.getHeader("Authorization");
// //         String username = null;
// //         String jwt = null;

// //         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
// //             jwt = authorizationHeader.substring(7);
// //             username = jwtUtil.extractUsername(jwt);
// //         }

// //         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
// //             UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

// //             if (jwtUtil.validateToken(jwt, userDetails)) {
// //                 UsernamePasswordAuthenticationToken authenticationToken =
// //                         new UsernamePasswordAuthenticationToken(
// //                                 userDetails, null, userDetails.getAuthorities());

// //                 authenticationToken.setDetails(
// //                         new WebAuthenticationDetailsSource().buildDetails(request));

// //                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
// //             }
// //         }

// //         chain.doFilter(request, response);
// //     }
// // }




package bt.edu.gcit.usermicroservice.security;

import bt.edu.gcit.usermicroservice.service.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // List of endpoints to exclude from JWT filtering
    private static final List<String> EXCLUDED_URLS = List.of(
            "/api/auth/login",    // Allow login to be unauthenticated
            "/api/users"          // Allow user registration to be unauthenticated
    );

    // Spring calls this to decide whether to run doFilterInternal
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return EXCLUDED_URLS.contains(path);  // Don't apply JWT filter to these endpoints
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Check if Authorization header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Extract the JWT from the header
            username = jwtUtil.extractUsername(jwt); // Extract the username (email) from the token

            // Log the token and username for debugging
            System.out.println("JWT Token found: " + jwt);
            System.out.println("Username extracted from JWT: " + username);
        }

        // If username is extracted and no authentication is currently set in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Log user details
            System.out.println("User details loaded: " + userDetails.getUsername() + " with authorities: " + userDetails.getAuthorities());

            // Validate token against the user details
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // If token is valid, create an authentication token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                // Set the authentication details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                System.out.println("Authentication set for user: " + username);
            } else {
                System.out.println("Invalid token for user: " + username);
            }
        }

        // Proceed with the filter chain
        chain.doFilter(request, response);
    }
}
