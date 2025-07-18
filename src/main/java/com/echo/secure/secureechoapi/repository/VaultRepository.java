package com.echo.secure.secureechoapi.repository;

import com.echo.secure.secureechoapi.model.Vault;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaultRepository extends JpaRepository<Vault , String> {
        void deleteByUserName(String userName);
}
