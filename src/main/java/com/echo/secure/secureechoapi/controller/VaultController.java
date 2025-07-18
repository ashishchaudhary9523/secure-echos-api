package com.echo.secure.secureechoapi.controller;

import com.echo.secure.secureechoapi.Exceptions.SecureEchoAPIException;
import com.echo.secure.secureechoapi.model.Vault;
import com.echo.secure.secureechoapi.payload.AccessDTO;
import com.echo.secure.secureechoapi.payload.VaultDTO;
import com.echo.secure.secureechoapi.repository.VaultRepository;
import com.echo.secure.secureechoapi.service.VaultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vault")
public class VaultController {

    private final VaultService vaultService;

    @Autowired
    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/store-data")
    public ResponseEntity<?> storeData(@Valid @RequestBody VaultDTO vaultDTO){
        try {
            String response = vaultService.storeVault(vaultDTO);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "Failed to store data" , e.getMessage());
        }
    }

    @PostMapping("/get-data")
    public ResponseEntity<?> getVault(@RequestBody AccessDTO accessDTO){
        try {
            String response = vaultService.getVault(accessDTO.getUserName() , accessDTO.getKey());
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
            throw new SecureEchoAPIException(HttpStatus.BAD_REQUEST , "Failed to get data" , e.getMessage());
        }
    }


}
