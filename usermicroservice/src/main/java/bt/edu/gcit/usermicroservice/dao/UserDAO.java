package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.Role;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserDAO {

    User save(User user);

    User findById(int theid);

    User findByEmail(String email);
    
    void deleteById(int theid);

    void updateUserEnabledStatus(int id, boolean enabled);
}
