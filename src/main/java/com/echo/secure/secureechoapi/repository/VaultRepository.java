package com.echo.secure.secureechoapi.repository;

import com.echo.secure.secureechoapi.model.Vault;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VaultRepository extends JpaRepository<Vault , String> {
        void deleteByUserName(String userName);
        Vault findByUserName(String userName);

        @Modifying
        @Transactional
        @Query("UPDATE Vault v SET v.failedAttempts = :failedAttempts WHERE v.userName = :userName")
        void updateFailedAttempts(@Param("failedAttempts") int failedAttempts, @Param("userName") String userName);
}
