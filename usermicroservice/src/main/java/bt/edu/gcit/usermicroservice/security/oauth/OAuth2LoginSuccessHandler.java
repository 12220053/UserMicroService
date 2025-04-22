package bt.edu.gcit.usermicroservice.security.oauth;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import bt.edu.gcit.usermicroservice.entity.Customer;
import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2User;
import bt.edu.gcit.usermicroservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private CustomerService customerService;

    @Autowired
    @Lazy
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * This method is called when a user has been successfully authenticated.
     * Performs actions upon successful login, such as creating or updating a customer.
     *
     * @param request       The HttpServletRequest.
     * @param response      The HttpServletResponse.
     * @param authentication The authentication object containing the authenticated user details.
     * @throws ServletException If a servlet error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // Get the authenticated user's details from the OAuth2 authentication object
        CustomerOAuth2User oauthUser = (CustomerOAuth2User) authentication.getPrincipal();
        String name = oauthUser.getName();
        String email = oauthUser.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oauthUser.getClientName();

        // Log the user details for debugging
        System.out.println("OAuth2LoginSuccessHandler: " + name + " | " + email);
        System.out.println("Client Name: " + clientName);

        // Determine the authentication type based on the client (Google, Facebook, etc.)
        AuthenticationType authenticationType = getAuthenticationType(clientName);

        // Check if the customer exists and take appropriate actions
        Customer customer = customerService.findByEMail(email);
        if (customer == null) {
            // Add new customer if no existing record found
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
        } else {
            // Update the authentication type for the existing customer
            customerService.updateAuthenticationType(customer.getId(), authenticationType);
        }

        // Call the parent method to continue the success handler flow
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Determine the authentication type based on the OAuth client name.
     *
     * @param clientName The name of the OAuth client (e.g., Google, Facebook).
     * @return The corresponding AuthenticationType.
     */
    private AuthenticationType getAuthenticationType(String clientName) {
        if (clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else {
            return AuthenticationType.DATABASE;
        }
    }
}
