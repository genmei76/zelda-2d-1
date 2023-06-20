package com.zelda;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import com.object.OBJ_Key;

public class UI {
    
    GamePanel gamePanel;
    Graphics2D g2;
    Font maruMonica;

    BufferedImage keyImage;

    boolean messageOnScreen = false;
    String message = "";
    int messageTimer = 0;

    String currentDialog = "";

    public UI(GamePanel _gamePanel){
        gamePanel = _gamePanel;
        try {
            InputStream is = getClass().getResourceAsStream("/res/fonts/MaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OBJ_Key key = new OBJ_Key(_gamePanel);
        keyImage = key.getImage();
    }

    public void showMessage(String _message){
        message = _message;
        messageOnScreen = true;
    }

    public void draw(Graphics2D _g2){
        g2 = _g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.WHITE);

        if(gamePanel.gameState == gamePanel.PAUSE_STATE){
            drawPauseScreen();
        }else if(gamePanel.gameState == gamePanel.DIALOG_STATE){
            drawDialogScreen();
        }else if(gamePanel.gameState == gamePanel.PLAY_STATE){

            g2.drawImage(keyImage, 10, 10, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
            g2.drawString("x" + gamePanel.getPlayer().getNbKeys(), 60, 55);

            if(messageOnScreen){
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gamePanel.getTileSize()/2, gamePanel.getTileSize()*5);
                messageTimer++;

                if(messageTimer > 120){
                    messageOnScreen = false;
                    messageTimer = 0;
                }
            }
        }
    }

    private void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90F));
        int lenght = (int)g2.getFontMetrics().getStringBounds("PAUSE", g2).getWidth();
        int x = (gamePanel.getWidth() - lenght)/2;
        int y = gamePanel.getScreenHeight()/2;
        g2.drawString("PAUSE", x, y);
    }

    private void drawDialogScreen(){
        //Window
        int x = gamePanel.getTileSize();
        int y = gamePanel.getTileSize();
        int width = gamePanel.getScreenWidth() - gamePanel.getTileSize()*2;
        int height = gamePanel.getTileSize()*4;
        drawSubWindow(x, y, width, height);

        x += gamePanel.getTileSize();
        y += gamePanel.getTileSize();

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        for(String line : currentDialog.split("\n")){
            g2.drawString(line, x, y);
            y+=40;
        }
    }
    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void setCurrentDialog(String s){currentDialog = s;}
}
