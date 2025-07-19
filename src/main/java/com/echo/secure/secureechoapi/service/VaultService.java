package com.echo.secure.secureechoapi.service;

import com.echo.secure.secureechoapi.model.Vault;
import com.echo.secure.secureechoapi.payload.VaultDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public interface VaultService {
    String storeVault(VaultDTO vaultDTO) throws Exception;
    String getVault(String userName , String key) throws Exception;
}
