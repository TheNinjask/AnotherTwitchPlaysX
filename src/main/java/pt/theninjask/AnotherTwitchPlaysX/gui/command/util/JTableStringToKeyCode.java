package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.util.List;

import javax.swing.JTable;

public class JTableStringToKeyCode extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StringToKeyCodeTableModel table;
	
	
	public JTableStringToKeyCode() {
		this.setTableHeader(null);
		this.setFocusable(false);
		this.setShowGrid(false);
		this.setEnabled(false);
		table = new StringToKeyCodeTableModel();
		this.setModel(table.getModel());
		this.getColumn(StringToKeyCodeTableModel.REMOVE_COLUMN).setCellRenderer(new JTableButtonRenderer());
		this.getColumn(StringToKeyCodeTableModel.REMOVE_COLUMN).setPreferredWidth(10);
		this.addMouseListener(new JTableButtonMouseListener(this));
	}
	
	public boolean isCellEditable(int row, int column){  
        return false;  
    }
	
	public void addRow(String key) {
		table.addRow(key);
	}
	
	public void clearAndSet(List<String> keys) {
		table.clear();
		for (String key : keys) {
			table.addRow(key);
		}
	}

}
