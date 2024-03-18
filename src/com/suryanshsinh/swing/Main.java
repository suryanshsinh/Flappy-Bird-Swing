package com.suryanshsinh.swing;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		
		JFrame window = new JFrame();
		window.setIconImage(new ImageIcon("resources\\icon.png").getImage());
		window.setTitle("Flappy Bird");
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(game);
		window.pack();
		window.setLocationRelativeTo(null);
	}
}
