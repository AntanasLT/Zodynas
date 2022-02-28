/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zodynas;

import java.awt.Toolkit;

/**
 *
 * @author a
 */
public class Zodynas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Main mainFrame;
	mainFrame = new Main(Integer.parseInt(args[0]), args[1]);
	mainFrame.setDefaultCloseOperation(Main.EXIT_ON_CLOSE);
//	mainFrame.setSize(700, 400);
        mainFrame.pack();
	mainFrame.setTitle("Žodynas");
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Zodynas.png"));
	mainFrame.setVisible(true);

    }

}
