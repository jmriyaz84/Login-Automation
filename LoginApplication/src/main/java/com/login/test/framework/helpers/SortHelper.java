package com.login.test.framework.helpers;

import com.login.test.framework.core.context.SortContext;
import com.login.test.pages.BasePage;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SortHelper extends BasePage{
	
	/**
	 * Click the header field for Sorting and store the sorted list in a Context
	 * @param pageName, headerElementString, recordElementsString
	 * @return void
	 * @throws Exception
	 */
	public void clickFieldToSort(BasePage pageName, String headerElementString, String recordElementsString) throws Exception {
		
		WebElement headerElement=null;
		List<WebElement> ascList=null;
		List<WebElement> descList=null;
		try{
			headerElement=pageName.getElement(headerElementString);	// Get the header of the particular field to be sorted
			WaitHelper.waitUntilClickable(headerElement);
			if(pageName.getElement(headerElementString).getAttribute("md-desc")==null) {
				WebElementHelper.clickElement(headerElement);			// Click the header of the particular field to be sorted
				ascList=pageName.getElements(recordElementsString);		// Get the records in ascending order
				SortContext.setAscendingOrderList(ascList);				// Store the ascending order list in a Context
				
				WebElementHelper.clickElement(headerElement);			// Click the header of the particular field to be sorted for second time
				descList=pageName.getElements(recordElementsString);    // Get the records in descending order
				SortContext.setDescendingOrderList(descList);			// Store the descending order list in a Context
				
			} else if(pageName.getElement(headerElementString).getAttribute("md-desc")!=null) {
				WebElementHelper.clickElement(headerElement);			// Click the header of the particular field to be sorted for second time
				descList=pageName.getElements(recordElementsString);    // Get the records in descending order
				SortContext.setDescendingOrderList(descList);			// Store the descending order list in a Context
				
				WebElementHelper.clickElement(headerElement);			// Click the header of the particular field to be sorted
				ascList=pageName.getElements(recordElementsString);		// Get the records in ascending order
				SortContext.setAscendingOrderList(ascList);				// Store the ascending order list in a Context
			}
			SortContext.setHeaderElementString(headerElementString);
			SortContext.setPageName(pageName);
		} catch(Exception ex) {
			throw new Exception("Error occurred while clicking the field "+headerElementString+" in page "+pageName+" for sort "+ ex.getMessage());
		}
	}	
	
	/**
	 * Get the flag for Text Sorting
	 * @param 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean getTextSortFlag() throws Exception {
		BasePage pageName=null;
	    String headerName=null;
		List<WebElement> ascList=null;
		List<WebElement> descList=null;
		int maxRowCount=0;
		boolean sortFlag=false;
		try {
			ascList=SortContext.getAscendingOrderList();
			descList=SortContext.getDescendingOrderList();
			pageName=SortContext.getPageName();
			headerName=SortContext.getHeaderElementString();
			if(ascList.size()>20) {
				maxRowCount=20;
			} else {
				maxRowCount=ascList.size();
			}
			if(ascList!=null && descList!=null && (ascList.size()==descList.size())) {
				for(int i=0; i<maxRowCount; i++) {
			
					// Compares and Checks if the sorting logic is working fine
					if(WebElementHelper.getText(ascList.get(i)).equals(WebElementHelper.getText(descList.get(descList.size()-i-1)))) {
						if((i!=ascList.size()-1) && WebElementHelper.getText(ascList.get(i)).compareTo(WebElementHelper.getText(ascList.get(i+1)))<=0) {
							sortFlag=true;
						}
					} else {
						sortFlag=false;
						break;
					}
					
				}
			}	
		} catch(Exception ex) {
			throw new Exception("Error in while sorting the text field "+headerName+" in page "+pageName+" in getTextSortFlag() "+ex.getMessage());
		}
		return sortFlag;
	}
		
	
	
	/**
	 * Get the flag for Date Sorting
	 * @param 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean getDateSortFlag() throws Exception {
				
		    BasePage pageName=null;
		    String headerName=null;
			List<WebElement> ascList=null;
			List<WebElement> descList=null;
			boolean sortFlag=false;
			SimpleDateFormat sdf = null;
			int maxRowCount=0;
			try {
				ascList=SortContext.getAscendingOrderList();
				descList=SortContext.getDescendingOrderList();
				pageName=SortContext.getPageName();
				headerName=SortContext.getHeaderElementString();
				sdf = new SimpleDateFormat("dd MMMMM yyyy");
				if(ascList.size()>20) {
					maxRowCount=20;
				} else {
					maxRowCount=ascList.size();
				}
				if(ascList!=null && descList!=null && (ascList.size()==descList.size())) {
					for(int i=0; i<maxRowCount; i++) {
				
						// Compares and Checks if the sorting logic is working fine
						if(WebElementHelper.getText(ascList.get(i)).equals(WebElementHelper.getText(descList.get(descList.size()-i-1)))) {
							try {
								if((i!=ascList.size()-1) && sdf.parse(WebElementHelper.getText(ascList.get(i))).before(sdf.parse(WebElementHelper.getText(ascList.get(i+1))))) {
									sortFlag=true;
								}
							} catch(ParseException pe) {			// Catches the date record having blank value
								System.out.println("Parse Exception "+pe.getMessage());
							}
						} else {
							sortFlag=false;
							break;
						}
						
					}
				}	
			} catch(Exception ex) {
				throw new Exception("Error in while sorting the date field "+headerName+" in page "+pageName+" in getDateSortFlag() "+ ex.getMessage());
			}
		return sortFlag;
	}

}
