package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.util.List;

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
		this.getColumn("Syntax").setPreferredWidth(75);
		this.getColumn("Edit").setCellRenderer(new JTableButtonRenderer());
		this.getColumn("Edit").setPreferredWidth(50);
		this.getColumn("Remove").setCellRenderer(new JTableButtonRenderer());
		this.getColumn("Remove").setPreferredWidth(85);
		this.addMouseListener(new JTableButtonMouseListener(this));
	}
	
	public boolean isCellEditable(int row, int column){  
        return false;  
    }
	
	public void addRow(CommandData data) {
		table.addRow(data);
	}
	
	public void clearAndSet(List<CommandData> data) {
		table.clear();
		for (CommandData commandData : data) {
			table.addRow(commandData);
		}
	}

}
