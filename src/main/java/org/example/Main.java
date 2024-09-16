package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberApp app = new NumberApp();
            app.setVisible(true);
        });
    }
}
