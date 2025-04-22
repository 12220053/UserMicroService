package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import bt.edu.gcit.usermicroservice.entity.Customer;
import bt.edu.gcit.usermicroservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {

    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Registration
    @PostMapping("/register")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    // Verification email - implementation required
    @PostMapping("/sendVerificationEmail")
    public void sendVerificationEmail(@RequestBody String email) {
        // To be implemented based on your email service
    }

    // Verify using verification code
    @GetMapping("/code/{code}")
    public Customer findByVerificationCode(@PathVariable String code) {
        return customerService.findByVerificationCode(code);
    }

    // Enable customer account
    @PostMapping("/enable/{id}")
    public void enable(@PathVariable long id) {
        customerService.enable(id);
    }

    // Check if email is unique
    @PostMapping("/isEmailUnique")
    public boolean isEmailUnique(@RequestBody String email) {
        return customerService.isEmailUnique(email);
    }

    // Get single customer by ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        return customerService.getCustomerById(id);
    }

    // Get all customers
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Update customer
    @PutMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    // Delete customer
    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
    }

    // Update Authentication Type
    @PutMapping("/updateAuthenticationType/{customerId}")
    public void updateAuthenticationType(@PathVariable Long customerId, @RequestBody String type) {
        AuthenticationType authenticationType;
        try {
            authenticationType = AuthenticationType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authentication type");
        }

        customerService.updateAuthenticationType(customerId, authenticationType);
    }

    // Uncomment and implement if verification logic is added later
    /*
    @PostMapping("/verifyAccount")
    public boolean verifyAccount(@RequestBody String code) {
        return customerService.verify(code);
    }

    @PostMapping("/verify")
    public boolean verify(@RequestBody String code) {
        return customerService.verify(code);
    }
    */
}
