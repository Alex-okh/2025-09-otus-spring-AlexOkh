package ru.otus.hw.service;

import java.util.Scanner;

public class InputServiceConsole implements InputService {
    Scanner scanner;

    public InputServiceConsole() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int getInt() {
        return scanner.nextInt();
    }
}
