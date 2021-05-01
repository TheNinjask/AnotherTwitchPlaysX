package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class JTableButtonRenderer extends DefaultTableCellRenderer {		
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(!(value instanceof JButton))
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JButton button = (JButton)value;
		
	    if (isSelected) {
	      button.setForeground(table.getSelectionForeground());
	      button.setBackground(table.getSelectionBackground());
	    } else {
	      button.setForeground(table.getForeground());
	      button.setBackground(UIManager.getColor("Button.background"));
	    }
	    return button;	
	  }
	}
