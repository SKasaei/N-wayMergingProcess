package executor;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.IEclModule;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.models.IModel;

import enml.NmlModule;
import enml.INmlModule;
import execute.context.INmlContext;

import java.awt.Choice;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.SystemColor;


public class matchWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JPanel contentPane;
    private int ECL_COUNTER = 0;
    private int ECL_COUNTER_TEMP = 0;
    private JTextField textField_filter;
  
    
    public matchWindow(INmlModule emlModule , INmlModule emlModule_Original) {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
        Object[] columnNames = createColumnTable(emlModule , emlModule.getContext().getModelRepository().getModels().get(0).getName());
        Object[][] data = createDataTable(emlModule , 0);
        
        // Align table
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        getContentPane().setLayout(null);
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
                        return Boolean.class;
                }
            }
        };
        table.setFillsViewportHeight(true);
        
        
        setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Match Window");
		contentPane.setLayout(null);
		 Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Admin\\git\\org.eclipse.epsilon.ep\\org.eclipse.epsilon.enml.editor\\icon\\Eclipse.png");    
	 	 setIconImage(icon);
		
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 145, 694, 359);
        getContentPane().add(scrollPane);
        // Align table
        table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        panel_1.setBorder(new TitledBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)), "Please Review the Created Match-List and Revise it if Required ...", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(0, 0, 694, 96);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);
        
        Choice choice_SelectModel = new Choice();
        choice_SelectModel.setBounds(498, 61, 116, 22);
        panel_1.add(choice_SelectModel);
        
        JLabel lblGuideline = new JLabel("Guideline:  ");
        lblGuideline.setBounds(22, 26, 65, 22);
        panel_1.add(lblGuideline);
        
        JLabel lblTableShowsComparison = new JLabel("Table Shows Comparison Result between Base Model and Selected Input Model:");
        lblTableShowsComparison.setBounds(22, 61, 478, 22);
        panel_1.add(lblTableShowsComparison);
        
        JButton btnDo = new JButton("Go");
        btnDo.setForeground(new Color(240, 255, 240));
        btnDo.setBackground(new Color(0, 0, 139));
        btnDo.setBounds(620, 61, 62, 22);
        panel_1.add(btnDo);
        
        Checkbox checkbox_Match = new Checkbox("Detacted as Same Elements");
        checkbox_Match.setForeground(Color.BLACK);
        checkbox_Match.setBackground(Color.WHITE);
        checkbox_Match.setState(true);
        checkbox_Match.setBounds(108, 24, 218, 24);
        panel_1.add(checkbox_Match);
        
        Checkbox checkbox_Match_1 = new Checkbox("Detacted as Dissimilar Elements");
        checkbox_Match_1.setBackground(Color.WHITE);
        checkbox_Match_1.setForeground(Color.BLACK);
        checkbox_Match_1.setBounds(332, 26, 209, 24);
        panel_1.add(checkbox_Match_1);
        
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_2.setBounds(0, 517, 694, 48);
        getContentPane().add(panel_2);
        panel_2.setLayout(null);
        
        JButton btn_Cansel = new JButton("Cansel");
        btn_Cansel.setBackground(SystemColor.controlHighlight);
        btn_Cansel.setBounds(562, 13, 120, 25);
        panel_2.add(btn_Cansel);
        
        JButton btn_Finish = new JButton("Finish");
        btn_Finish.setBackground(SystemColor.controlHighlight);
        btn_Finish.setEnabled(false);
        btn_Finish.setBounds(433, 13, 120, 25);
        panel_2.add(btn_Finish);
        
        JButton btn_Next = new JButton("Next >");
        btn_Next.setBackground(SystemColor.controlHighlight);
        btn_Next.setBounds(301, 13, 120, 25);
        panel_2.add(btn_Next);
        
        JButton btn_Back = new JButton("< Back");
        btn_Back.setBackground(SystemColor.controlHighlight);
        btn_Back.setBounds(169, 13, 120, 25);
        panel_2.add(btn_Back);
        
        Label label_filter = new Label("Filter by Type");
        label_filter.setBounds(10, 107, 83, 24);
        contentPane.add(label_filter);
        
        textField_filter = new JTextField();
        textField_filter.setBounds(99, 109, 475, 22);
        contentPane.add(textField_filter);
        textField_filter.setColumns(10);
        
        JButton btn_filter = new JButton("Filter");
        btn_filter.setBounds(586, 107, 103, 25);
        contentPane.add(btn_filter);
        
        
        ////////////////////////////////////
        ///////action//////////////////
        
        btn_Cansel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Btn_CanselAction(evt);
            }
        });
        
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
         ////// P window
            			properties propertiesWinsow = new properties(emlModule_Original);
            			propertiesWinsow.setVisible(true);
         	    		setVisible(false);
            }
        });
        
        btn_Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				int result_Next = JOptionPane.showConfirmDialog(null, "Do you confirm the match lists?");
				if (result_Next  == JOptionPane.YES_OPTION) {
				     // Yes button was pressed
				for (int i = 0; i < table.getRowCount(); i++) {
					Boolean chked = Boolean.valueOf(table.getValueAt(i, 4)
							.toString());
					String dataCol0 = table.getValueAt(i, 0).toString();
					if (chked) {
						emlModule.getContext().setEclModule_WithBase(setUserMatch_True(emlModule , dataCol0 , ECL_COUNTER));
					}else {
						emlModule.getContext().setEclModule_WithBase(setUserMatch_False(emlModule , dataCol0 , ECL_COUNTER));
						}
					}
				
				//EX
				
				try {
					emlModule.getContext().nmlMatchTrace();
					emlModule.getContext().getModelRepository().addModel(emlModule.getContext().getBModel());
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
				
				} else if (result_Next  == JOptionPane.NO_OPTION) {
				     // No button was pressed
				}
			}

		});
        
        ///// choice
        choice_SelectModel.add("");
        for (int ichoice=0 ; ichoice < (emlModule.getContext().getModelRepository().getModels().size()) ; ichoice++)
        {
        	choice_SelectModel.add(emlModule.getContext().getModelRepository().getModels().get(ichoice).getName());
        }
        
        btnDo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ECL_COUNTER_TEMP = ECL_COUNTER;
				DefaultTableModel model_2 = btn_DoAction( e ,  emlModule ,  choice_SelectModel);
					
			
				for (int i = 0; i < table.getRowCount(); i++) {
					Boolean chked = Boolean.valueOf(table.getValueAt(i, 4)
							.toString());
					String dataCol0 = table.getValueAt(i, 0).toString();
					if (chked) {
						
						emlModule.getContext().setEclModule_WithBase(setUserMatch_True(emlModule , dataCol0 , ECL_COUNTER_TEMP));
						
					}else {
						emlModule.getContext().setEclModule_WithBase(setUserMatch_False(emlModule , dataCol0 , ECL_COUNTER_TEMP));
						}
					}
				
			
				 table = new JTable(model_2){

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
			                        return Boolean.class;
			                }
			            }
			        };
			//	table.setModel(model_2);
		       // add(new JScrollPane(table));
				scrollPane.setViewportView(table);
		     
				// table.setModel(model_2);
				 // Align table
		        table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
			}

		});
        
      /////filter
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        btn_filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String text = textField_filter.getText();
               if(text.length() == 0) {
                  sorter.setRowFilter(null);
               } else {
                  try {
                     sorter.setRowFilter(RowFilter.regexFilter(text , 1));
                  } catch (Exception v) {
                	  System.out.println(v);
      	        }
                }
            }
         });
        
       
    
    }

    public Object[] createColumnTable(INmlModule emlModule , String Version)
	   {
    	
   
    	String newVersion = Version;

    	String Base = emlModule.getContext().getBModel().getName();
    	
    	Object[] columnNames = {"(#)", "Type", Base, newVersion, "Match"};
   
	       return  columnNames;
	   }
    
    public Object[][] createDataTable(INmlModule emlModule , int Counter)
	   {
    	IEclModule ECLM = new EclModule();
		ECLM = emlModule.getContext().getEclModule_WithBase().get(Counter);
		
		MatchTrace aa = ECLM.getContext().getMatchTrace();
			
		int aaSize = aa.size();
		Object[][] multiples = new Object[aaSize][5];
		
		int i = 0;
		for (Match match : aa.getMatches())
		 {
			
			try {
				String EBaseName = findObjectName(match.getLeft() , "name" , ECLM.getContext()); //emlModule.getContext()
				String EnewVersionName = findObjectName(match.getRight() , "name" , ECLM.getContext());
				
				Parameter LeftParameterRule = match.getRule().getLeftParameterRule();
				String ModelType_matchRule = LeftParameterRule.getTypeName();
				char[] ModelNamearr_matchRule = new char[ ModelType_matchRule.toCharArray().length - ModelType_matchRule.indexOf("!") - 1 ];
				ModelType_matchRule.getChars(ModelType_matchRule.indexOf("!") + 1 , ModelType_matchRule.toCharArray().length , ModelNamearr_matchRule, 0);
				String typeMatchRule = String.copyValueOf(ModelNamearr_matchRule);
			       // Create {Number , "type", EBaseName, EnewVersionName, Match}
				
					for (int j = 0; j < 5; j++) {
						if(j==0) {
							multiples[i][j] = i; 
							}
						if(j==1) {
							multiples[i][j] = typeMatchRule; 
							}
						if(j==2) {
							multiples[i][j] = EBaseName; 
							}
						if(j==3) {
							multiples[i][j] = EnewVersionName; 
							}
						if(j==4) {
							multiples[i][j] = match.isMatching(); 
							}
						} 
					

				
			} catch (EolRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			i++;
		}
    	//////////////////

		return multiples;
	            
       /* Object[][] data = {
            {"Buy", "IBM", new Integer(1000), new Double(80.50), false},
            {"Sell", "MicroSoft", new Integer(2000), new Double(6.25), true},
            {"Sell", "Apple", new Integer(3000), new Double(7.35), true},
            {"Buy", "Nortel", new Integer(4000), new Double(20.00), false}
        };*/
        
     
	        
	   }
    
    public  String findObjectName(Object source, String propertyName, IEolContext context) throws EolRuntimeException {
    	/*NameExpression y = new NameExpression();
    	y.setName(propertyName);
    	PropertyCallExpression x = new PropertyCallExpression();
    	Object n = x.execute(source, y, context);*/
    	IPropertyGetter getter = context.getIntrospectionManager().getPropertyGetterFor(source, propertyName, context);

    	Object sourceContainer = getter.invoke(source, propertyName, context);
    	return sourceContainer.toString();
    	
    }
    
    public  List<IEclModule> setUserMatch_True(INmlModule emlModule , String dataCol0 , int C) {
    	
    	int i = 0;
		for (Match match : emlModule.getContext().getEclModule_WithBase().get(C).getContext().getMatchTrace().getMatches())
		 {
			if (i == Integer.parseInt(dataCol0)) {
				match.setMatching(true);
				break;
			}
			i++;
		 }
		return  emlModule.getContext().getEclModule_WithBase();
    }
    
 public  List<IEclModule> setUserMatch_False(INmlModule emlModule , String dataCol0 , int C) {
    	
    	int i = 0;
		for (Match match : emlModule.getContext().getEclModule_WithBase().get(C).getContext().getMatchTrace().getMatches())
		 {
			if (i == Integer.parseInt(dataCol0)) {
				match.setMatching(false);
				break;
			}
			i++;
		 }
		return  emlModule.getContext().getEclModule_WithBase();
    }
 
 public  DefaultTableModel btn_DoAction(java.awt.event.ActionEvent evt , INmlModule emlModule , Choice choice_SelectModel) {
	 
	
      
      String choiceSelectedItem = choice_SelectModel.getSelectedItem();
      IModel Model_In_MODULE = null;
      for(int iECLM = 0 ; iECLM<emlModule.getContext().getEclModule_WithBase().size() ; iECLM++) {
		try {
			Model_In_MODULE = emlModule.getContext().getEclModule_WithBase().get(iECLM).getContext().getModelRepository().getModelByName(choiceSelectedItem);
			if (Model_In_MODULE != null) {
				Object[] columnNames = createColumnTable(emlModule , choiceSelectedItem);
				Object[][]   data = createDataTable(emlModule , iECLM);
			        
			    DefaultTableModel model = new DefaultTableModel(data, columnNames);
			    ECL_COUNTER = iECLM;
			    return model;
			   
			}
		
		} catch (EolRuntimeException e) {
			
			}
      }
 	return null;
 	
 }
 
 public void Btn_CanselAction(ActionEvent e)
 {

	   setVisible(false); //you can't see me!
	   dispose(); //Destroy the JFrame object
      
 }

 
}