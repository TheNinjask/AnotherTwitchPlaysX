package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;

public class CommandTableModel{


	private DefaultTableModel table;
	
	public CommandTableModel() {
		table = new DefaultTableModel();
		table.addColumn("Lead");
		table.addColumn("Syntax");
		table.addColumn("Edit");
		table.addColumn("Remove");
	}
	
	public void addRow(CommandData data) {
		data.setLead(String.format("Test %s", Math.random()>0.5 ? "0" : "11111111111111"));
		JButton syntax = new JButton("Syntax");
		syntax.addActionListener(l->{
			System.out.println("syntax");
		});
		JButton edit = new JButton("Edit");
		edit.addActionListener(l->{
			System.out.println("edit");
		});
		JButton remove = new JButton("Remove");
		remove.addActionListener(l->{
			System.out.println("remove");
		});
		table.addRow(new Object[] {data, syntax, edit, remove});
	}
	
	public TableModel getModel() {
		return table;
	}
	
	public void adjustFont() {
		
	}
}
