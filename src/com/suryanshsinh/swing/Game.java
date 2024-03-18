package com.suryanshsinh.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener{
	Random r = new Random();
	final int PANEL_WIDTH = 432;
	final int PANEL_HEIGHT = 768;
	final int birdWidth = 51;
	final int birdHeight = 36;
	final int pipeWidth = 78;
	final int pipeHeight = 480;
	Image bird;
	Image ground1;
	Image ground2;
	Image[] pipe1 = new Image[2];
	Image[] pipe2 = new Image[2];
	Image background;
	Timer timer;
	Image gameOverImage = new ImageIcon("resources\\gameover.png").getImage();
	Image flappyBirdText = new ImageIcon("resources\\flappy-bird.png").getImage();
	int birdx = 80;
	int birdy = 200;
	int velocity = -5;
	int gravity = 1;
	int lift = 13;
	int pipeVelocity = 2;
	int pipe1x = PANEL_WIDTH;
	int pipe2x = PANEL_WIDTH + 216 + pipeWidth / 2;
	int pipe1y = -1*(r.nextInt((400 - 80) + 1) + 80);
	int pipe2y = -1*(r.nextInt((400 - 80) + 1) + 80);
	int g1x = 0;
	int g2x = PANEL_WIDTH;
	int frame = 1;
	int preGameVelocity = 2;
	int score = 0;
	boolean assignedScore = false;
	boolean game = false;
	boolean gameover = false;
	int delayFrame = 0;
	boolean allowedInput = true;
	
	Game() {
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(this);
		background = new ImageIcon("resources\\background.png").getImage();
		pipe1[0] = new ImageIcon("resources\\top-pipe.png").getImage();
		pipe1[1] = new ImageIcon("resources\\bottom-pipe.png").getImage();
		pipe2[0] = new ImageIcon("resources\\top-pipe.png").getImage();
		pipe2[1] = new ImageIcon("resources\\bottom-pipe.png").getImage();
		ground1 = new ImageIcon("resources\\ground.png").getImage();
		ground2 = new ImageIcon("resources\\ground.png").getImage();
		bird = new ImageIcon("resources\\bird1.png").getImage();
		timer = new Timer(8, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(background, 0, 0, 432, 786,  null);
		g2D.drawImage(pipe1[0], pipe1x, pipe1y, pipeWidth, pipeHeight, null);
		g2D.drawImage(pipe1[1], pipe1x, pipe1y + pipeHeight + 150, pipeWidth, pipeHeight, null);
		g2D.drawImage(pipe2[0], pipe2x, pipe2y, pipeWidth, pipeHeight, null);
		g2D.drawImage(pipe2[1], pipe2x, pipe2y + pipeHeight + 150, pipeWidth, pipeHeight, null);
		g2D.drawImage(ground1, g1x, PANEL_HEIGHT - 144, PANEL_WIDTH, 144, null);
		g2D.drawImage(ground2, g2x, PANEL_HEIGHT - 144, PANEL_WIDTH, 144, null);
		

        Font customFont = new Font("Segoe UI Black", Font.PLAIN, 40);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(customFont);
		g2D.drawString(""+score, 20, 50);

	    // Calculate rotation angle based on the velocity (Credits: mike-bedar on stackoverflow.com)
	    double rotationAngle = Math.atan2(velocity, 20) + Math.PI / 20;
	    
		if (game) {
		    g2D.rotate(rotationAngle, birdx + 26, birdy + 18);
		    g2D.drawImage(bird, birdx, birdy, birdWidth, birdHeight, null);
		    g2D.rotate(-rotationAngle, birdx + 26, birdy + 18);
		}
		else if (gameover) {
		    g2D.rotate(rotationAngle, birdx + 26, birdy + 18);
		    g2D.drawImage(bird, birdx, birdy, birdWidth, birdHeight, null);
		    g2D.rotate(-rotationAngle, birdx + 26, birdy + 18);
			g2D.drawImage(gameOverImage, (PANEL_WIDTH / 2) - 115, (PANEL_HEIGHT / 2) - 51, 231, 75, null);
		}
		else {
		    g2D.drawImage(bird, birdx, birdy, 51, 36, null);
			g2D.drawImage(flappyBirdText, (PANEL_WIDTH / 2) - 115, (PANEL_HEIGHT / 2) - 51, 231, 90, null);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Rectangle pipe1TopRect = new Rectangle(pipe1x, pipe1y, pipeWidth, pipeHeight);		
		Rectangle pipe1BottomRect = new Rectangle(pipe1x, pipe1y + pipeHeight + 150, pipeWidth, pipeHeight);	
		Rectangle pipe2TopRect = new Rectangle(pipe2x, pipe2y, pipeWidth, pipeHeight);		
		Rectangle pipe2BottomRect = new Rectangle(pipe2x, pipe2y + pipeHeight + 150, pipeWidth, pipeHeight);		
		Rectangle birdRect = new Rectangle(birdx, birdy, birdWidth, birdHeight);
		if (game) {
			pipe1x -= pipeVelocity;
			pipe2x -= pipeVelocity;
			g1x -= pipeVelocity;
			g2x -= pipeVelocity;
			g1x = (g1x <= -PANEL_WIDTH) ? PANEL_WIDTH - pipeVelocity : g1x;
			g2x = (g2x <= -PANEL_WIDTH) ? PANEL_WIDTH - pipeVelocity : g2x;
			if (pipe1x <= -pipeWidth) {
				pipe1x = PANEL_WIDTH - pipeVelocity;
				pipe1y = -1*(r.nextInt((400 - 80) + 1) + 80);
				assignedScore = false;
			}
			if (pipe2x <= -pipeWidth) {
				pipe2x = PANEL_WIDTH - pipeVelocity;
				pipe2y = -1*(r.nextInt((400 - 80) + 1) + 80);
				assignedScore = false;
			}
			pipe2x = (pipe2x <= -pipeWidth) ? PANEL_WIDTH - pipeVelocity : pipe2x;
			velocity += gravity;
			birdy += velocity;
			if (birdy < 0) {
				birdy = 0;
			}
			if ((pipe1x < birdx - pipeWidth && !assignedScore) || (pipe2x < birdx - pipeWidth && !assignedScore)) {
				score++;
				assignedScore = true;
			}
		}
		else if (gameover){
			if (birdy <= PANEL_HEIGHT - 144 - birdHeight) {
				birdy += velocity;
				velocity += gravity;
			}
			delayFrame += 1;
			if (delayFrame > 100 ) {
				allowedInput = true;
				delayFrame = 0;
			}
		}
		else { // pre-game
			birdy += preGameVelocity;
			if (birdy == 180 || birdy == 220) {
				preGameVelocity *= -1;
			}
		}
		
		if (!gameover) {
			frame++;
			if (frame == 5) {
				bird = new ImageIcon("resources\\bird2.png").getImage();
			}
			else if (frame == 10) {
				bird = new ImageIcon("resources\\bird3.png").getImage();
			}
			else if (frame == 15) {
				bird = new ImageIcon("resources\\bird1.png").getImage();
				frame = 1;
			}
			if (birdy >= PANEL_HEIGHT - 144 - birdHeight) {
				birdy = PANEL_HEIGHT - 144 - birdHeight;
				game = false;
				gameover = true;
				allowedInput = false;
			}
			if(birdRect.intersects(pipe1TopRect) || 
					birdRect.intersects(pipe1BottomRect) || 
					birdRect.intersects(pipe2TopRect) || 
					birdRect.intersects(pipe2BottomRect)) {
				game = false;
				gameover = true;
				allowedInput = false;
			}
		}
		
		repaint();
	}
	
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ' && allowedInput) {
			if (game) {
				velocity = -lift;
			}
			else if (gameover) {
				gameover = false;
				score = 0;
				birdy = 200;
				velocity = -10;
				pipe1x = PANEL_WIDTH;
				pipe2x = PANEL_WIDTH + 216 + pipeWidth / 2;
				pipe1y = -1*(r.nextInt((400 - 80) + 1) + 80);
				pipe2y = -1*(r.nextInt((400 - 80) + 1) + 80);
			}
			else {
				game = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
