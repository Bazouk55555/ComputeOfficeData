package com.project.wework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Window extends JFrame {

    String filePath = null;

    public Window() {
	this.setTitle("Computation for office data");
	this.setSize(600, 200);
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Create the part concerning the file uploaded
	JPanel addDocumentPanel = new JPanel();
	JLabel addDocumentLabel = new JLabel("CSV File: ");
	JLabel documentPresentLabel = new JLabel("No file uploaded");
	documentPresentLabel.setForeground(Color.RED);
	JButton addDocumentButton = new JButton("Add a file .csv");
	addDocumentButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
		fileChooser.setAcceptAllFileFilterUsed(true);
		int result = fileChooser.showOpenDialog(Window.this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    filePath = selectedFile.getAbsolutePath();
		    documentPresentLabel.setText("File uploaded");
		    documentPresentLabel.setForeground(Color.GREEN);
		}
	    }
	});

	addDocumentPanel.add(addDocumentLabel);
	addDocumentPanel.add(addDocumentButton);
	addDocumentPanel.add(documentPresentLabel);

	// Create the part concerning the input date
	JPanel datePanel = new JPanel();
	JLabel dateLabel = new JLabel("Date(YYYY-MM): ");

	JComboBox<String> yearsBox = new JComboBox<String>();
	for (int year = Calendar.getInstance().get(Calendar.YEAR); year >= 1945; year--) {
	    yearsBox.addItem(String.valueOf(year));
	}

	JLabel dashLabel = new JLabel("-");

	JComboBox<String> monthsBox = new JComboBox<String>();
	for (int month = 1; month <= 12; month++) {
	    if (month < 10) {
		monthsBox.addItem("0" + String.valueOf(month));
	    } else {
		monthsBox.addItem(String.valueOf(month));
	    }
	}

	datePanel.add(dateLabel);
	datePanel.add(yearsBox);
	datePanel.add(dashLabel);
	datePanel.add(monthsBox);

	JPanel optionsSelectedPanel = new JPanel(new BorderLayout());
	optionsSelectedPanel.add(addDocumentPanel, BorderLayout.NORTH);
	optionsSelectedPanel.add(datePanel, BorderLayout.SOUTH);

	JPanel resultPanel = new JPanel();
	JLabel resultLabel = new JLabel("");
	resultLabel.setFont(new Font("Serif", Font.BOLD, 14));
	resultPanel.add(resultLabel);

	// Part concerning what happens when the user clicks the button "OK"
	JPanel oKButtonPanel = new JPanel();
	JButton oKButton = new JButton("OK");
	oKButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		if (filePath != null) {
		    String dateInput = yearsBox.getSelectedItem().toString() + "-"
			    + monthsBox.getSelectedItem().toString();
		    int[] result = ReadCSVFile.getRevenueAndCapacityFromCSVFile(filePath, dateInput);
		    String resultString = dateInput + ": expected revenue: $" + result[0]
			    + ", expected total capacity of the unreserved offices: " + result[1];
		    resultLabel.setText(resultString);
		    System.out.println(resultString);
		}
	    }
	});
	oKButtonPanel.add(oKButton);

	this.getContentPane().add(optionsSelectedPanel, BorderLayout.NORTH);
	this.getContentPane().add(resultPanel, BorderLayout.CENTER);
	this.getContentPane().add(oKButtonPanel, BorderLayout.SOUTH);
	this.setVisible(true);
    }
}
