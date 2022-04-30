package executor;

import static org.eclipse.epsilon.common.util.OperatingSystem.getJavaVersion;
import static org.eclipse.epsilon.common.util.OperatingSystem.getOsNameAndVersion;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getAvailableMemory;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getCpuName;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getMaxMemory;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getNumberOfHardwareThreads;
import static org.eclipse.epsilon.common.util.profiling.BenchmarkUtils.getTime;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import org.eclipse.epsilon.common.util.profiling.ProfileDiagnostic.MemoryUnit;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;

import enml.EmlModule;
import enml.IEmlModule;
import execute.context.IEmlContext;
import loadingModel.loadingModel;


import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextField;
import java.awt.Font;

public class properties extends JFrame {

	private JPanel contentPane;
	private JTextField textField_ECL;
	private JTextField textField_MetaModel;
	private String MergingStrategy = "Three-way";

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					properties frame = new properties();
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
	public properties(IEmlModule emlModule) {
		
		IEmlModule emlModule_Original = new EmlModule();
		try {
			emlModule_Original.parse(new URI("file:///C:/Users/Admin/git/org.eclipse.epsilon.ep/org.eclipse.epsilon.enml.editor/nmlFile/CurrentTextArea.nml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 685, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		 setTitle("Setting Properties");
		 
		 Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\Eclipse.png");    
	 	 setIconImage(icon);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 73, 680, 440);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 680, 64);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new TitledBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(128, 128, 128)), "Model Selection", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 514, 680, 50);
		panel_3.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
		
		JButton btn_Cansel = new JButton("Cansel");
		btn_Cansel.setBackground(SystemColor.controlHighlight);
		
		JButton btn_Finish = new JButton("Finish");
		btn_Finish.setEnabled(false);
		btn_Finish.setBackground(SystemColor.controlHighlight);
		
		JButton btn_Next = new JButton("Next >");
		btn_Next.setBackground(SystemColor.controlHighlight);
		
		JButton btn_Back = new JButton("< Back");
		btn_Back.setEnabled(false);
		btn_Back.setBackground(SystemColor.controlHighlight);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(185, Short.MAX_VALUE)
					.addComponent(btn_Back, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btn_Next, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btn_Finish, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btn_Cansel, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(btn_Back, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Next, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Finish, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Cansel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		
		JLabel lbl_Please = new JLabel("Please select required files ...");
		lbl_Please.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbl_Please, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(283, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lbl_Please, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel_2.setLayout(null);
		
		JPanel panel_ECL = new JPanel();
		panel_ECL.setBounds(12, 347, 656, 69);
		panel_2.add(panel_ECL);
		panel_ECL.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Select ECL File", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JButton btn_Set_ECL = new JButton("Browse...");
		btn_Set_ECL.setBackground(SystemColor.controlHighlight);
		
		textField_ECL = new JTextField();
		textField_ECL.setColumns(10);
		GroupLayout gl_panel_ECL = new GroupLayout(panel_ECL);
		gl_panel_ECL.setHorizontalGroup(
			gl_panel_ECL.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_ECL.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField_ECL, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btn_Set_ECL, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_ECL.setVerticalGroup(
			gl_panel_ECL.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_ECL.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_ECL.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_ECL, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Set_ECL, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panel_ECL.setLayout(gl_panel_ECL);
		
		JPanel panel_MM = new JPanel();
		panel_MM.setBounds(12, 19, 656, 69);
		panel_2.add(panel_MM);
		panel_MM.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Select the Meta-Model for Non-UML models", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		textField_MetaModel = new JTextField();
		textField_MetaModel.setColumns(10);
		
		JButton btn_SetMetaModel = new JButton("Browse...");
		btn_SetMetaModel.setEnabled(false);
		btn_SetMetaModel.setBackground(SystemColor.controlHighlight);
		GroupLayout gl_panel_MM = new GroupLayout(panel_MM);
		gl_panel_MM.setHorizontalGroup(
			gl_panel_MM.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_MM.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField_MetaModel, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btn_SetMetaModel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_MM.setVerticalGroup(
			gl_panel_MM.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_MM.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_MM.createParallelGroup(Alignment.BASELINE)
						.addComponent(btn_SetMetaModel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_MetaModel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_MM.setLayout(gl_panel_MM);
		
		JPanel panel_Base = new JPanel();
		panel_Base.setBounds(12, 183, 656, 69);
		panel_2.add(panel_Base);
		panel_Base.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Select Base Model", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Base.setLayout(null);
		
		Choice choiceBase = new Choice();
		choiceBase.setBounds(10, 29, 624, 39);
		panel_Base.add(choiceBase);
		
		JPanel panel_Target = new JPanel();
		panel_Target.setBounds(12, 265, 656, 69);
		panel_2.add(panel_Target);
		panel_Target.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Select Target Model", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Target.setLayout(null);
		
		Choice choiceTarget = new Choice();
		choiceTarget.setBounds(10, 29, 622, 39);
		panel_Target.add(choiceTarget);
		contentPane.setLayout(null);
		contentPane.add(panel_3);
		contentPane.add(panel_1);
		contentPane.add(panel_2);
		
		JPanel panel_LoadModel = new JPanel();
		panel_LoadModel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Loading Input Models", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_LoadModel.setBounds(12, 101, 656, 69);
		panel_2.add(panel_LoadModel);
		
		JButton btn_Load = new JButton("Load Models");
		btn_Load.setBackground(SystemColor.controlHighlight);
		btn_Load.setForeground(Color.BLACK);
		
		
		
		JButton btn_Load_Successful = new JButton(" ");
		btn_Load_Successful.setEnabled(false);
		btn_Load_Successful.setForeground(Color.BLACK);
		btn_Load_Successful.setBackground(Color.LIGHT_GRAY);
		
		JButton btn_Load_Unsuccessful = new JButton(" ");
		btn_Load_Unsuccessful.setForeground(Color.BLACK);
		btn_Load_Unsuccessful.setEnabled(false);
		btn_Load_Unsuccessful.setBackground(Color.RED);
		GroupLayout gl_panel_LoadModel = new GroupLayout(panel_LoadModel);
		gl_panel_LoadModel.setHorizontalGroup(
			gl_panel_LoadModel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_LoadModel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btn_Load_Unsuccessful, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_Load_Successful, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(btn_Load, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_LoadModel.setVerticalGroup(
			gl_panel_LoadModel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_LoadModel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_LoadModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btn_Load_Successful, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Load_Unsuccessful, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Load, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_LoadModel.setLayout(gl_panel_LoadModel);
		
		
		////////////////////////////////
		//////btn Action///////////////
		
       

        choiceBase.add("");
		choiceTarget.add("");
		
		btn_Cansel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Btn_CanselAction(evt);
            }
        });
		
		btn_Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	boolean Loading_models = LoadInputModelsAction(evt , emlModule, choiceBase , choiceTarget);
            	if(Loading_models) {
            	btn_Load_Successful.setBackground(Color.GREEN);
            	btn_Load_Unsuccessful.setBackground(Color.LIGHT_GRAY);
            	btn_Load.setEnabled(false);
            	}
            }
        });
		
		btn_SetMetaModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	BrowseMetaModelAction(evt);
            	btn_Load.setEnabled(true);
            }
        });
		
		btn_Set_ECL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	ECL_File = BrowseECLAction(evt);
            }
        });
		
		btn_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnNextAction(evt , emlModule, choiceBase , choiceTarget , ECL_File , emlModule_Original);
            }
        });
		
		////// EMF Meta Model enable
		ArrayList<String> importModelFileNames = new ArrayList<String>();
    	importModelFileNames = emlModule.getImportModelFileName();
    	String modelFileName = importModelFileNames.get(0);
    	int indexOf_type = modelFileName.indexOf(".model");
    	int indexOf_type2 = modelFileName.indexOf(".xmi");
    	if (indexOf_type != -1) {
    		btn_SetMetaModel.setEnabled(true);
    		btn_Load.setEnabled(false);
		}
    	if (indexOf_type2 != -1) {
    		btn_SetMetaModel.setEnabled(true);
    		btn_Load.setEnabled(false);
		}
	}
	
	//////////////////////////////////////////
	////////////// Next
	protected File ECL_File = null;
	
	  private void btnNextAction(java.awt.event.ActionEvent evt , IEmlModule emlModule , Choice choiceBase , Choice choiceTarget , File ECLFile , IEmlModule emlModule_Original) {//GEN-FIRST:event_btnOpenChildJFrameActionPerformed

	    	try {
	    		if (ECLFile != null) {
	    	if (choiceBase.getSelectedItem()!="" && choiceTarget.getSelectedItem()!="" && choiceTarget.getSelectedItem()!=choiceBase.getSelectedItem()) {

	    		emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()).getAliases().clear();
	    		emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()).getAliases().add(choiceTarget.getSelectedItem());
	    		emlModule.getContext().addTModel(emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()));
	    		emlModule.getContext().getModelRepository().removeModel(emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()));
	    		
	    		if(MergingStrategy.equals("Three-way")) {
	    		emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()).getAliases().clear();
	    		emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()).getAliases().add(choiceBase.getSelectedItem());
	    		emlModule.getContext().addBModel(emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()));
	    		emlModule.getContext().getModelRepository().removeModel(emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()));
	    		}
	    				
	    	////system Info
	    		emlModule.getContext().setMergerReport(getOsNameAndVersion());
	    		emlModule.getContext().setMergerReport(getJavaVersion());
	    		emlModule.getContext().setMergerReport(getCpuName());
	    		emlModule.getContext().setMergerReport("Logical processors: "+getNumberOfHardwareThreads());
	    		emlModule.getContext().setMergerReport("Xms: "+getAvailableMemory(MemoryUnit.MB));
	    		emlModule.getContext().setMergerReport("Xmx: "+getMaxMemory(MemoryUnit.MB));
	    		emlModule.getContext().setMergerReport("Starting execution at "+getTime());
	    		emlModule.getContext().setMergerReport("-------------------------------------------------");
	    		////////////////////// Match with base
	 	       SimpleDateFormat sdf
	           = new SimpleDateFormat(
	               "dd-MM-yyyy HH:mm:ss.SSS");
		    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"); 
			LocalDateTime now = LocalDateTime.now();   
			String StartTimeMatch =   dtf1.format(now);
			Date StartTimeMatch1;
						
				if(MergingStrategy.equals("Two-way")) {
					emlModule.getContext().nmlMatchTraceWithoutBase(ECLFile);
				}else {
	    		emlModule.getContext().comparingInputModels_withBase(ECLFile);
				}
	    		
				now = LocalDateTime.now();   
				String EndTimeMatch =   dtf1.format(now);
				Date EndTimeMatch1;
				
				try {
					EndTimeMatch1 = sdf.parse(EndTimeMatch);
					StartTimeMatch1 = sdf.parse(StartTimeMatch);
					computeDiff(StartTimeMatch1, EndTimeMatch1 , emlModule.getContext());

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	    		
	    	//	emlModule.getContext().getModelRepository().addModel(emlModule.getContext().getBModel());
	    	//	emlModule.getContext().getModelRepository().addModel(emlModule.getContext().getTModel());
				if(MergingStrategy.equals("Two-way")) {
					//EX
					
					try {
				    	emlModule.getContext().getModelRepository().addModel(emlModule.getContext().getTModel());
						emlModule.execute();	
					} catch (EolRuntimeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					////// CC window
					ConsistencyChecking ConsistencyCheckingWindow = new ConsistencyChecking(emlModule);
					ConsistencyCheckingWindow.setVisible(true);
		    		setVisible(false);
				}else {
					
	    		matchWindow comparisonWindow = new matchWindow(emlModule , emlModule_Original);
	    		comparisonWindow.setVisible(true);
	    		setVisible(false);
				}
	    	
	    	}else {
	    		JOptionPane.showMessageDialog(this, " Select Base and Target model! ");
	    			}
	    		}else {
		    		JOptionPane.showMessageDialog(this, " Select ECL file! ");
    			}
	    		
	    	}
	    	catch (Exception e) {
	            JOptionPane.showMessageDialog(this, e.getMessage());
	        }
	    }
	
	///////////////////////////////////////////
	////// Import Models/////////////////////////
	
	   private Boolean LoadInputModelsAction(java.awt.event.ActionEvent evt , IEmlModule emlModule , Choice choiceBase , Choice choiceTarget) {//GEN-FIRST:event_btnListJFramesActionPerformed
	        
	        try {
			loadingModel loadImortedModel = new loadingModel();

	        
	        ArrayList<String> importModelList = new ArrayList<String>();
			importModelList = emlModule.getImportModel();
			
	    	ArrayList<String> importModelNames = new ArrayList<String>();
	    	importModelNames = emlModule.getImportModelName();
	    	
	    	ArrayList<String> importModelFileNames = new ArrayList<String>();
	    	importModelFileNames = emlModule.getImportModelFileName();
	    	
			int importModelListSize = importModelList.size();
			Boolean EMFLoad = false;
			
			for (int i = 0 ; i < importModelListSize ; i++) {
				if (EMFLoad == true) {break;}
				
				String modelFileName = importModelFileNames.get(i);
				
				// if original version not imported
				if (modelFileName.equals("null")) {
					MergingStrategy = "Two-way";
					choiceBase.add("The original version has not been imported");
					break;
					}
				
				int alt_type = 0;
				int indexOf_type = modelFileName.indexOf(".xml");
				if (indexOf_type != -1) {
					alt_type = 1;
				} else {
					indexOf_type = modelFileName.indexOf(".uml");
					if (indexOf_type != -1) {
						alt_type = 2;
					} else {
						indexOf_type = modelFileName.indexOf(".model");
						if (indexOf_type != -1) {
							alt_type = 3;
						}else {
							indexOf_type = modelFileName.indexOf(".xmi");
							if (indexOf_type != -1) {
								alt_type = 3;
							}
						}
					}
				}
				switch (alt_type) {
				// XML
				case 1:
				{
				emlModule.getContext().getModelRepository().addModels(loadImortedModel.loadPlainXmlModel(importModelList.get(i),importModelNames.get(i)));
				System.out.println(importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
				choiceBase.add(importModelNames.get(i));
				choiceTarget.add(importModelNames.get(i));
				}
					break;
				// UML
				case 2:
				{
					emlModule.getContext().getModelRepository().addModels(loadImortedModel.loadUMLModel(importModelNames.get(i) , importModelList.get(i)));
					System.out.println(importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
					choiceBase.add(importModelNames.get(i));
					choiceTarget.add(importModelNames.get(i));
				}
					break;
				// EMF
				case 3:
				{
					
					if ( textField_MetaModel.getText().isEmpty() ) {
					JOptionPane.showMessageDialog(this, " Set Your Meta-Model ");
					}else {
						SetMetaModelAction(emlModule , choiceBase , choiceTarget);
						EMFLoad = true;
					}
			     


				}
					break;
				 default:
					 break;
				}
				
			}
		
			return true;
	    }
	        catch (Exception e) {
	            JOptionPane.showMessageDialog(this, e.getMessage());
	            return false;
	        }
	    }
	   
	   // SET META MODEL FOR EMF
	   
	   private void SetMetaModelAction(IEmlModule emlModule , Choice choiceBase , Choice choiceTarget) {//GEN-FIRST:event_btnListJFramesActionPerformed
	        
	        try {
	        	
	        	loadingModel loadImortedModel = new loadingModel();

	            ArrayList<String> importModelList = new ArrayList<String>();
	    		importModelList = emlModule.getImportModel();
	    		
	        	ArrayList<String> importModelNames = new ArrayList<String>();
	        	importModelNames = emlModule.getImportModelName();
	        	
	        	ArrayList<String> importModelFileNames = new ArrayList<String>();
	        	importModelFileNames = emlModule.getImportModelFileName();
	        	
	    		int importModelListSize = importModelList.size();
				String MetaModelText = textField_MetaModel.getText();

	    		for (int i = 0 ; i < importModelListSize ; i++) {
	    		String modelFileName = importModelFileNames.get(i);
	        	emlModule.getContext().getModelRepository().addModels(loadImortedModel.loadEMFModel(importModelList.get(i), MetaModelText ,importModelNames.get(i)));
				//String previousText = textArea.getText();
			//	textArea.setText(previousText + importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
	        	System.out.println( "Meta-Model = "+importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
	  
				choiceBase.add(importModelNames.get(i));
				choiceTarget.add(importModelNames.get(i));
	    		}
	       
	   }
	        catch (Exception e) {
	            JOptionPane.showMessageDialog(this, e.getMessage());
	        }
	    }
	   
	   public void BrowseMetaModelAction(ActionEvent e)
	   {

	           // Create an object of JFileChooser class
	           JFileChooser j = new JFileChooser("f:");
	           // Invoke the showsOpenDialog function to show the save dialog
	           int r = j.showOpenDialog(null);
	           // If the user selects a file
	           if (r == JFileChooser.APPROVE_OPTION) {
	               // Set the label to the path of the selected directory
	               File fi = new File(j.getSelectedFile().getAbsolutePath());
	               textField_MetaModel.setText(fi.toString());
	              
	           }
	        
	   }
	   
	   public File BrowseECLAction(ActionEvent e)
	   {

	           // Create an object of JFileChooser class
	           JFileChooser j = new JFileChooser("f:");
	           // Invoke the showsOpenDialog function to show the save dialog
	           int r = j.showOpenDialog(null);
	           // If the user selects a file
	           if (r == JFileChooser.APPROVE_OPTION) {
	               // Set the label to the path of the selected directory
	               File fi = new File(j.getSelectedFile().getAbsolutePath());
	               textField_ECL.setText(fi.toString());
	               return fi;
	              
	           }
	           return null;
	        
	   }
	   
	   public void Btn_CanselAction(ActionEvent e)
	   {

		   setVisible(false); //you can't see me!
		   dispose(); //Destroy the JFrame object
	        
	   }
	   
	   public static void computeDiff(Date S1, Date E2 , IEmlContext context) {

		    long diffInMillies = (E2.getTime() ) - (S1.getTime() );

		    //create the list
		    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
		    Collections.reverse(units);

		    //create the result map of TimeUnit and difference
		    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
		    long milliesRest = diffInMillies;

		    for ( TimeUnit unit : units ) {

		        //calculate difference in millisecond 
		        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
		        long diffInMilliesForUnit = unit.toMillis(diff);
		        milliesRest = milliesRest - diffInMilliesForUnit;

		        //put the result in the map
		        result.put(unit,diff);
		    }
		    
		/////////// match time
					context.setMergerReport("-------------------------------------------------");
					context.setMergerReport("-------------------------------------------------");
					context.setMergerReport( " Matching with base version time is:" + result.get(TimeUnit.DAYS) + " days, " +
		    	result.get(TimeUnit.HOURS) + " hours, " +
		    		result.get(TimeUnit.MINUTES) + " minutes, " +
						result.get(TimeUnit.SECONDS) + " seconds, " +
							result.get(TimeUnit.MILLISECONDS) + " milliseconds,");
					context.setMergerReport("-------------------------------------------------");
					context.setMergerReport("-------------------------------------------------");
		    
		}
	   
	   
}
