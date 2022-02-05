package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class TrayManager {

	private static final TrayManager singleton = new TrayManager();
	
	private List<MenuItem> options = new ArrayList<MenuItem>();
	
	private boolean isActive = false;

	private TrayIcon trayIcon = new TrayIcon(new ImageIcon(Constants.ICON_PATH).getImage());
	
	private PopupMenu popup = new PopupMenu();
	
	private TrayManager() {
		trayIcon.setImageAutoSize(true);
	}
	
	public static TrayManager getInstance() {
		return singleton;
	}
	
	public boolean isSupported() {
		return SystemTray.isSupported();
	}
	
	public boolean startUp() {
		if (!isActive && !options.isEmpty() && SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			options.forEach(opt->{
				popup.add(opt);
			});
			trayIcon.setPopupMenu(popup);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				return false;
			}
			isActive = true;
			return isActive;
		}
		return false;
	}
	
	public boolean refresh() {
		return stop() && startUp();
	}
	
	public boolean stop() {
		if (isActive && SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			tray.remove(trayIcon);
			isActive=false;
		}
		return true;
	}
	
	public TrayManager setToolTip(String tooltip) {
		trayIcon.setToolTip(tooltip);
		return this;
	}
	
	public TrayManager setImage(Image image) {
		trayIcon.setImage(image);
		return this;
	}
	
	public TrayManager setImage(URL image) {
		trayIcon.setImage(new ImageIcon(image).getImage());
		return this;
	}
	
	public TrayManager addMenuItem(MenuItem item) {
		options.add(item);
		return this;
	}
	
	public TrayManager removeMenuItem(MenuItem item) {
		options.remove(item);
		return this;
	}
	
	public TrayManager clearMenuItems() {
		options.clear();
		return this;
	}
	
}
