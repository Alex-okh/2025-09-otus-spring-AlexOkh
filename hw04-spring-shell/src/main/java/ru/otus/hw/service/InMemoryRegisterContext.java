package ru.otus.hw.service;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;

@Component
public class InMemoryRegisterContext implements RegisterContext {
    private Student currentStudent;

    @Override
    public void register(Student student) {
        this.currentStudent = student;
    }

    @Override
    public boolean isStudentRegistered() {
        return currentStudent != null;
    }

    @Override
    public Student getStudent() {
        return currentStudent;
    }
}
