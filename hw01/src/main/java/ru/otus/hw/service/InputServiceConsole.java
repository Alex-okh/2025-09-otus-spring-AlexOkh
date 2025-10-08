package ru.otus.hw.service;

import java.util.Scanner;

public class InputServiceConsole implements InputService {
    private final Scanner scanner;

    public InputServiceConsole() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int getInt() {
        return scanner.nextInt();
    }
}
