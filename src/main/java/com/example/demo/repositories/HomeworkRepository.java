package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

}
