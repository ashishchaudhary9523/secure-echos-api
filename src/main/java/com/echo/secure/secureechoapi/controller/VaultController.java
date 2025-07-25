package com.echo.secure.secureechoapi.controller;

import com.echo.secure.secureechoapi.payload.AccessDTO;
import com.echo.secure.secureechoapi.payload.VaultDTO;
import com.echo.secure.secureechoapi.service.VaultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173")
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
           return new ResponseEntity<>( "Failed to store data" , HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get-data")
    public ResponseEntity<?> getVault(@RequestBody AccessDTO accessDTO){
        String response = null;
        try {
            response = vaultService.getVault(accessDTO.getUserName() , accessDTO.getKey());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response , HttpStatus.OK);

    }


}
