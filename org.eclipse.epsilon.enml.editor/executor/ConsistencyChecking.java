package executor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.evl.launch.EvlRunConfiguration;

import enml.INmlModule;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.SystemColor;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.awt.TextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;

public class ConsistencyChecking extends JFrame {

	private JPanel contentPane;
	private JTextField textField_EVL;

	protected Path EVL_File = null;
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsistencyChecking frame = new ConsistencyChecking();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the frame.
	 */
	public ConsistencyChecking(INmlModule emlModule) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 580, 573);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Consistency Checking Window");
		 Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\Eclipse.png");    
	 	 setIconImage(icon);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)), "NML Facilities", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(0, 0, 574, 64);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		Label label_2 = new Label("Please select required file ...");
		label_2.setBounds(10, 25, 360, 24);
		panel_1.add(label_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		panel_3.setBounds(0, 489, 574, 51);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btn_Cansel = new JButton("Cansel");
		btn_Cansel.setBackground(SystemColor.controlHighlight);
		btn_Cansel.setBounds(465, 13, 85, 26);
		panel_3.add(btn_Cansel);
		
		JButton btn_Finish = new JButton("Finish");
		btn_Finish.setBackground(SystemColor.controlHighlight);
		btn_Finish.setBounds(368, 13, 85, 26);
		panel_3.add(btn_Finish);
		
		JButton btn_Next = new JButton("Next >");
		btn_Next.setEnabled(false);
		btn_Next.setBackground(SystemColor.controlHighlight);
		btn_Next.setBounds(271, 14, 85, 26);
		panel_3.add(btn_Next);
		
		JButton btn_Back = new JButton("< Back");
		btn_Back.setEnabled(false);
		btn_Back.setBackground(SystemColor.controlHighlight);
		btn_Back.setBounds(174, 14, 85, 26);
		panel_3.add(btn_Back);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 67, 574, 422);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_DesignRational = new JPanel();
		panel_DesignRational.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Design Rational", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_DesignRational.setBounds(12, 61, 550, 110);
		panel_2.add(panel_DesignRational);
		panel_DesignRational.setLayout(null);
		
		Label label = new Label("You can view the merger details using option Design Rational:");
		label.setBounds(10, 37, 360, 24);
		panel_DesignRational.add(label);
		
		JButton btnDesignRational = new JButton("Design Rational");
		btnDesignRational.setBackground(SystemColor.controlHighlight);
		btnDesignRational.setBounds(405, 62, 133, 35);
		panel_DesignRational.add(btnDesignRational);
		
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setLayout(null);
		panel_2_1.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Select EVL File (optional)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2_1.setBounds(12, 235, 550, 126);
		panel_2.add(panel_2_1);
		
		JButton btn_Set_EVL = new JButton("Browse ...");
		btn_Set_EVL.setBackground(SystemColor.controlHighlight);
		btn_Set_EVL.setBounds(443, 88, 95, 25);
		panel_2_1.add(btn_Set_EVL);
		
		textField_EVL = new JTextField();
		textField_EVL.setColumns(10);
		textField_EVL.setBounds(12, 89, 419, 22);
		panel_2_1.add(textField_EVL);
		
		Label label_1 = new Label("You can use option Consistency Checking to enter your constraints to check ");
		label_1.setBounds(12, 32, 490, 24);
		panel_2_1.add(label_1);
		
		Label label_1_1 = new Label("the consistency of the created model:");
		label_1_1.setBounds(12, 48, 492, 24);
		panel_2_1.add(label_1_1);
		
		
		
		
		  ////////////////////////////////////
        ///////action//////////////////
		
		btn_Finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (EVL_File != null) {
            	FINISHAction(evt ,  emlModule);
            	}
            	//// store
            	 int w = emlModule.getContext().getModelRepository().getModels().size();
       		  	 IModel TargetModel = emlModule.getContext().getModelRepository().getModels().get((w-1));
       		  	 TargetModel.store();
       		  	 Btn_CanselAction(evt);
            }
        });
		
		btn_Set_EVL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	EVL_File = BrowseEVLAction(evt);
            }
        });
		
		btn_Cansel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Btn_CanselAction(evt);
            }
        });
		
		btnDesignRational.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			////// DR window
				DesignRational DesignRationalWindow = new DesignRational(emlModule);
				DesignRationalWindow.setVisible(true);
	    		setVisible(false);
			}

		});
		
		
	}
	
	   public void Btn_CanselAction(ActionEvent e)
	   {

		   setVisible(false); //you can't see me!
		   dispose(); //Destroy the JFrame object
	        
	   }
	   
	   public Path BrowseEVLAction(ActionEvent e)
	   {

	           // Create an object of JFileChooser class
	           JFileChooser j = new JFileChooser("f:");
	           // Invoke the showsOpenDialog function to show the save dialog
	           int r = j.showOpenDialog(null);
	           // If the user selects a file
	           if (r == JFileChooser.APPROVE_OPTION) {
	               // Set the label to the path of the selected directory
	               File fi = new File(j.getSelectedFile().getAbsolutePath());
	               textField_EVL.setText(fi.toString());
	               
	               Path path = fi.toPath();
	               return path;
	              
	           }
	           return null;
	        
	   }
	   
	   public void FINISHAction(ActionEvent e , INmlModule emlModule)
	   {
		   
		  int q = emlModule.getContext().getModelRepository().getModels().size();
		  IModel TargetModel = emlModule.getContext().getModelRepository().getModels().get((q-1));
		  
		///// Consistency checking
			EvlRunConfiguration runConfig = EvlRunConfiguration.Builder()
					.withScript(EVL_File)
					.withModel(TargetModel)
					//.withParameter("greeting", "Hello from ")
					.withProfiling()
					.withResults()
					.withParallelism()
					.build();
				
				runConfig.run(); 
	        
	   }
}
