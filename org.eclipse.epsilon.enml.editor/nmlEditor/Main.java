/**
 * 
 */
/**
 * @author Admin
 *
 */
package nmlEditor;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) throws Exception {
				
	
	      // Start all Swing applications on the EDT.
	      SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            new nmlEditor().setVisible(true);
	         }
	      });
		
	}
}