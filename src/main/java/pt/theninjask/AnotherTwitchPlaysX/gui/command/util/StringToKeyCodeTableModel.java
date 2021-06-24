package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class StringToKeyCodeTableModel {

	private DefaultTableModel table;

	public static final String KEYCODE_COLUMN = "KeyCode";
	
	public static final String REMOVE_COLUMN = "Remove";
	
	public StringToKeyCodeTableModel() {
		table = new DefaultTableModel();
		table.addColumn(KEYCODE_COLUMN);
		table.addColumn(REMOVE_COLUMN);
	}

	public void addRow(String keyText) {
		JButton remove = new JButton(DataManager.getLanguage().getStringToKeyCode().getRemove());
		remove.setMargin(new Insets(0, 0, 0, 0));
		remove.addActionListener(l -> {
			Constants.STRING_TO_KEYCODE.remove(keyText);
			for (int i = 0; i < table.getRowCount(); i++) {
				Object tmp = table.getValueAt(i, 0);
				if (tmp instanceof String) {
					if (((String) tmp).equals(keyText)) {
						table.removeRow(i);
						return;
					}
				}
			}
		});
		table.addRow(new Object[] { keyText, remove });
	}

	public TableModel getModel() {
		return table;
	}

	public void clear() {
		table.setRowCount(0);
	}

}
