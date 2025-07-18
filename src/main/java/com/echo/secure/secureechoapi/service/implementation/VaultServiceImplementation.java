package com.echo.secure.secureechoapi.service.implementation;

import com.echo.secure.secureechoapi.AESUtil.AESUtil;
import com.echo.secure.secureechoapi.Exceptions.SecureEchoAPIException;
import com.echo.secure.secureechoapi.model.Vault;
import com.echo.secure.secureechoapi.payload.VaultDTO;
import com.echo.secure.secureechoapi.repository.CustomerRepository;
import com.echo.secure.secureechoapi.repository.VaultRepository;
import com.echo.secure.secureechoapi.service.VaultService;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.DecimalMaxValidatorForInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if(vaultRepository.existsById(vaultDTO.getUserName())){
            return vaultDTO.getUserName() + "'s vault already exists. Cannot create a new one. Please contact your administrator for further assistance.";
        }
            String encrypted = AESUtil.encrypt(vaultDTO.getEncryptedData(), vaultDTO.getKey());
            String keyHash = passwordEncoder.encode(vaultDTO.getKey());

            Vault vault = new Vault();
            vault.setUserName(vaultDTO.getUserName());
            vault.setEncryptedData(encrypted);
            vault.setKey(keyHash);
            vault.setFailedAttempts(0);

            vaultRepository.save(vault);
            return "Data saved";
    }

    @Transactional
    @Override
    public String getVault(String userName, String key) throws Exception {
        Vault vault = vaultRepository.findById(userName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND , "User not found"));
        System.out.println(vault);
        if(passwordEncoder.matches(key , vault.getKey())){
//            vault.setFailedAttempts(0);
            System.out.println("Key matched");
            String data =  AESUtil.decrypt(vault.getEncryptedData() , key);
            delete(vault.getUserName());
            return data;
        } else {
            vault.setFailedAttempts(vault.getFailedAttempts() + 1);
            vaultRepository.save(vault);
            System.out.println("Key not matched");
            if(vault.getFailedAttempts() >= 3){
                delete(vault.getUserName());
                throw new SecureEchoAPIException(HttpStatus.UNAUTHORIZED , "The vault has been deleted for 3 failed attempts. Please contact your administrator for further assistance.");
            } else {
                throw new SecureEchoAPIException(HttpStatus.UNAUTHORIZED , "The key is incorrect. Please try again. You are left with " + (3-vault.getFailedAttempts()) + " attempts." );
            }
        }
    }
    private void delete(String userName){
        customerRepository.delete(customerRepository.findByUserName(userName));
        vaultRepository.deleteByUserName(userName);
        vaultRepository.flush();
        customerRepository.flush();
        System.out.println("deleted");
    }


//    private VaultDTO vaulttoVaultDTO(Vault vault){
//        VaultDTO vaultDTO = new VaultDTO();
//        vaultDTO.setUserName(vault.getUserName());
//        vaultDTO.setEncryptedData(vault.getEncryptedData());
//        vaultDTO.setKey(vault.getKey());
//        vaultDTO.setFailedAttempts(vault.getFailedAttempts());
//        return vaultDTO;
//    }

}
