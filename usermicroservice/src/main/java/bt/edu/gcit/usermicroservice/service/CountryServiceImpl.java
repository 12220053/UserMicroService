package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.CountryDAO;
import bt.edu.gcit.usermicroservice.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryDAO countryDAO;

    @Autowired
    public CountryServiceImpl(CountryDAO theCountryDAO) {
        this.countryDAO = theCountryDAO;
    }

    @Override
    public Country findByCode(String code) {
        return countryDAO.findByCode(code);
    }

    @Override
    @Transactional
    public void save(Country country) {
        countryDAO.save(country);
    }

    @Override
    @Transactional
    public Country findById(int id) { // ✅ Still using int
        return countryDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Country> findAll() {
        return countryDAO.findAll();
    }

    @Override
    @Transactional
    public List<Country> findAllByOrderByNameAsc() {
        return countryDAO.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public void update(Country country) {
        countryDAO.update(country); // Or save if you're using save for update too
    }

    @Override
    @Transactional
    public void delete(Country country) {
        countryDAO.delete(country);
    }
}
