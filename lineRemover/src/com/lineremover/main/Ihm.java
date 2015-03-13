package com.lineremover.main;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Ihm implements ActionListener {
	private JFileChooser fc;
	private JFrame jf;
	private JFormattedTextField number;
	private int num = 2;
	JPanel pane;
	File dir;
	JTextArea log;
	private JButton openButton, suppButton;

	public Ihm() {
		jf = new JFrame();
		pane = new JPanel();

		fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("select folder");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		openButton = new JButton("Select the folder");
		openButton.addActionListener(this);
		suppButton = new JButton("Remove the line number");
		suppButton.addActionListener(this);
		number = new JFormattedTextField();
		number.setColumns(3);
		number.setValue(new Integer(num));


		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		pane.add(openButton);
		pane.add(suppButton);
		pane.add(number);
		pane.add(logScrollPane);

		jf.add(pane);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(pane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				dir = fc.getSelectedFile();
				log.append("Path: "+dir.getAbsolutePath()+"\n");
				System.out.println("Directory:"+dir.getAbsolutePath());
			} else {
			}

			//Handle supp button action.
		} else if ((e.getSource() == suppButton)&&(dir != null)) {
			num = ((Number) number.getValue()).intValue();
			log.append("Remove the line "+num+" of every files\n");

			int confirm = JOptionPane.showConfirmDialog(jf,
					"Do you want to delete the line number "+num+" of all files located in "
							+dir.getAbsolutePath()+"?",
							"Confirmation",
							JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				BufferedReader br;
				File[] files = dir.listFiles();
				for (File file : files) {
					if (file.isFile()) {
						log.append("In process "+file.getName()+"\n");
						removeFromFile(file);

					}
				}
				JOptionPane.showMessageDialog(jf,
					    "Done!",
					    "",
					    JOptionPane.PLAIN_MESSAGE);
			}

		}

	}
	public void removeFromFile(File file) {
		try {
			//Construct the new file that will later be renamed to the original filename. 
			File tempFile = new File(file.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			//Read from the original file and write to the new one unless it is the line to delete
			int i = 1;
			while ((line = br.readLine()) != null) {	
				if (i != num){
					pw.println(line);
					pw.flush();
				}
				i++;
			}
			pw.close();
			br.close();

			//Delete the original file
			if (!file.delete()) {
				System.out.println("Could not delete file");
				return;
			} 

			//Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(file))
				System.out.println("Could not rename file");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}






