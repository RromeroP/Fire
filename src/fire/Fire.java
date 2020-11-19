/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author dam
 */
public class Fire {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        int WIDTH = 800;
        int HEIGHT = 400;
        int COOLING = 0;
        
        BufferedImage IMG = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        Random rand = new Random();

        byte[] pixels = ((DataBufferByte) IMG.getRaster().getDataBuffer()).getData();

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(IMG.getWidth() + 15, IMG.getHeight() + 39);
        frame.setVisible(true);

        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(Color.black);
                
                start_fire();

                expand_fire();
                
                g.drawImage(IMG, 0, 0, null);

            }

            private void expand_fire() {
                for (int x = 0; x < IMG.getWidth(); x++) {
                    for (int y = 2; y < IMG.getHeight(); y++) {

                        int p_position = (x + (IMG.getHeight() - y) * IMG.getWidth());

                        int b_total = 0;
                        int g_total = 0;
                        int r_total = 0;

                        //Cojemos el color de los 4 pixeles adjacentes
                        b_total += getBGR(0, x, y - 1);
                        g_total += getBGR(1, x, y - 1);
                        r_total += getBGR(2, x, y - 1);

                        b_total += getBGR(0, x, y + 1);
                        g_total += getBGR(1, x, y + 1);
                        r_total += getBGR(2, x, y + 1);

                        b_total += getBGR(0, x - 1, y);
                        g_total += getBGR(1, x - 1, y);
                        r_total += getBGR(2, x - 1, y);

                        b_total += getBGR(0, x + 1, y);
                        g_total += getBGR(1, x + 1, y);
                        r_total += getBGR(2, x + 1, y);

                        b_total = b_total / 4;
                        g_total = g_total / 4;
                        r_total = r_total / 4 - COOLING;

                        //Comprobamos que no haya valores negativos
                        if (b_total < 0) {
                            b_total = 0;
                        } else if (g_total < 0) {
                            g_total = 0;
                        } else if (r_total < 0) {
                            r_total = 0;
                        }

                        //Cambiamos el color del pixel
                        setBGR(0, p_position, b_total);
                        setBGR(1, p_position, g_total);
                        setBGR(2, p_position, r_total);

                    }
                }
            }

            private int getBGR(int bgr, int x, int y) {
                int p_position = (x + (IMG.getHeight() - y) * IMG.getWidth());
                return pixels[bgr + 3 * p_position] & 0xff;
            }

            private void setBGR(int bgr, int p_position, int color) {
                pixels[bgr + 3 * p_position] = (byte) color;
            }

            private void start_fire() {
                for (int x = 0; x < IMG.getWidth(); x++) {
                    for (int y = 1; y < 2; y++) {

                        int p_position = (x + (IMG.getHeight() - y) * IMG.getWidth());

                        int rand_int = rand.nextInt(100);

                        //Cuanto mayor el numero del if, más fuego inicial. En este caso un 3%.
                        if (rand_int < 3) {
                            setBGR(0, p_position, 255);
                            setBGR(1, p_position, 255);
                            setBGR(2, p_position, 255);
                        }

                    }
                }
            }

        };

        frame.add(pane);
        
        //Redibujamos infinitamente
        while (true) {
            frame.repaint();
            Thread.sleep(10);
        }
    }

}
