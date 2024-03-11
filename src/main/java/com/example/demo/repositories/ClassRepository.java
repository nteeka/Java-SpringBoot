package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Classes;


public interface ClassRepository extends JpaRepository<Classes, Long>{

}
