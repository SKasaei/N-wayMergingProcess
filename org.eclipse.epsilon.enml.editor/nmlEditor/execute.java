package nmlEditor;

import java.awt.Choice;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import enml.IEmlModule;
import loadingModel.loadingModel;


public class execute extends javax.swing.JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6880246776182893L;
	/* Creates new form ParentJFrame */
    public execute(IEmlModule emlModule) {
        initComponents(emlModule);
       
    }

    @SuppressWarnings("unchecked")
    // //GEN-BEGIN:initComponents
    private void initComponents(IEmlModule emlModule) {
        lblInfo = new javax.swing.JLabel();
        lblBase = new javax.swing.JLabel();
        lblTarget = new javax.swing.JLabel();
        btnOpenChildJFrame = new javax.swing.JButton();
        btnListJFrames = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        textAreaMeta = new javax.swing.JTextArea();
        scrollPane2 = new javax.swing.JScrollPane();
        lblMetaModel = new javax.swing.JLabel();
        btnSetMeta = new javax.swing.JButton();
        

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("nml Executor");
        
        lblBase.setText("Select Base-Model");
        lblTarget.setText("Select Target-Model");
        
        Choice choiceBase = new Choice();
        Choice choiceTarget = new Choice();
        choiceBase.add("");
		choiceTarget.add("");

        lblInfo.setText("List of Imported Models");
        btnOpenChildJFrame.setText("Run");
        btnOpenChildJFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunActionPerformed(evt , emlModule , choiceBase , choiceTarget);
            }
        });
        
        btnListJFrames.setText("Load Imported Models");
        btnListJFrames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListModelsActionPerformed(evt , emlModule , choiceBase , choiceTarget);
            }
        });
        
        lblMetaModel.setText("Inserted Meta-Model");
        lblMetaModel.setEnabled(false);
        btnSetMeta.setText("Set Meta-Model");
        btnSetMeta.setEnabled(false);
        btnSetMeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetMetaActionPerformed(evt , emlModule , choiceBase , choiceTarget);
            }
        });

        textArea.setColumns(20);
        textArea.setRows(9);
        scrollPane.setViewportView(textArea);
        
        textAreaMeta.setColumns(20);
        textAreaMeta.setRows(1);
        scrollPane2.setViewportView(textAreaMeta);
        scrollPane2.setEnabled(false);
        textAreaMeta.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                		.addGroup(layout.createSequentialGroup()
                				.addComponent(lblBase)
                				.addGap(8, 8, 8)
                        		.addComponent(choiceBase)
                        		.addGap(12, 12, 12)
                        		.addComponent(lblTarget)
                        		.addGap(8, 8, 8)
                        		.addComponent(choiceTarget))
                        .addGap(8, 8, 8)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)

                    .addComponent(lblInfo)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOpenChildJFrame)
                        .addGap(18, 18, 18)
                        .addComponent(btnListJFrames))
                    .addComponent(lblMetaModel)
                    .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(btnSetMeta))
                		)
                .addContainerGap())
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfo)
                
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                		.addComponent(lblBase)
                		.addComponent(choiceBase)
                		.addComponent(lblTarget)
                		.addComponent(choiceTarget))
                .addGap(8, 8, 8)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOpenChildJFrame)
                    .addComponent(btnListJFrames))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               
                .addGap(8, 8, 8)
                .addComponent(lblMetaModel)
                .addGap(8, 8, 8)
                .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)   
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSetMeta)) 
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8)

            		)
        );
   	  Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\nml.png");    
   	  setIconImage(icon);
        pack();
    }// //GEN-END:initComponents

    /**
     * Open Child JFrame button click event
     */
    private void btnRunActionPerformed(java.awt.event.ActionEvent evt , IEmlModule emlModule , Choice choiceBase , Choice choiceTarget) {//GEN-FIRST:event_btnOpenChildJFrameActionPerformed

    	try {
    	if (choiceBase.getSelectedItem()!="" && choiceTarget.getSelectedItem()!="" && choiceTarget.getSelectedItem()!=choiceBase.getSelectedItem()) {

    		emlModule.getContext().addBModel(emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()));
    		//emlModule.getContext().getModelRepository().removeModel(emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()));
    		emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()).getAliases().clear();
    		emlModule.getContext().getModelRepository().getModelByName(choiceBase.getSelectedItem()).getAliases().add("BaseVersion");

    		emlModule.getContext().addTModel(emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()));
    		//emlModule.getContext().getModelRepository().removeModel(emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()));
    		emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()).getAliases().clear();
    		emlModule.getContext().getModelRepository().getModelByName(choiceTarget.getSelectedItem()).getAliases().add("TargetVersion");

    		
    		JOptionPane.showMessageDialog(this, " OK ");
    	
    	}else {
    		JOptionPane.showMessageDialog(this, " Select Base and Target model ");
    	}
    	}
    	catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * List JFrame button click event
     */
    private void btnListModelsActionPerformed(java.awt.event.ActionEvent evt , IEmlModule emlModule , Choice choiceBase , Choice choiceTarget) {//GEN-FIRST:event_btnListJFramesActionPerformed
        
        try {
		loadingModel loadImortedModel = new loadingModel();

        
        ArrayList<String> importModelList = new ArrayList<String>();
		importModelList = emlModule.getImportModel();
		
    	ArrayList<String> importModelNames = new ArrayList<String>();
    	importModelNames = emlModule.getImportModelName();
    	
    	ArrayList<String> importModelFileNames = new ArrayList<String>();
    	importModelFileNames = emlModule.getImportModelFileName();
    	
		int importModelListSize = importModelList.size();
		int emfcount = 0 ;
		for (int i = 0 ; i < importModelListSize ; i++) {
			String modelFileName = importModelFileNames.get(i);
			
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
			String previousText = textArea.getText();
			textArea.setText(previousText + importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
			choiceBase.add(importModelNames.get(i));
			choiceTarget.add(importModelNames.get(i));
			}
				break;
			// UML
			case 2:
			{
				emlModule.getContext().getModelRepository().addModels(loadImortedModel.loadUMLModel(importModelNames.get(i) , importModelList.get(i)));
				String previousText = textArea.getText();
				textArea.setText(previousText + importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
				choiceBase.add(importModelNames.get(i));
				choiceTarget.add(importModelNames.get(i));
			}
				break;
			// EMF
			case 3:
			{
				
				if ( emfcount == 0 ) {
				JOptionPane.showMessageDialog(this, " Set Your Meta-Model ");
		        btnSetMeta.setEnabled(true);
		        lblMetaModel.setEnabled(true);
		        scrollPane2.setEnabled(true);
		        textAreaMeta.setEnabled(true);
		        emfcount++;
				}


			}
				break;
			 default:
				 break;
			}
			
		}
		btnListJFrames.setEnabled(false);
    }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void btnSetMetaActionPerformed(java.awt.event.ActionEvent evt , IEmlModule emlModule , Choice choiceBase , Choice choiceTarget) {//GEN-FIRST:event_btnListJFramesActionPerformed
        
        try {
        	
        	loadingModel loadImortedModel = new loadingModel();

            ArrayList<String> importModelList = new ArrayList<String>();
    		importModelList = emlModule.getImportModel();
    		
        	ArrayList<String> importModelNames = new ArrayList<String>();
        	importModelNames = emlModule.getImportModelName();
        	
        	ArrayList<String> importModelFileNames = new ArrayList<String>();
        	importModelFileNames = emlModule.getImportModelFileName();
        	
        	//===========+++++++++++++++++++++++++++++++++++++++++++++++++++++++
       	 // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");
            // Invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);
            // If the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                    // Set the text
                    textAreaMeta.setText(fi.getAbsolutePath());
            
        	//===========++++++++++++++++++++++++++++++++++++++++++++++
        	
    		int importModelListSize = importModelList.size();
			String MetaModelText = textAreaMeta.getText();

    		for (int i = 0 ; i < importModelListSize ; i++) {
    		String modelFileName = importModelFileNames.get(i);
        	emlModule.getContext().getModelRepository().addModels(loadImortedModel.loadEMFModel(importModelList.get(i), MetaModelText ,importModelNames.get(i)));
			String previousText = textArea.getText();
			textArea.setText(previousText + importModelNames.get(i) + "\n" + modelFileName + "\n" + importModelList.get(i) + "\n");
			textAreaMeta.setText(MetaModelText);
			choiceBase.add(importModelNames.get(i));
			choiceTarget.add(importModelNames.get(i));
    		}
        }
    		else
                JOptionPane.showMessageDialog(this, "the user cancelled the operation");
        }      
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private javax.swing.JButton btnListJFrames;
    private javax.swing.JButton btnOpenChildJFrame;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblBase;
    private javax.swing.JLabel lblTarget;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextArea textAreaMeta;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JButton btnSetMeta;
    private javax.swing.JLabel lblMetaModel;




}