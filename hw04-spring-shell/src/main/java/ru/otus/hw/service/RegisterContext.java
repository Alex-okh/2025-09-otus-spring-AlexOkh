package ru.otus.hw.service;

import ru.otus.hw.domain.Student;

public interface RegisterContext {
    void register(Student student);
    Student getStudent();
    boolean isStudentRegistered();
}
