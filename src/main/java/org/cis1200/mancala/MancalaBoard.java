package org.cis1200.mancala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MancalaBoard extends JPanel {
    private Mancala m;
    private boolean paused = false;
    private JLabel playerStatus;

    public MancalaBoard(JLabel playerStat) {
        m = new Mancala();

        playerStatus = playerStat;
        playerStatus.setText(m.getPlayerStatus());

        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                // updates the game state
                m.playTurn(p.y / 100, p.x / 100);

                playerStatus.setText(m.getPlayerStatus());
                repaint(); // repaints the game board
            }
        });
    }

    public void reset() {
        m = new Mancala();
        playerStatus.setText(m.getPlayerStatus());
        repaint();
    }

    public boolean getPaused() {
        return paused;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(m.getWhilePlayingStatus(), 205, 400);
        if (!paused) {
            Color tan = new Color(212, 178, 152);
            Color brown = new Color(119, 89, 66);
            setBackground(tan);

            g.setColor(tan);
            g.fillRect(0, 0, 200, 900);

            g.setColor(brown);
            //draw stores
            g.fillRoundRect(0, 0, 200, 100, 10, 10);
            g.fillRoundRect(0, 700, 200, 100, 10, 10);

            //draw holes
            for (int j = 0; j < 200; j += 100) {
                for (int i = 100; i < 700; i += 100) {
                    g.fillOval(j, i, 100, 100);
                }
            }

            //drawStones
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 2; c++) {
                    int count = m.getStoneNum(r, c);
                    int xPos = c * 100 + 25;
                    int yPos = r * 100 + 30;
                    if (r == 0 && c == 1) {
                        xPos = 25;
                    }
                    if (!(r == 0 && c == 0) && !(r == 7 && c == 1)) {
                        g.setColor(Color.WHITE);
                        g.fillOval(xPos - 4, yPos - 13, 30, 15);
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(count), xPos,yPos);
                    }
                    for (int i = 0; i < count; i++) {
                        Stone s = new Stone(xPos, yPos);
                        s.drawStone(g);
                        xPos += 7;
                        if (r == 7 && c == 0) {
                            if (xPos > 190) {
                                xPos = c * 100 + 25;
                                yPos += 10;
                            }
                        } else if (r == 0 && c == 1 && xPos > 190) {
                            xPos = 25;
                            yPos += 10;
                        } else {
                            if (xPos > c * 100 + 90) {
                                xPos = c * 100 + 25;
                                yPos += 10;
                            }
                        }
                    }
                }
            }
        }
    }

    //override get preferred size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 800);
    }

    public void undoBoard() {
        m.undo();
        playerStatus.setText(m.getPlayerStatus());
        repaint();

        requestFocusInWindow();
    }

    public void pause(String filename) {
        if (!paused) {
            m.saveBoard(filename);
        }
        paused = true;
        playerStatus.setText("Paused");
        repaint();
    }

    public void resume(String filename) {
        m.reUpload(filename);
        playerStatus.setText("Resumed");
        paused = false;
        repaint();
        playerStatus.setText(m.getPlayerStatus());
    }


}
