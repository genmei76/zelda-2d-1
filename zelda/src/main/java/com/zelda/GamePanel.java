package com.zelda;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {
    //Screen settings
    final int originalTileSize = 16; //16x16 pixels
    final int scale = 3; //16x16 pixels * 3 = 48x48 pixels

    final int tileSize = originalTileSize * scale;

    final int maxScreencol = 16;
    final int maxScreenrow = 12;
    final int screenWidth = tileSize * maxScreencol; //768 pixels
    final int screenHeight = tileSize * maxScreenrow; //576 pixels

    final int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //Player settings
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyHandler);
        setFocusable(true);
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while(gameThread != null){
            double drawInterval = 1000000000.0 / FPS; // 0.01666666666666666666666666666667 seconds
            double nextDrawTime = System.nanoTime() + drawInterval;

            update();
            repaint();

            try {
                double remainingTimeToDraw = nextDrawTime - System.nanoTime();
                if(remainingTimeToDraw < 0)
                    remainingTimeToDraw = 0;

                Thread.sleep((long) remainingTimeToDraw / 1000000);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(keyHandler.upPressed){
            playerY -= playerSpeed;
        } else if(keyHandler.downPressed){
            playerY += playerSpeed;
        } else if(keyHandler.leftPressed){
            playerX -= playerSpeed;
        } else if(keyHandler.rightPressed){
            playerX += playerSpeed;
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }

}