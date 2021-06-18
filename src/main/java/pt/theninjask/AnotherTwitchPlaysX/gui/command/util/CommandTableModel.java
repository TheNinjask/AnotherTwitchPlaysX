package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.CommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

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
		data.setLead(data.getLead());
		JButton syntax = new JButton("Syntax");
		syntax.setMargin(new Insets(0, 0, 0, 0));
		syntax.addActionListener(l->{
			try {
				JPanel tmp = new JPanel(new GridLayout(2, 1));
				tmp.setOpaque(false);
				JLabel syntaxLabel = new JLabel(String.format("Syntax: %s", data.getRegex()));
				syntaxLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(syntaxLabel);
				JLabel egLabel = new JLabel(String.format("Example: %s", data.getRegexExample()));
				egLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(egLabel);
				Constants.showCustomColorMessageDialog(
						null, 
						tmp, 
						"Syntax of command", 
						JOptionPane.PLAIN_MESSAGE, 
						null, 
						Constants.TWITCH_COLOR
						);
				}catch (Exception e) {
					Constants.showExpectedExceptionDialog(e);
				}
		});
		JButton edit = new JButton("Edit");
		edit.setMargin(new Insets(0, 0, 0, 0));
		edit.addActionListener(l->{
			MainFrame.replacePanel(new CommandPanel(data));
		});
		JButton remove = new JButton("Remove");
		remove.setMargin(new Insets(0, 0, 0, 0));
		remove.addActionListener(l->{
			DataManager.getInstance().getCommands().remove(data);
			for(int i=0;i<table.getRowCount();i++) {
				Object tmp = table.getValueAt(i, 0);
				if(tmp instanceof CommandData) {
					if(((CommandData) tmp).equals(data)) {
						table.removeRow(i);
						return;
					}
				}
			}				
		});
		table.addRow(new Object[] {data, syntax, edit, remove});
	}
	
	public TableModel getModel() {
		return table;
	}
	
	public void clear() {
		table.setRowCount(0);
	}
	
	@Deprecated
	public void adjustFont() {
	}
}
