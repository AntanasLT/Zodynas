/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zodynas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author createInputs
 */
public class Main extends JFrame implements ActionListener, MouseListener {

    JPanel pnInputs, pnWords;
    JScrollPane sPlist, sPedit;
    JMyButton btAt_begin, btAll, btNew, btEdit, btDelete, btSave, btRecent;
    JMyTextField tfSearch, tfRecent;
    JMyTextArea taEdit;
    JList<String> listWords;
    DefaultListModel<String> dlm;
    JLabel lStatus;
    
    int fontSize, selected, count;
    int[] index;
    String dict_filename;
    ArrayList<String> words;
    ListSelectionListener listener;
    
    final int MAX_WORDS = 100;
    final int RECENT = 30;
    final String DICT_FILE = "ell.dat";


    public Main(int size, String dat) {
	fontSize = size;
        dict_filename = dat;
	init();
    }

    private void init() {
	setLayout(new BorderLayout());
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
                show_save_question();
		System.exit(0);
	    }
	});
        listener = (ListSelectionEvent lse) -> {
            getText();
        };
	createPanel();
	createInputs();
        read_dat();
        index = new int[MAX_WORDS];
        tfSearch.requestFocus();      
    }

    private void createPanel() {
	lStatus = new JLabel();
	add(lStatus, BorderLayout.SOUTH);
        dlm = new DefaultListModel<>();
        listWords = new JList<>(dlm);
        listWords.addListSelectionListener(listener);
        pnWords = new JPanel(new GridLayout(2, 1));
        taEdit = new JMyTextArea(10, 40, fontSize);
        taEdit.setWrapStyleWord(true);
        taEdit.setLineWrap(true);
	sPlist = new JScrollPane(listWords);
        listWords.setFont(new Font("Arial", Font.PLAIN, fontSize));
        pnWords.add(sPlist);
        sPedit = new JScrollPane(taEdit);
        pnWords.add(sPedit);
	add(pnWords, BorderLayout.CENTER);
    }

    private void createInputs() {
	pnInputs = new JPanel();
	btNew = new JMyButton("Naujas", fontSize);
	btNew.addActionListener(this);
	btNew.setActionCommand("new");
        btNew.setMnemonic('N');
	pnInputs.add(btNew);
        
	btEdit = new JMyButton("Keisti", fontSize);
	btEdit.setActionCommand("edit");
        btEdit.setMnemonic('K');
	btEdit.addActionListener(this);
	pnInputs.add(btEdit);
        
	btDelete = new JMyButton("Trinti", fontSize);
	btDelete.setActionCommand("delete");
	btDelete.addActionListener(this);
	pnInputs.add(btDelete);
        
	btSave = new JMyButton("Išsaugoti", fontSize);
	btSave.setActionCommand("save");
	btSave.addActionListener(this);
	pnInputs.add(btSave);
        
	tfSearch = new JMyTextField("", 20, fontSize);
        tfSearch.addActionListener(this);
        tfSearch.addMouseListener(this);
	pnInputs.add(tfSearch);
        
	btAt_begin = new JMyButton("Pradžia", fontSize);
	btAt_begin.setActionCommand("at_begin");
	btAt_begin.addActionListener(this);
        btAt_begin.setMnemonic('P');
	pnInputs.add(btAt_begin);
        
	btAll = new JMyButton("Bet kur", fontSize);
//        btAll.setForeground(Color.green);
	btAll.setActionCommand("all");
	btAll.addActionListener(this);
        btAll.setMnemonic('B');
	pnInputs.add(btAll);
        
	tfRecent = new JMyTextField(String.valueOf(RECENT), 3, fontSize);
        tfRecent.addActionListener(this);
        tfRecent.addMouseListener(this);
	pnInputs.add(tfRecent);
        
	btRecent = new JMyButton("Paskutinieji", fontSize);
	btRecent.setActionCommand("recent");
	btRecent.addActionListener(this);
	pnInputs.add(btRecent);
        
	this.add(pnInputs, BorderLayout.NORTH);
        
    }

    
    private void read_dat() {
	BufferedReader reader;
	String line;
	reader = null;
        count = 0;
	try {
	    reader = new BufferedReader(new FileReader(dict_filename));
            words = new ArrayList<>();
	    while ((line = reader.readLine()) != null ) {
                words.add(line);
                count++;
	    }
            lStatus.setText(String.valueOf(count));
	} catch (IOException ex) {
            show_error(ex.toString());
	} finally {
	    try {
                reader.close();
	    } catch (IOException|NullPointerException ex) {
		show_error(ex.toString());
	    }
	}	
    }
    
    private void search(String string, boolean at_begin) {
        int i, n;
        i = 0; n = 0;
        dlm.clear();
        if (!string.isEmpty()) {
            if (!at_begin) {
                for (String line : words) {
                    if (line.contains(string) & n < MAX_WORDS) {
                        dlm.addElement(line);
                        index[n] = i;
                        n++;
                    }
                    i++;
                }
            } else {
                for (String line : words) {
                    if (line.startsWith(string) & n < MAX_WORDS) {
                        dlm.addElement(line);
                        index[n] = i;
                        n++;
                    }
                    i++;
                }
            }
        }
        lStatus.setText(String.valueOf(n));
        if (n == MAX_WORDS) {
            lStatus.setForeground(Color.RED);
        } else {
            lStatus.setForeground(Color.BLACK);
        }
        tfSearch.requestFocus();
    }

    private void getText() {
        if (!listWords.isSelectionEmpty()) {
            selected = listWords.getSelectedIndex();
            taEdit.setText(dlm.getElementAt(selected));
            lStatus.setText(String.valueOf(selected + count - RECENT));
        }
    }
    
    private void edit() {
        String text;
        text = taEdit.getText();
        dlm.add(selected+1, text);
        dlm.remove(selected);
        words.remove(index[selected]);
        words.add(index[selected], text);
    }
    
    private void add() {
        String text;
        text = taEdit.getText();
        dlm.clear();
        count++;
        dlm.add(0, text);
        words.add(text);
        lStatus.setText(String.valueOf(count));
    }
    
    private void delete() {
        dlm.remove(selected);
        words.remove(index[selected]);
        count--;
        lStatus.setText(String.valueOf(count));
    }

    private void save() {
        try {
            Writer f;
            f = new FileWriter(DICT_FILE);
            for (String line : words)
                f.write(line + "\n");
            f.close();
        } catch (IOException ex) {
            show_error(ex.toString());
        }
    }

    private void show_error(String message) {
        JOptionPane.showMessageDialog(this, message, "Λάθος;", JOptionPane.ERROR_MESSAGE);
    }

    private void show_save_question() {
       if(JOptionPane.showConfirmDialog(this, "Išsaugoti pakeitimus?", "Išsaugojimas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
           save();
       };        
    }

    private void recent() {
        dlm.clear();
        for (int i = count - Integer.parseInt(tfRecent.getText()); i < count; i++) {
            dlm.addElement(words.get(i));
        }
    }

//    private void getVersion() {
//	Manifest manifest;
//	Attributes attr;
//	try {
//	    manifest = new Manifest(new JarInputStream(new BufferedInputStream(new FileInputStream("manifest.mf"))));
//	    attr = manifest.getMainAttributes();
//
////	    attr = manifest.getAttributes("MANIFEST_VERSION");
////	    System.out.println(attr.getValue("MANIFEST_VERSION"));
//	    System.out.println(attr.getValue("Manifest-Version"));
//	} catch (IOException ex) {
//	    System.out.println(ex.toString());
//	}
//    }




    @Override
    public void actionPerformed(ActionEvent ae) {
	String command;
	command = ae.getActionCommand();
	switch (command) {
	    case "at_begin":
                search(tfSearch.getText(), true);
		break;
	    case "all":
		search(tfSearch.getText(), false);;
		break;
	    case "edit":
                edit();
		break;
	    case "save":
		save();
		break;
	    case "delete":
                delete();
		break;
	    case "new":
                add();
		break;
	    case "recent":
                recent();
		break;
	}
        if (ae.getSource().equals(tfSearch)) {
            search(tfSearch.getText(), false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON3) {
            tfSearch.setText("");
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

}
