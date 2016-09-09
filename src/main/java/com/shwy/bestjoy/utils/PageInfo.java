package com.shwy.bestjoy.utils;

public class PageInfo implements IPageInterface{
	public static final int DEFAULT_PAGEINDEX = 1;
	public static final int PER_PAGE_SIZE = 25;
	public static final int MAX_PAGE_SIZE = 50;
	public long mTotalCount;
	public String mTag;
	public int mDefaultMaxPageSize = MAX_PAGE_SIZE;
	public int mDefaultPerPageSize = PER_PAGE_SIZE;
	public int mPageIndex = DEFAULT_PAGEINDEX;
	public int mPageSize = mDefaultPerPageSize;


     public PageInfo() {
    	 
     }
     
     public void reset() {
    	 mPageSize = PER_PAGE_SIZE;
    	 mPageIndex = DEFAULT_PAGEINDEX;
    	 mTotalCount = 0;
    	 
     }
     
     public void computePageSize(int newPageSize) {
    	 if (newPageSize > mDefaultMaxPageSize) {
    		 mPageSize = mDefaultMaxPageSize;
    	 } else if (newPageSize < mDefaultPerPageSize) {
    		 mPageSize = mDefaultPerPageSize;
    	 } else {
    		 mPageSize = newPageSize;
    	 }
     }
}
