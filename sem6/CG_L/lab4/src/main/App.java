package main;

import javax.swing.SwingUtilities;

import main.view.AppFrame;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            AppFrame appFrame = new AppFrame();
            appFrame.setVisible(true);
        });

    }
}
