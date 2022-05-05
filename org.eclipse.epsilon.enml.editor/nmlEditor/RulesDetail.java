package nmlEditor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import dom.MergeRule;
import enml.INmlModule;

import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Panel;
import java.awt.Choice;

public class RulesDetail extends JFrame {

	private JPanel contentPane;
	
	protected List<MergeRule> OctopusRule = new ArrayList<>();
	protected List<MergeRule> OursRule = new ArrayList<>();
	protected List<MergeRule> TransferRule = new ArrayList<>();
	
	TextArea textArea_OctopusDetails = new TextArea();
	TextArea textArea_OursDetails = new TextArea();
	TextArea textArea_TransferDetails = new TextArea();
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RulesDetail frame = new RulesDetail();
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
	public RulesDetail(INmlModule emlModule) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 907, 673);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Rules Details Window");
		 Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\Eclipse.png");    
	 	 setIconImage(icon);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Octopus Rules", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 13, 880, 176);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btn_Octopus = new JButton("Show Details");
		btn_Octopus.setBackground(SystemColor.controlHighlight);
		btn_Octopus.setBounds(748, 25, 120, 22);
		panel_1.add(btn_Octopus);
		
		JLabel lbl_Octopus = new JLabel("Total Rules: 0");
		lbl_Octopus.setBounds(12, 28, 133, 16);
		panel_1.add(lbl_Octopus);
		
		OctopusRule = emlModule.getOctopusRules();
		String OC_Rules = "Total Rules: " + String.valueOf(OctopusRule.size());
		lbl_Octopus.setText(OC_Rules);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(227, 227, 227)), "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_11.setBounds(12, 60, 856, 104);
		panel_1.add(panel_11);
		panel_11.setLayout(null);
		
		
		textArea_OctopusDetails.setBounds(10, 26, 836, 68);
		panel_11.add(textArea_OctopusDetails);
		
		Choice choice_Octopus = new Choice();
		choice_Octopus.setBounds(151, 25, 580, 22);
		panel_1.add(choice_Octopus);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Ours Rules", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(12, 202, 880, 176);
		contentPane.add(panel_2);
		
		JButton btn_Ours = new JButton("Show Details");
		btn_Ours.setBackground(SystemColor.controlHighlight);
		btn_Ours.setBounds(748, 25, 120, 22);
		panel_2.add(btn_Ours);
		
		JLabel lbl_Ours = new JLabel("Total Rules: 0");
		lbl_Ours.setBounds(12, 28, 133, 16);
		panel_2.add(lbl_Ours);
		
		OursRule = emlModule.getOursRules();
		String OU_Rules = "Total Rules: " + String.valueOf(OursRule.size());
		lbl_Ours.setText(OU_Rules);
		
		JPanel panel_22 = new JPanel();
		panel_22.setLayout(null);
		panel_22.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(227, 227, 227)), "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_22.setBounds(12, 60, 856, 104);
		panel_2.add(panel_22);
		
		
		textArea_OursDetails.setBounds(10, 26, 836, 68);
		panel_22.add(textArea_OursDetails);
		
		Choice choice_Ours = new Choice();
		choice_Ours.setBounds(151, 25, 580, 22);
		panel_2.add(choice_Ours);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Transfer Rules", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(12, 391, 880, 176);
		contentPane.add(panel_3);
		
		JButton btn_Transfer = new JButton("Show Details");
		btn_Transfer.setBackground(SystemColor.controlHighlight);
		btn_Transfer.setBounds(748, 25, 120, 22);
		panel_3.add(btn_Transfer);
		
		JLabel lbl_Transfer = new JLabel("Total Rules: 0");
		lbl_Transfer.setBounds(12, 28, 133, 16);
		panel_3.add(lbl_Transfer);
		
		TransferRule = emlModule.getTransferRules();
		String TR_Rules = "Total Rules: " + String.valueOf(TransferRule.size());
		lbl_Transfer.setText(TR_Rules);
		
		JPanel panel_33 = new JPanel();
		panel_33.setLayout(null);
		panel_33.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(227, 227, 227)), "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_33.setBounds(12, 60, 856, 104);
		panel_3.add(panel_33);
		
		
		textArea_TransferDetails.setBounds(10, 26, 836, 68);
		panel_33.add(textArea_TransferDetails);
		
		Choice choice_Transfer = new Choice();
		choice_Transfer.setBounds(151, 25, 580, 22);
		panel_3.add(choice_Transfer);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		panel_4.setBounds(0, 580, 902, 58);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		JButton btn_Cansel = new JButton("Cansel");
		btn_Cansel.setBackground(SystemColor.controlHighlight);
		btn_Cansel.setBounds(770, 13, 120, 35);
		panel_4.add(btn_Cansel);
		choice_Octopus.add("");
		choice_Ours.add("");
		choice_Transfer.add("");
		
		/// OC
		int importRuleListSize_Oc = OctopusRule.size();
		for (int i = 0 ; i < importRuleListSize_Oc ; i++) {
    		String MergingRuleName = OctopusRule.get(i).getName();
    		choice_Octopus.add(MergingRuleName);
    		}
		
		   //===================
		btn_Octopus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenRule_OC_DetailsActionPerformed(evt , OctopusRule , choice_Octopus);
            }
        });
        //====================
		
		/// OU
		
		int importRuleListSize_Ou = OursRule.size();
		for (int i = 0 ; i < importRuleListSize_Ou ; i++) {
    		String MergingRuleName = OursRule.get(i).getName();
    		choice_Ours.add(MergingRuleName);
    		}
		
		   //===================
		btn_Ours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenRule_OU_DetailsActionPerformed(evt , OursRule , choice_Ours);
            }
        });
        //====================
		
		/// TR
		
				int importRuleListSize_Tr = TransferRule.size();
				for (int i = 0 ; i < importRuleListSize_Tr ; i++) {
		    		String MergingRuleName = TransferRule.get(i).getName();
		    		choice_Transfer.add(MergingRuleName);
		    		}
				
				   //===================
				btn_Transfer.addActionListener(new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		                btnOpenRule_TR_DetailsActionPerformed(evt , TransferRule , choice_Transfer);
		            }
		        });
		        //====================
				
				btn_Cansel.addActionListener(new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	Btn_CanselAction(evt);
		            }
		        });
        
	}
	
private void btnOpenRule_OC_DetailsActionPerformed(java.awt.event.ActionEvent evt , List<MergeRule> MergingRule , Choice choice ) {//GEN-FIRST:event_btnListJFramesActionPerformed
        
        try {
        	String choiceItem = choice.getItem(choice.getSelectedIndex());
        	int importRuleListSize = MergingRule.size();

    		for (int i = 0 ; i < importRuleListSize ; i++) {
    		String MergingRuleName = MergingRule.get(i).getName();
    		if(choiceItem == MergingRuleName) {
    			String MergingRuleNam = MergingRule.get(i).toString();
    			textArea_OctopusDetails.setText(  MergingRuleName + "\n" + MergingRuleNam);
    		}
    			}
    		
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

private void btnOpenRule_OU_DetailsActionPerformed(java.awt.event.ActionEvent evt , List<MergeRule> MergingRule , Choice choice ) {//GEN-FIRST:event_btnListJFramesActionPerformed
    
    try {
    	String choiceItem = choice.getItem(choice.getSelectedIndex());
    	int importRuleListSize = MergingRule.size();

		for (int i = 0 ; i < importRuleListSize ; i++) {
		String MergingRuleName = MergingRule.get(i).getName();
		if(choiceItem == MergingRuleName) {
			String MergingRuleNam = MergingRule.get(i).toString();
			textArea_OursDetails.setText(  MergingRuleName + "\n" + MergingRuleNam);
		}
			}
		
    }
    catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

private void btnOpenRule_TR_DetailsActionPerformed(java.awt.event.ActionEvent evt , List<MergeRule> MergingRule , Choice choice ) {//GEN-FIRST:event_btnListJFramesActionPerformed
    
    try {
    	String choiceItem = choice.getItem(choice.getSelectedIndex());
    	int importRuleListSize = MergingRule.size();

		for (int i = 0 ; i < importRuleListSize ; i++) {
		String MergingRuleName = MergingRule.get(i).getName();
		if(choiceItem == MergingRuleName) {
			String MergingRuleNam = MergingRule.get(i).toString();
			textArea_TransferDetails.setText(  MergingRuleName + "\n" + MergingRuleNam);
		}
			}
		
    }
    catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

public void Btn_CanselAction(ActionEvent e)
{

	   setVisible(false); //you can't see me!
	   dispose(); //Destroy the JFrame object
     
}
}
