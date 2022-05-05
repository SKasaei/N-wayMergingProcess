package executor;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;

import dom.MergeRule;
import dom.importModel;
import enml.INmlModule;
import nmlMatchTrace.nmlMatchTrace;

import javax.swing.border.MatteBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTextArea;
import java.awt.TextArea;

public class DesignRational extends JFrame {

	private JPanel contentPane;
	private JTable table;
	
	List<String> Models = new ArrayList<>();
	private JTextField textField_Type;
	private JTextField textField_Rule;
	 TextArea textArea = new TextArea();
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DesignRational frame = new DesignRational();
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
	public DesignRational(INmlModule emlModule ) {
		setResizable(false);
		
		/////////////////////////////////////
		DefineModelsName(emlModule);
		/////////////////////////////////////
		Object[] columnNames = createColumnTable();
	    Object[][] data = createDataTable(emlModule , columnNames);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Design Rational Window");
		 Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\Eclipse.png");    
	 	 setIconImage(icon);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)), "Table Guideline:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(0, 0, 854, 68);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("(X): Corresponding Element Does Not Exist (Not Found)");
		lblNewLabel.setBounds(501, 39, 329, 16);
		panel_1.add(lblNewLabel);
		
		JLabel lblxCorrespondingElement = new JLabel("(\u2713): Same or Equivalent Element Exist");
		lblxCorrespondingElement.setBounds(48, 39, 329, 16);
		panel_1.add(lblxCorrespondingElement);
		
		JLabel lblRulesCanBe = new JLabel("Rules can be Octopus, Ours, or Transfer!");
		lblRulesCanBe.setBounds(26, 13, 243, 26);
		panel_1.add(lblRulesCanBe);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(0, 589, 854, 56);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btn_Back = new JButton("Close");
		btn_Back.setBackground(SystemColor.controlHighlight);
		btn_Back.setBounds(716, 13, 126, 26);
		panel_3.add(btn_Back);
		
		JButton btn_SaveReport = new JButton("Save");
		btn_SaveReport.setBackground(SystemColor.controlHighlight);
		btn_SaveReport.setBounds(578, 13, 126, 26);
		panel_3.add(btn_SaveReport);
		
		        
		 DefaultTableModel model = new DefaultTableModel(data, columnNames);
		    
		 table = new JTable(model) {

	            private static final long serialVersionUID = 1L;

	            /*@Override
	            public Class getColumnClass(int column) {
	            return getValueAt(0, column).getClass();
	            }*/
	            @Override
	            public Class getColumnClass(int column) {
	                switch (column) {
	                    case 0:
	                        return Integer.class;
	                    case 1:
	                        return String.class;
	                    case 2:
	                        return String.class;
	                    case 3:
	                        return String.class;
	                    default:
	                        return String.class;
	                }
	            }
	        };
		 table.setEnabled(false);
		
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 107, 854, 263);
        getContentPane().add(scrollPane);
        
        JLabel lblFilterByType = new JLabel("Filter by Type:");
        lblFilterByType.setBounds(10, 78, 89, 16);
        contentPane.add(lblFilterByType);
        
        JLabel lblFilterByRule = new JLabel("Filter by Rule:");
        lblFilterByRule.setBounds(371, 78, 89, 16);
        contentPane.add(lblFilterByRule);
        
        textField_Type = new JTextField();
        textField_Type.setBounds(97, 75, 240, 22);
        contentPane.add(textField_Type);
        textField_Type.setColumns(10);
        
        JButton btnFilter = new JButton("Filter");
        btnFilter.setBackground(SystemColor.controlHighlight);
        btnFilter.setBounds(724, 75, 111, 22);
        contentPane.add(btnFilter);
        
        textField_Rule = new JTextField();
        textField_Rule.setColumns(10);
        textField_Rule.setBounds(457, 75, 240, 22);
        contentPane.add(textField_Rule);
		
	  
	    
        // Align table
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        for (int i = 0 ; i <Models.size() ; i++) {
        table.getColumnModel().getColumn(i + 4).setCellRenderer(rightRenderer);
        }
        
        /////filter
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(180, 180, 180)), "Merger Report", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(10, 383, 832, 193);
        contentPane.add(panel);
        panel.setLayout(null);
        
       
        textArea.setBackground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setBounds(10, 26, 812, 157);
        panel.add(textArea);
        
        createTextArea_Report(emlModule,textArea);
        
        btnFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String text_Rule = textField_Rule.getText();
               String text_Type = textField_Type.getText();
               if(text_Rule.length() == 0 && text_Type.length() == 0) {
                  sorter.setRowFilter(null);
               } else {
                  try {
                	  if(text_Rule.length() == 0) { sorter.setRowFilter(RowFilter.regexFilter(text_Type , 2));}
                	  if(text_Type.length() == 0) {  sorter.setRowFilter(RowFilter.regexFilter(text_Rule , 3));}
                	  if (text_Type.length() != 0 && text_Rule.length() != 0) { 
                		  List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
                		  filters.add(RowFilter.regexFilter(text_Rule , 3));
                		  filters.add(RowFilter.regexFilter(text_Type , 2));
                		  RowFilter rf = RowFilter.andFilter(filters);
                		  sorter.setRowFilter(rf);
                	  }
                  } catch (Exception v) {
                	  System.out.println(v);
      	        }
                }
            }
         });
        
        btn_Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			////// CC window
				ConsistencyChecking ConsistencyCheckingWindow = new ConsistencyChecking(emlModule);
				ConsistencyCheckingWindow.setVisible(true);
	    		setVisible(false);
			}

		});
        
        
        btn_SaveReport.addActionListener(new ActionListener() {
     			public void actionPerformed(ActionEvent e) {
     			////// Save 
     				btnSaveReportActionPerformed(e);
     			}

     		});
        
	}
	
	///////////////////////////////////////////////////////////////////
	
	 public void DefineModelsName(INmlModule emlModule)
	   {
		if(emlModule.getContext().getBModel() == null) {
		  	int Count = (emlModule.getContext().getModelRepository().getModels().size())-1;
		  	for (int i = 0 ; i < Count ; i ++) {
		  		Models.add(emlModule.getContext().getModelRepository().getModels().get(i).getName());
		  		}
		}else {
	  	Models.add(emlModule.getContext().getBModel().getName());
	  	int Count = (emlModule.getContext().getModelRepository().getModels().size())-2;
	  	for (int i = 0 ; i < Count ; i ++) {
	  		Models.add(emlModule.getContext().getModelRepository().getModels().get(i).getName());
	  		}
		}
	   }
	
	 public Object[] createColumnTable()
	   {
  	
	  	Object[] multiples = new Object[ (Models.size()) +4];
	  	
	  //columnNames = {"(#)", "Element", "Type", V0, V1, "..."};
		for (int i = 0; i < (Models.size()) +4; i++) {
			if(i==0) {
				
				multiples[i] = "(#)";
				
				}else if(i==1) {
					
				multiples[i] = "Element";
				
				}else if(i==2) {
					
				multiples[i] = "Type";
				
				}else if(i==3) {
					
					multiples[i] = "Rule";
					
					}
				else {
					multiples[i] = Models.get(i - 4);
				}
		}
	  	
	       return  multiples;
	   }
	 
	    public Object[][] createDataTable(INmlModule emlModule , Object[] columnNames)
		   {
	    	List<nmlMatchTrace> matches = new ArrayList<>();
	    	matches.addAll(emlModule.getContext().getOctopusMatchTrace());
	    	matches.addAll(emlModule.getContext().getOctopusMatchTraceWB());
	    	matches.addAll(emlModule.getContext().getOursMatchTrace());
	    	matches.addAll(emlModule.getContext().getOursMatchTraceWB());
	    	
	    	int count_octopus = emlModule.getContext().getOctopusMatchTrace().size();
	    	int count_octopusWB = emlModule.getContext().getOctopusMatchTraceWB().size();
	    	int count_ours = emlModule.getContext().getOursMatchTrace().size();
	    	int count_oursWB = emlModule.getContext().getOursMatchTraceWB().size();
	    	
	    	int COUNT_RR = count_octopus + count_octopusWB + count_ours + count_oursWB;
	    	int COUNT_C = (Models.size()) +4;
	    	
	    	List<Object> transferObjects = emlModule.getContext().getTransferObjects();
	    	List<String> transferObjectsParameter = emlModule.getContext().getTransferObjectsParameter();
	    	int COUNT_transfer = -1 ; 
	    	int COUNT_R = COUNT_RR + transferObjects.size();
	    	
	    	Object[][] multiples = new Object [COUNT_R] [COUNT_C];
	    	matches.get(0).getLeft();
	    	try {
	    	for(int i = 0 ; i < COUNT_R ; i++) {
	    		
	    		if (i >= COUNT_RR) {COUNT_transfer++;}
	    	
		    	for (int j = 0; j < COUNT_C; j++) {
		    		
		    		////////////////////////// Octopus with base
		    		if(i < count_octopus) {
		    			
						if(j==0) {
							multiples[i][j] = i; 
							}else
						if(j==1) {
							multiples[i][j] = findObjectName(matches.get(i).getLeft() , "name" , emlModule.getContext()); 
							}else
						if(j==2) {
							
							Parameter LeftParameterRule = matches.get(i).getRule().getLeftParameterRule();
							String ModelType_matchRule = LeftParameterRule.getTypeName();
							char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
							ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
							String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
							
							multiples[i][j] = typeMatchRule; 
							}else
						if(j==3) {
							multiples[i][j] = "Octopus"; 
							}else{
								multiples[i][j] = "(\u2713)"; 
							}
							
						} 
		    		
		    		//////////////////////////Octopus withOut base
		    		if(i >= count_octopus && i < count_octopusWB+count_octopus) {
		    			
						if(j==0) {
							multiples[i][j] = i; 
							}else
						if(j==1) {
							multiples[i][j] = findObjectName(matches.get(i).getLeft() , "name" , emlModule.getContext()); 
							}else
						if(j==2) {
							
							Parameter LeftParameterRule = matches.get(i).getRule().getLeftParameterRule();
							String ModelType_matchRule = LeftParameterRule.getTypeName();
							char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
							ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
							String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
							
							multiples[i][j] = typeMatchRule; 
							}else
						if(j==3) {
							multiples[i][j] = "Octopus"; 
							}else
							{
								if(emlModule.getContext().getBModel() == null) {
									multiples[i][j] = "(\u2713)";
								}else {
								if (columnNames[j].equals(Models.get(0))) {
									multiples[i][j] = "(X)"; 
								}else {
									multiples[i][j] = "(\u2713)";
									}
								}
							}
						} 
		    		
		    		//////////////////////////Ours with base
	    		if(i >= count_octopusWB+count_octopus && i < count_octopusWB+count_octopus+count_ours) {
	    			
					if(j==0) {
						multiples[i][j] = i; 
						}else
					if(j==1) {
						multiples[i][j] = findObjectName(matches.get(i).getLeft() , "name" , emlModule.getContext()); 
						}else
					if(j==2) {
						
						Parameter LeftParameterRule = matches.get(i).getRule().getLeftParameterRule();
						String ModelType_matchRule = LeftParameterRule.getTypeName();
						char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
						ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
						String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
						
						multiples[i][j] = typeMatchRule; 
						}else
					if(j==3) {
						multiples[i][j] = "Ours"; 
						}else
						{
							for (int p = 0 ; p < matches.get(i).getModelsInMatch().size() ; p++) {
								if(j==4) {
									multiples[i][j] = "(\u2713)";
								}else
								if (matches.get(i).getModelsInMatch().get(p).equals(columnNames[j])) {
									multiples[i][j] = "(\u2713)";
									break;
								}else {
									
									multiples[i][j] = "(X)"; 
								}
							}
						}
					} 
	    		
	    		//////////////////////////Ours withOut base
    		if(i >= count_octopusWB+count_octopus+count_ours && i < count_octopusWB+count_octopus+count_ours+count_oursWB) {
    			
				if(j==0) {
					multiples[i][j] = i; 
					}else
				if(j==1) {
					multiples[i][j] = findObjectName(matches.get(i).getLeft() , "name" , emlModule.getContext()); 
					}else
				if(j==2) {
					
					Parameter LeftParameterRule = matches.get(i).getRule().getLeftParameterRule();
					String ModelType_matchRule = LeftParameterRule.getTypeName();
					char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
					ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
					String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
					
					multiples[i][j] = typeMatchRule; 
					}else
				if(j==3) {
					multiples[i][j] = "Ours"; 
					}else
					{
						if (columnNames[j].equals(Models.get(0))) {
							multiples[i][j] = "(X)"; 
						}else {
								for (int p = 0 ; p < matches.get(i).getModelsInMatch().size() ; p++) {
								
								if (matches.get(i).getModelsInMatchWB().get(p).equals(columnNames[j])) {
									multiples[i][j] = "(\u2713)";
									break;
								}else {
									
									multiples[i][j] = "(X)"; 
								}
							}
						}
					}
				} 
    		
    		/////////////////// Transfer
    		if(i < COUNT_R && i >= count_octopusWB+count_octopus+count_ours+count_oursWB) {
    			
				if(j==0) {
					multiples[i][j] = i; 
					}else
				if(j==1) {
					multiples[i][j] = findObjectName(transferObjects.get(COUNT_transfer) , "name" , emlModule.getContext()); 
					}else
				if(j==2) {
					
					String ModelType_matchRule = transferObjectsParameter.get(COUNT_transfer);
					char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
					ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
					String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
					
					multiples[i][j] = typeMatchRule; 
					}else
				if(j==3) {
					multiples[i][j] = "Transfer"; 
					}else{
						
						String ModelType_matchRule = transferObjectsParameter.get(COUNT_transfer);
						char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ( ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") )  ];
						ModelType_matchRule.getChars(0 , ModelType_matchRule.indexOf("!") , ModelNamearr_matchRule, 0 );
						String NameOfModelByParameter = String.copyValueOf(ModelNamearr_matchRule);
						
						if(NameOfModelByParameter.equals(columnNames[j])) {
							multiples[i][j] = "(\u2713)"; 
							
						}else {
							multiples[i][j] = "(X)"; 
						}
					}
    			
    					}
    		
		    		
		    		}
		    	
	    		}
		   } catch (EolRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		        return multiples;
		        
		   }
	    
	    public  String findObjectName(Object source, String propertyName, IEolContext context) throws EolRuntimeException {
	   try {
	    	IPropertyGetter getter = context.getIntrospectionManager().getPropertyGetterFor(source, propertyName, context);

	    	Object sourceContainer = getter.invoke(source, propertyName, context);
	    	if (sourceContainer == null) {
	    		return "unset";
	    	}else {
	    	return sourceContainer.toString();
	    	}
	   }catch (EolRuntimeException e) {
		   return "-";
		}
	    	
	    }
	    
	
	    
	    public void createTextArea_Report( INmlModule emlModule , TextArea textArea_Report )
		   {
	    	
	    	int x = emlModule.getContext().getMergerReport().size();
	    	for(int i = 0 ; i < x ; i++) {
	    		textArea_Report.getText();
	    		textArea_Report.setText(textArea_Report.getText()  + emlModule.getContext().getMergerReport().get(i) + "\n");
	    		
	    	}
		        
		   }
	    
	    private void btnSaveReportActionPerformed(java.awt.event.ActionEvent evt ) {//GEN-FIRST:event_btnListJFramesActionPerformed
	        
	           // Create an object of JFileChooser class
	           JFileChooser j = new JFileChooser("f:");
	           // Invoke the showsSaveDialog function to show the save dialog
	           int r = j.showSaveDialog(null);
	           if (r == JFileChooser.APPROVE_OPTION) {
	               // Set the label to the path of the selected directory
	        	   File fi = new File(j.getSelectedFile().getAbsolutePath() + ".txt");
	        	   File fExel = new File(j.getSelectedFile().getAbsolutePath() + ".xlsx");
	               try {
	                   // Create a file writer
	                   FileWriter wr = new FileWriter(fi, false);
	                   // Create buffered writer to write
	                   BufferedWriter w = new BufferedWriter(wr);
	                   // Write
	                   Path pathE = fExel.toPath();
	                   writeToExcell(table , pathE);
	                   
	                   w.write(textArea.getText());
	                   w.flush();
	                   w.close();
	               }
	               catch (Exception e) {
	            	   JOptionPane.showMessageDialog(this, e.getMessage());
	               }
	           }     
	    }
	    
	    private static void writeToExcell(JTable table, Path path) throws FileNotFoundException, IOException {
	        new WorkbookFactory();
	        Workbook wb = new XSSFWorkbook(); //Excell workbook
	        Sheet sheet = wb.createSheet(); //WorkSheet
	        Row row = sheet.createRow(2); //Row created at line 3
	        TableModel model = table.getModel(); //Table model


	        Row headerRow = sheet.createRow(0); //Create row at line 0
	        for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
	            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
	        }

	        for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
	            for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
	                row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
	            }

	            //Set the row to the next one in the sequence 
	            row = sheet.createRow((rows + 3)); 
	        }
	        wb.write(new FileOutputStream(path.toString()));//Save the file     
	    }
}
