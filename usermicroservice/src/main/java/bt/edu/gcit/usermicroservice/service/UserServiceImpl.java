package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User save(User user) {
        // Encrypt the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.save(user);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Override
    public User findByID(int theId) {
        return userDAO.findById(theId);
    }

    @Transactional
    @Override
    public User updateUser(int theid, User updatedUser) {
        // Find the existing user by ID
        User existingUser = userDAO.findById(theid);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + theid);
        }

        // Update user details
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());

        // If the password has changed, encrypt it and update
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Update roles
        existingUser.setRoles(updatedUser.getRoles());

        // Save the updated user
        return userDAO.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteById(int theid) {
        // Delete the user by ID
        userDAO.deleteById(theid);
    }

    @Transactional
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        // Update the 'enabled' status of the user
        userDAO.updateUserEnabledStatus(id, enabled);
    }

    @Transactional
    @Override
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        // Find the user by ID
        User user = findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }

        // Check if the photo size exceeds the limit
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }

        // Clean the filename to prevent any malicious characters
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());

        // Extract file extension and generate a new filename with timestamp to ensure uniqueness
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;

        // Define the upload path and transfer the photo
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);

        // Update the user's photo
        user.setPhoto(filename);

        // Save the user with the updated photo
        save(user);
    }
}
