/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zodynas;

import java.awt.Font;
import javax.swing.JTextArea;

/**
 *
 * @author a
 */
class JMyTextArea extends JTextArea {
    
    public JMyTextArea(int rows, int cols, int fontsize) {
        setRows(rows);
        setColumns(cols);
	setFont(new Font("Arial", Font.PLAIN, fontsize));
	setAutoscrolls(true);
    }
    
    
}
