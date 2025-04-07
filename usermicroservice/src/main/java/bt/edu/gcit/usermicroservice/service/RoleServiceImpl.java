package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.RoleDAO;
import bt.edu.gcit.usermicroservice.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // Ensure transaction management is enabled at the class level
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    // Constructor injection
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public void addRole(Role role) {
        roleDAO.addRole(role);  // Ensure RoleDAO has this method implemented
    }
}
