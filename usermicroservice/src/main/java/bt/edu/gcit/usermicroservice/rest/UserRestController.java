// package bt.edu.gcit.usermicroservice.rest;

// import bt.edu.gcit.usermicroservice.entity.Role;
// import bt.edu.gcit.usermicroservice.entity.User;
// import bt.edu.gcit.usermicroservice.service.UserService;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.validation.Valid;
// import jakarta.validation.constraints.NotNull;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.util.Map;
// import java.util.Set;

// /**
//  * REST controller for managing users.
//  */
// @RestController
// @RequestMapping("/api")
// public class UserRestController {

//     private final UserService userService;

//     public UserRestController(UserService userService) {
//         this.userService = userService;
//     }

//     /**
//      * Saves a new user with multipart/form-data (e.g., with photo and roles).
//      */
//     @PostMapping(value = "/users", consumes = "multipart/form-data")
//     public User save(
//             // @RequestPart("firstName") @Valid @NotNull String firstName,
//             // @RequestPart("lastName") @Valid @NotNull String lastName,
//             // @RequestPart("email") @Valid @NotNull String email,
//             // @RequestPart("password") @Valid @NotNull String password,
//             // @RequestPart("photo") @Valid @NotNull MultipartFile photo,
//             // @RequestPart("roles") @Valid @NotNull String rolesJson

//             //  the @RequestPrt works on its own so we use @Valid @NotNull with object

//             @RequestPart("firstName") String firstName,
//             @RequestPart("lastName") String lastName,
//             @RequestPart("email") String email,
//             @RequestPart("password") String password,
//             @RequestPart("photo") MultipartFile photo,
//             @RequestPart("roles") String rolesJson


//     ) {
//         try {
//             User user = new User();
//             user.setFirstName(firstName);
//             user.setLastName(lastName);
//             user.setEmail(email);
//             user.setPassword(password);

//             ObjectMapper objectMapper = new ObjectMapper();
//             Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>() {});
//             user.setRoles(roles);

//             System.out.println("Uploading photo...");

//             User savedUser = userService.save(user);
//             userService.uploadUserPhoto(savedUser.getId().intValue(), photo);

//             return savedUser;
//         } catch (IOException e) {
//             throw new RuntimeException("Error while uploading photo", e);
//         }
//     }

//     /**
//      * Checks if the provided email already exists in the system.
//      *
//      * @param email the email to check
//      * @return true if the email is a duplicate, false otherwise
//      */
//     @GetMapping("/users/checkDuplicateEmail")
//     public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
//         boolean isDuplicate = userService.isEmailDuplicate(email);
//         return ResponseEntity.ok(isDuplicate);
//     }

//     /**
//      * Updates a user with the given ID using the provided User object.
//      *
//      * @param id the ID of the user to be updated
//      * @param updatedUser the User object containing the updated information
//      * @return the updated User object
//      */
//     @PutMapping("/users/{id}")
//     public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
//         return userService.updateUser(id, updatedUser);
//     }

//     /**
//      * Deletes a user with the specified ID.
//      *
//      * @param id the ID of the user to delete
//      */
//     @DeleteMapping("/users/{id}")
//     public void deleteUser(@PathVariable int id) {
//         userService.deleteById(id);
//     }

//     /**
//      * Update the enabled status of a user with the specified ID.
//      *
//      * @param id The ID of the user to update
//      * @param requestBody Request body containing the "enabled" field
//      * @return OK if the update was successful
//      */
//     @PutMapping("/users/{id}/enabled")
//     public ResponseEntity<?> updateUserEnabledStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
//         Boolean enabled = requestBody.get("enabled");
//         userService.updateUserEnabledStatus(id, enabled);
//         System.out.println("User enabled status updated successfully");
//         return ResponseEntity.ok().build();
//     }
// }


///==========================================


package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Saves a new user with multipart/form-data (e.g., with photo and roles).
     */
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can create a new user
    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public User save(
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("photo") MultipartFile photo,
            @RequestPart("roles") String rolesJson
    ) {
        try {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            ObjectMapper objectMapper = new ObjectMapper();
            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>() {});
            user.setRoles(roles);

            System.out.println("Uploading photo...");

            User savedUser = userService.save(user);
            userService.uploadUserPhoto(savedUser.getId().intValue(), photo);

            return savedUser;
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading photo", e);
        }
    }

    /**
     * Checks if the provided email already exists in the system.
     *
     * @param email the email to check
     * @return true if the email is a duplicate, false otherwise
     */
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can check email duplication
    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    /**
     * Updates a user with the given ID using the provided User object.
     *
     * @param id the ID of the user to be updated
     * @param updatedUser the User object containing the updated information
     * @return the updated User object
     */
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can update a user
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the ID of the user to delete
     */
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can delete a user
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    /**
     * Update the enabled status of a user with the specified ID.
     *
     * @param id The ID of the user to update
     * @param requestBody Request body containing the "enabled" field
     * @return OK if the update was successful
     */
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can update user status
    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean enabled = requestBody.get("enabled");
        userService.updateUserEnabledStatus(id, enabled);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }
}
