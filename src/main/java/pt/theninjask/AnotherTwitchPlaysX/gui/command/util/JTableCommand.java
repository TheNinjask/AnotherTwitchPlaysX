package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import javax.swing.JTable;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;

public class JTableCommand extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CommandTableModel table;
	
	
	public JTableCommand() {
		this.setTableHeader(null);
		this.setFocusable(false);
		this.setShowGrid(false);
		this.setEnabled(false);
		table = new CommandTableModel();
		this.setModel(table.getModel());
		this.getColumn("Syntax").setCellRenderer(new JTableButtonRenderer());
		this.getColumn("Edit").setCellRenderer(new JTableButtonRenderer());
		this.getColumn("Remove").setCellRenderer(new JTableButtonRenderer());
		this.addMouseListener(new JTableButtonMouseListener(this));
	}
	
	public boolean isCellEditable(int row, int column){  
        return false;  
    }
	
	public void addRow(CommandData data) {
		table.addRow(data);
	}

}
