package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Faculty;
import com.example.demo.repositories.FacultyRepository;

@Service
public class FacultyService {
	@Autowired
    private FacultyRepository facultyRepository;

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAllNotDeleted();
    }

    public Optional<Faculty> getFacultyById(long id) {
        return facultyRepository.findById(id);
    }

    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
}
