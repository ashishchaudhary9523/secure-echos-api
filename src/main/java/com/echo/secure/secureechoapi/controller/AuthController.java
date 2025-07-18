package com.echo.secure.secureechoapi.controller;


import com.echo.secure.secureechoapi.Security.JWTTokenProvider;
import com.echo.secure.secureechoapi.model.Customer;
import com.echo.secure.secureechoapi.payload.JWTAuthResponse;
import com.echo.secure.secureechoapi.payload.LoginDTO;
import com.echo.secure.secureechoapi.payload.SignupDTO;
import com.echo.secure.secureechoapi.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping("/sign-in")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserNameOrEmail() ,
                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signupDTO) {
        if(customerRepository.existsCustomerByUserName(signupDTO.getUserName())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if(customerRepository.existsCustomerByEmail(signupDTO.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setName(signupDTO.getName());
        customer.setEmail(signupDTO.getEmail());
        customer.setUserName(signupDTO.getUserName());
        customer.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        customerRepository.save(customer);

        return new ResponseEntity<>("successfully sign-up!", HttpStatus.OK);
    }

}
