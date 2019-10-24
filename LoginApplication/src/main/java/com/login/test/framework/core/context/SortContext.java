package com.login.test.framework.core.context;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.login.test.pages.BasePage;

public class SortContext {

	public static List<WebElement> ascendingOrderList = null;
	public static List<WebElement> descendingOrderList = null;
	public static BasePage pageName =null;
	public static String headerElementString = null;
	/**
	 * @return the ascendingOrderList
	 */
	public static List<WebElement> getAscendingOrderList() {
		return ascendingOrderList;
	}
	/**
	 * @param ascendingOrderList the ascendingOrderList to set
	 */
	public static void setAscendingOrderList(List<WebElement> ascendingOrderList) {
		SortContext.ascendingOrderList = ascendingOrderList;
	}
	/**
	 * @return the descendingOrderList
	 */
	public static List<WebElement> getDescendingOrderList() {
		return descendingOrderList;
	}
	/**
	 * @param descendingOrderList the descendingOrderList to set
	 */
	public static void setDescendingOrderList(List<WebElement> descendingOrderList) {
		SortContext.descendingOrderList = descendingOrderList;
	}
	/**
	 * @return the pageName
	 */
	public static BasePage getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public static void setPageName(BasePage pageName) {
		SortContext.pageName = pageName;
	}
	/**
	 * @return the headerElementString
	 */
	public static String getHeaderElementString() {
		return headerElementString;
	}
	/**
	 * @param headerElementString the headerElementString to set
	 */
	public static void setHeaderElementString(String headerElementString) {
		SortContext.headerElementString = headerElementString;
	}
	
	
}
