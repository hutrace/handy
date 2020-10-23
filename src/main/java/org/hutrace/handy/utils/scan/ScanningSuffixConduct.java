package org.hutrace.handy.utils.scan;

import org.hutrace.handy.exception.ScanningApplicationException;

public interface ScanningSuffixConduct {
	
	public String[] getPackages();
	
	public boolean supports(String fileName);
	
	public void add(String resource) throws ScanningApplicationException;
	
}
