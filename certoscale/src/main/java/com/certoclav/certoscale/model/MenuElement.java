package com.certoclav.certoscale.model;


public class MenuElement {



	public enum MenuItemId {
		MENU_ITEM_APPLICATIONS,
		MENU_ITEM_APPLICATION_SETTINGS,
		MENU_ITEM_USER,
		MENU_ITEM_DEVICE,
		MENU_ITEM_LIBRARY,
		MENU_ITEM_RECIPES,
		MENU_ITEM_WEIGHING_UNITS,
		MENU_ITEM_CALIBRATION,
		MENU_ITEM_GLP
	}

	public MenuElement(String menuText, int imageResId, MenuItemId id) {
		this.menuText = menuText;
		this.imageResId = imageResId;
		this.id = id;
	}

	public MenuItemId getId() {
		return id;
	}

	public void setId(MenuItemId id) {
		this.id = id;
	}

	private MenuItemId id;
	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public int getImageResId() {
		return imageResId;
	}

	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

	private String menuText = "";
	private int imageResId = 0;



}




