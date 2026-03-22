package com.example.VIBES.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VIBES.Model.User;

@Repository
public interface UserRepo extends JpaRepository<User , Long>{  
     User findByUsername(String username);
}
