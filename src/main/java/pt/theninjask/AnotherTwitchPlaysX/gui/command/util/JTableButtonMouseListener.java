package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class JTableButtonMouseListener extends MouseAdapter {

	private JTable table;

	private JButton selected;
	
	//TODO more parity with normal buttons aka same behavior
	
	public JTableButtonMouseListener(JTable table) {
		this.table = table;
		this.selected = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
			return;
		}
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();

		if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
			Object value = table.getValueAt(row, column);
			if (value instanceof JButton) {
				JButton tmp = ((JButton) value);
				// the code below was used in certain cases
				// but as the code is now laid out 
				// Im not seeing the case were this is now being needed
				//if(selected!=null && tmp!=selected)
				// selected.getModel().setPressed(false);
				selected = tmp;
				selected.getModel().setPressed(true);
				table.revalidate();
				table.repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
			return;
		}
		if(selected!=null) {
			selected.getModel().setPressed(false);
			table.revalidate();
			table.repaint();
		}
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();

		if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
			Object value = table.getValueAt(row, column);
			if (value instanceof JButton) {
				JButton tmp = ((JButton) value);
				if(selected==tmp)
					tmp.doClick();
			}
		}
	}
	
	/*@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
			return;
		}
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();

		if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
			Object value = table.getValueAt(row, column);
			if (value instanceof JButton) {
				System.out.println("clicked");
				JButton tmp = ((JButton) value);
				tmp.doClick();
			}
		}
	}*/
}
