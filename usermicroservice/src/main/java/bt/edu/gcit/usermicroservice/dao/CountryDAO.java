package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Country;
import java.util.List;

public interface CountryDAO {
    Country findByCode(String code);

    // Create
    void save(Country country);

    // Read
    Country findById(int id); // 🔧 Changed from Long to int
    List<Country> findAll();
    List<Country> findAllByOrderByNameAsc();

    // Update
    void update(Country country);

    // Delete
    void delete(Country country);
}
