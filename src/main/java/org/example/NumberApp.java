package org.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
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
        setSize(400, 700);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        createDefaultWindow();
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

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new BoxLayout(numberPanel, BoxLayout.X_AXIS));
        JPanel currentColumn = new JPanel();
        currentColumn.setLayout(new BoxLayout(currentColumn, BoxLayout.Y_AXIS));

        for (Integer number : randomNumbers) {
            JButton button = new JButton(number.toString());
            button.setBackground(Color.BLUE);
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> onNumberClick(number));

            currentColumn.add(button);
            currentColumn.add(Box.createRigidArea(new Dimension(30, 15)));


            if (currentColumn.getComponentCount() == 20) {
                numberPanel.add(currentColumn);
                currentColumn = new JPanel();
                currentColumn.setLayout(new BoxLayout(currentColumn, BoxLayout.Y_AXIS));
            }
        }

        if (currentColumn.getComponentCount() > 0) {
            numberPanel.add(currentColumn);
        }

        JButton sortButton = new JButton("Sort");
        sortButton.setBackground(Color.GREEN);
        sortButton.addActionListener(e -> onSortClick());

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.GREEN);
        resetButton.addActionListener(e -> onResetClick());

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        controlPanel.add(sortButton);
        controlPanel.add(resetButton);

        mainPanel.add(numberPanel, BorderLayout.WEST);
        mainPanel.add(controlPanel, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void onNumberClick(int number) {
        if (number <= 30) {
            generateRandomNumbers(number);
            displayNumbersPage();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a number less than or equal to 30.");
        }
    }

    private void onSortClick() {
        new Thread(() -> {
            List<Integer> sortedNumbers = new ArrayList<>(randomNumbers);
            quickSort(sortedNumbers, 0, sortedNumbers.size() - 1);
        }).start();
        isDescending = !isDescending;
    }

    private void onResetClick() {
        mainPanel.removeAll();
        mainPanel.setLayout(null);
        createDefaultWindow();
        add(mainPanel);
        enterButton.addActionListener(e -> onSubmit());

        revalidate();
        repaint();
    }

    public void quickSort(List<Integer> nums, int start, int finish) {
        if (start < finish) {
            int pIndex = partition(nums, start, finish, isDescending);
            updateUI(nums);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            quickSort(nums, start, pIndex - 1);
            quickSort(nums, pIndex + 1, finish);
        } else {
            SwingUtilities.invokeLater(() -> {
                randomNumbers.clear();
                randomNumbers.addAll(nums);
                displayNumbersPage();
            });
        }
    }

    private void createDefaultWindow() {
        JLabel label = new JLabel("How many numbers to display?");
        label.setBounds(100, 50, 200, 30);
        mainPanel.add(label);

        numberField = new JTextField(10);
        numberField.setBounds(100, 100, 200, 30);
        mainPanel.add(numberField);

        enterButton = new JButton("Enter");
        enterButton.setBackground(Color.BLUE);
        enterButton.setForeground(Color.WHITE);
        enterButton.setBounds(100, 150, 200, 30);
        mainPanel.add(enterButton);
    }

    private int partition(List<Integer> nums, int start, int finish, boolean isDescending) {
        int pivot = nums.get(finish);
        int i = start - 1;

        for (int j = start; j < finish; j++) {
            if (isDescending) {
                if (nums.get(j) >= pivot) {
                    i++;
                    int temp = nums.get(i);
                    nums.set(i, nums.get(j));
                    nums.set(j, temp);
                }
            } else {
                if (nums.get(j) <= pivot) {
                    i++;
                    int temp = nums.get(i);
                    nums.set(i, nums.get(j));
                    nums.set(j, temp);
                }
            }
        }
        int temp = nums.get(i + 1);
        nums.set(i + 1, nums.get(finish));
        nums.set(finish, temp);
        return i + 1;
    }

    private void updateUI(List<Integer> newList) {
        SwingUtilities.invokeLater(() -> {
            randomNumbers.clear();
            randomNumbers.addAll(newList);
            displayNumbersPage();
        });
    }
}
