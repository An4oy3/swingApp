package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NumberApp extends JFrame {

    private final JPanel mainPanel;
    private JButton enterButton;
    private JTextField numberField;
    private List<Integer> randomNumbers;
    private boolean isDescending = true;

    public NumberApp() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        JLabel label = new JLabel("How many numbers to display?");
        numberField = new JTextField(10);
        enterButton = new JButton("Enter");

        mainPanel.add(label);
        mainPanel.add(numberField);
        mainPanel.add(enterButton);
        add(mainPanel);

        enterButton.addActionListener(e -> onSubmit());
    }

    private void onSubmit() {
        try {
            int count = Integer.parseInt(numberField.getText());
            if (count <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive number.");
                return;
            }
            generateRandomNumbers(count);
            displayNumbersPage();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private void generateRandomNumbers(int count) {
        Random random = new Random();
        randomNumbers = new ArrayList<>();
        boolean hasLessThan30 = false;

        while (randomNumbers.size() < count) {
            int num = random.nextInt(1000) + 1;
            if (num <= 30) {
                hasLessThan30 = true;
            }
            randomNumbers.add(num);
        }

        if (!hasLessThan30) {
            randomNumbers.set(random.nextInt(randomNumbers.size()), random.nextInt(30) + 1);
        }
    }

    private void displayNumbersPage() {
        mainPanel.removeAll();
        JPanel numberPanel = new JPanel(new GridLayout(0, 10, 10, 10));

        for (Integer number : randomNumbers) {
            JButton button = new JButton(number.toString());
            button.addActionListener(e -> onNumberClick(number));
            numberPanel.add(button);
        }

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> onSortClick());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> onResetClick());

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(numberPanel, BorderLayout.CENTER);
        mainPanel.add(sortButton, BorderLayout.SOUTH);
        mainPanel.add(resetButton, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    private void onNumberClick(int number) {
        if (number <= 30) {
            generateRandomNumbers(randomNumbers.size());
            displayNumbersPage();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a number less than or equal to 30.");
        }
    }

    private void onSortClick() {
        if (isDescending) {
            Collections.reverse(quickSort(randomNumbers, 0, randomNumbers.size() - 1));
        } else {
            quickSort(randomNumbers, 0, randomNumbers.size() - 1);
        }
        isDescending = !isDescending;
        displayNumbersPage();
    }

    private void onResetClick() {
        mainPanel.removeAll();
        JLabel label = new JLabel("How many numbers to display?");
        numberField = new JTextField(10);
        enterButton = new JButton("Enter");

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(label);
        mainPanel.add(numberField, new GridBagConstraints());
        mainPanel.add(enterButton, new GridBagConstraints());
        add(mainPanel);
        enterButton.addActionListener(e -> onSubmit());

        revalidate();
        repaint();
    }

    public List<Integer> quickSort(List<Integer> nums, int start, int finish) {
        if (start < finish) {
            int pIndex = partition(nums, start, finish);
            quickSort(nums, start, pIndex - 1);
            quickSort(nums, pIndex + 1, finish);
        }
        return nums;
    }

    private int partition(List<Integer> nums, int start, int finish) {
        int pivot = nums.get(finish);
        int i = start - 1;


        for (int j = start; j < finish; j++) {
            if (nums.get(j) <= pivot) {
                i++;
                int temp = nums.get(i);
                nums.set(i, nums.get(j));
                nums.set(j, temp);
            }
        }
        int temp = nums.get(i + 1);
        nums.set(i + 1, nums.get(finish));
        nums.set(finish, temp);
        return i + 1;
    }
}
