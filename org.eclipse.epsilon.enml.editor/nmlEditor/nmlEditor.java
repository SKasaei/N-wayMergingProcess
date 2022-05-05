package nmlEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import org.fife.ui.rtextarea.*;

import dom.MergeRule;
import dom.importModel;

import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.erl.dom.NamedRuleList;
import org.fife.ui.rsyntaxtextarea.*;
import enml.NmlModule;
import enml.INmlModule;
import executor.properties;
import loadingModel.loadingModel;

/**
 * use RSyntaxTextArea to add Java syntax
 * highlighting to a Swing application.
 */
public class nmlEditor extends JFrame implements ActionListener{

	
	
   private static final long serialVersionUID = 1L;
   // Text component
   JTextArea t;
   // Frame
   JFrame f;
   JPanel cp = new JPanel(new BorderLayout());
   RSyntaxTextArea textArea = new RSyntaxTextArea(30, 100);
   
   public nmlEditor() {

	  textArea.setFont(new Font("Monospaced",Font.PLAIN,18));
      textArea.setHighlightCurrentLine(false);
     // textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
      AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
      atmf.putMapping("text/myLanguage", "colors.color");
      textArea.setSyntaxEditingStyle("text/myLanguage");
      textArea.setCodeFoldingEnabled(true);
      RTextScrollPane sp = new RTextScrollPane(textArea);
      textArea.getText();
      sp.getGutter().setLineNumberColor(Color.decode("#04b976"));
      sp.getGutter().setLineNumberFont(new Font("Monospaced",Font.BOLD,18));
      cp.add(sp);
      //MenuBar
      setJMenuBar(createMenuBar(textArea));
      SyntaxScheme esquema = textArea.getSyntaxScheme();
      esquema.getStyle(Token.RESERVED_WORD).foreground = Color.decode("#8B008B");
      
      setContentPane(cp);
      setTitle("NML Editor");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);
 	  Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\nml.png");    
 	  setIconImage(icon);

   }

   
   private  JMenuBar createMenuBar(RSyntaxTextArea textArea) {

       JMenuBar menuBar = new JMenuBar();
       
       JMenu fileMenu = new JMenu("File");
       
       JMenuItem mi2 = new JMenuItem("Open");
       JMenuItem mi3 = new JMenuItem("Save");

       // Add action listener
       mi2.addActionListener(this);
       mi3.addActionListener(this);
       fileMenu.add(mi2);
       fileMenu.add(mi3);
       
       JMenu editMenu = new JMenu("Edit");
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)));
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)));
       editMenu.addSeparator();
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)));
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)));
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)));
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)));
       editMenu.addSeparator();
       editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)));

       menuBar.add(fileMenu);
       menuBar.add(editMenu);

       // validate
       JMenu Run = new JMenu("Run");
       JMenuItem RulesDetails=new JMenuItem("Rules Details"); 
       JMenuItem v1 = new JMenuItem("Validate");
       JMenuItem v2 = new JMenuItem("Execute");



       // Add action listener
       v1.addActionListener(this);
       v2.addActionListener(this);
       RulesDetails.addActionListener(this);
   

       Run.add(v1);
       Run.add(v2);

   
       Run.add(RulesDetails);


       menuBar.add(Run);

       return menuBar;
   }

   private static JMenuItem createMenuItem(Action action) {
       JMenuItem item = new JMenuItem(action);
       item.setToolTipText(null); // Swing annoyingly adds tool tip text to the menu item
       return item;
   }
   
   // If a button is pressed
   public void actionPerformed(ActionEvent e)
   {
       String s = e.getActionCommand();
       if (s.equals("Save")) {
           // Create an object of JFileChooser class
           JFileChooser j = new JFileChooser("f:");
           // Invoke the showsSaveDialog function to show the save dialog
           int r = j.showSaveDialog(null);
           if (r == JFileChooser.APPROVE_OPTION) {
               // Set the label to the path of the selected directory
        	   File fi = new File(j.getSelectedFile().getAbsolutePath() + ".nml");
               try {
                   // Create a file writer
                   FileWriter wr = new FileWriter(fi, false);
                   // Create buffered writer to write
                   BufferedWriter w = new BufferedWriter(wr);
                   // Write
                   w.write(textArea.getText());
                   w.flush();
                   w.close();
               }
               catch (Exception evt) {
                   JOptionPane.showMessageDialog(f, evt.getMessage());
               }
           }
           // If the user cancelled the operation
           else
               JOptionPane.showMessageDialog(f, "the user cancelled the operation");
       }
       else if (s.equals("Open")) {
           // Create an object of JFileChooser class
           JFileChooser j = new JFileChooser("f:");
           // Invoke the showsOpenDialog function to show the save dialog
           int r = j.showOpenDialog(null);
           // If the user selects a file
           if (r == JFileChooser.APPROVE_OPTION) {
               // Set the label to the path of the selected directory
               File fi = new File(j.getSelectedFile().getAbsolutePath());
               try {
                   // String
                   String s1 = "", sl = "";
                   // File reader
                   FileReader fr = new FileReader(fi);
                   // Buffered reader
                   BufferedReader br = new BufferedReader(fr);
                   // Initialize sl
                   sl = br.readLine();
                   // Take the input from the file
                   while ((s1 = br.readLine()) != null) {
                       sl = sl + "\n" + s1;
                   }
                   // Set the text
                   textArea.setText(sl);
               }
               catch (Exception evt) {
                   JOptionPane.showMessageDialog(f, evt.getMessage());
               }
           }
           // If the user cancelled the operation
           else
               JOptionPane.showMessageDialog(f, "the user cancelled the operation");
       }      
       //================================
       else if (s.equals("Validate")) {
    	   try {
               String currentDir = System.getProperty("user.dir");
               currentDir = currentDir + "\\org.eclipse.epsilon.enml.editor\\nmlFile\\CurrentTextArea.nml";
    		   //create file 
        	   File fi1 = new File(currentDir);
        	   // Create a file writer
               FileWriter wr = new FileWriter(fi1, false);
               // Create buffered writer to write
               BufferedWriter w = new BufferedWriter(wr);
               // Write
               if(textArea.getText().length() == 0)
            	   JOptionPane.showMessageDialog(f, " write your code ");
               else {
               w.write(textArea.getText());
               w.flush();
               w.close();
               //================
    		   INmlModule emlModule = new NmlModule();
    		   Boolean parseFile = emlModule.parse(new URI("file:///C:/Users/Admin/git/org.eclipse.epsilon.ep/org.eclipse.epsilon.enml.editor/nmlFile/CurrentTextArea.nml"));
    		   if (parseFile)
    		   JOptionPane.showMessageDialog(f, " your code is correct ");
    		   else {
    			   JOptionPane.showMessageDialog(f, " your code needs modification ");
    		   }
               }
    	   }
    	   catch (Exception evt) {
               JOptionPane.showMessageDialog(f, evt.getMessage());
           }
       }
       //===========
       else if (s.equals("Execute")) {
    	   try {
    		   
               String currentDir = System.getProperty("user.dir");
               currentDir = currentDir + "\\org.eclipse.epsilon.enml.editor\\nmlFile\\CurrentTextArea.nml";
    		   //create file 
        	   File fi1 = new File(currentDir);
        	   // Create a file writer
               FileWriter wr = new FileWriter(fi1, false);
               // Create buffered writer to write
               BufferedWriter w = new BufferedWriter(wr);
               // Write
               if(textArea.getText().length() == 0)
            	   JOptionPane.showMessageDialog(f, " write your code ");
               else {
               w.write(textArea.getText());
               w.flush();
               w.close();
               //================
    		   INmlModule emlModule = new NmlModule();
    		   Boolean parseFile = emlModule.parse(new URI("file:///C:/Users/Admin/git/org.eclipse.epsilon.ep/org.eclipse.epsilon.enml.editor/nmlFile/CurrentTextArea.nml"));
    		//   loadingModel loadImortedModel = new loadingModel();

    		   if (parseFile) {
    			   
    			   btnOpenExecuteJFrameActionPerformed(e , emlModule);
    			   
    		   }

    		   else {
    			   JOptionPane.showMessageDialog(f, " your code needs modification ");
    		   } 
               }
    	   }
    	   catch (Exception evt) {
               JOptionPane.showMessageDialog(f, evt.getMessage());
           }
       }
       else if (s.equals("Rules Details")) {
    	   
    	   try {
               String currentDir = System.getProperty("user.dir");
               currentDir = currentDir + "\\org.eclipse.epsilon.enml.editor\\nmlFile\\CurrentTextArea.nml";
    		   //create file 
        	   File fi1 = new File(currentDir);
        	   // Create a file writer
               FileWriter wr = new FileWriter(fi1, false);
               // Create buffered writer to write
               BufferedWriter w = new BufferedWriter(wr);
               // Write
               if(textArea.getText().length() == 0)
            	   JOptionPane.showMessageDialog(f, " write your code ");
               else {
               w.write(textArea.getText());
               w.flush();
               w.close();
               //================
    		   INmlModule emlModule = new NmlModule();
    		   Boolean parseFile = emlModule.parse(new URI("file:///C:/Users/Admin/git/org.eclipse.epsilon.ep/org.eclipse.epsilon.enml.editor/nmlFile/CurrentTextArea.nml"));
    		   if (parseFile) {
    			   btnOpenRulesJFrameActionPerformed(e , emlModule , "Octopus");
    		   }
    		   
    		   else {
    			   JOptionPane.showMessageDialog(f, " your code needs modification ");
    		   }
               }
    	   }
    	   catch (Exception evt) {
               JOptionPane.showMessageDialog(f, evt.getMessage());
           }
       }
    
       

   }
   
   private void btnOpenExecuteJFrameActionPerformed(java.awt.event.ActionEvent evt ,INmlModule emlModule) {
       /* Create an object of childJFrame */
     //  execute child = new execute(emlModule);
       /* Make childJFrame visible */
    //   child.setVisible(true);
       
       try {
			properties PropertiesFrame = new properties(emlModule);
			PropertiesFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   private void btnOpenRulesJFrameActionPerformed(java.awt.event.ActionEvent evt ,INmlModule emlModule , String ruleType) {
       /* Create an object of childJFrame */
	   RulesDetail child = new RulesDetail(emlModule);
       /* Make childJFrame visible */
       child.setVisible(true);
   }
} 