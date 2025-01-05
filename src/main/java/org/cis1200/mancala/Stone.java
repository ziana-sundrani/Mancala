package org.cis1200.mancala;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Stone {
    private static final String IMG_FILE = "src/mancalaStone.png";
    private int xPos;
    private int yPos;
    private static BufferedImage img;

    public Stone(int x, int y) {
        xPos = x;
        yPos = y;

        try {
            img = ImageIO.read(new File(IMG_FILE));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void drawStone(Graphics g) {
        g.drawImage(img, xPos, yPos, 15, 15, null);
    }
}
