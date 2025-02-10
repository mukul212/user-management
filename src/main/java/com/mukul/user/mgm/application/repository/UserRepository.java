package com.mukul.user.mgm.application.repository;

import com.mukul.user.mgm.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRoleIgnoreCase(@Param("role") String role);

    User findByUserName(@Param("userName") String userName);
}
