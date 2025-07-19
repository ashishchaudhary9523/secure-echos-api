package com.echo.secure.secureechoapi.service.implementation;

import com.echo.secure.secureechoapi.AESUtil.AESUtil;
import com.echo.secure.secureechoapi.Exceptions.SecureEchoAPIException;
import com.echo.secure.secureechoapi.model.Customer;
import com.echo.secure.secureechoapi.model.Vault;
import com.echo.secure.secureechoapi.payload.VaultDTO;
import com.echo.secure.secureechoapi.repository.CustomerRepository;
import com.echo.secure.secureechoapi.repository.VaultRepository;
import com.echo.secure.secureechoapi.service.VaultService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VaultServiceImplementation implements VaultService {

    private final VaultRepository vaultRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @Autowired
    public VaultServiceImplementation(VaultRepository vaultRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.vaultRepository = vaultRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public String storeVault(VaultDTO vaultDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!vaultDTO.getUserName().equals(currentUserName)){
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }

        if(!vaultRepository.existsById(vaultDTO.getUserName())){
            return "User not found. Maybe you need to sign-up first?";
        }
        Vault vault = vaultRepository.findByUserName(vaultDTO.getUserName());
        if(vaultRepository.existsById(vaultDTO.getUserName()) && vault.getIsUpdated()){
            return vaultDTO.getUserName() + "'s vault already exists. Cannot create a new one. Please contact your administrator for further assistance.";
        }
            String encrypted = AESUtil.encrypt(vaultDTO.getEncryptedData(), vaultDTO.getKey());
            String keyHash = passwordEncoder.encode(vaultDTO.getKey());

            vault.setEncryptedData(encrypted);
            vault.setKey(keyHash);
            vault.setFailedAttempts(0);
            vault.setIsUpdated(true);

            vaultRepository.save(vault);
            return "Data saved";
    }

    @Transactional
    @Override
    public String getVault(String userName, String key) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userName.equals(currentUserName)){
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        Customer customer = customerRepository.findById(userName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND , "User not found"));
        Vault vault = customer.getVault();
        if(passwordEncoder.matches(key , vault.getKey())){
            System.out.println("Key matched");
            String data = null;
            try {
                data = AESUtil.decrypt(vault.getEncryptedData() , key);
            } catch (Exception e) {
                return "Failed to decrypt data" + e.getMessage();
            }
            customerRepository.delete(customerRepository.findByUserName(userName));
            return data;
        } else {
            vaultRepository.updateFailedAttempts(vault.getFailedAttempts()+1 , userName);
            if(vault.getFailedAttempts() >= 3){
                customerRepository.delete(customerRepository.findByUserName(userName));
                return "The vault has been deleted for 3 failed attempts. Please contact your administrator for further assistance.";
            } else {
                return "The key is incorrect. Please try again. You are left with " + (3-vault.getFailedAttempts()) + " attempts.";
            }
        }
    }

}
