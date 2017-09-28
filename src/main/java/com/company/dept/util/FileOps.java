package com.company.dept.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.company.dept.model.Account;

public class FileOps {
	
	public static List<Account> getAccounts(String file){
		List<String> lines = getLinesFromFile(file);
		List<Account> accounts = new ArrayList<Account>();
		for(String s : lines) {
			accounts.add(new Account(s));
		}
		return accounts;
	}
	
	public static Account getAccount(String file, String userId){
		List<String> lines = getLinesFromFile(file);
		String[] input = null;
		for(String s : lines) {
			input = s.split("\\s+");
			if(input[0].equalsIgnoreCase(userId)) return new Account(s);
		}
		return null;
	}
	
	public static List<String> getLinesFromFile(String filePath){
		FileInputStream stream = null;
	    	BufferedReader buffer = null;
	    	String line = null;
	    	List<String> lines = new ArrayList<String>();
	    	try {
	    			File file = new File(filePath);
	    			stream = new FileInputStream(file);
				buffer = new BufferedReader(new InputStreamReader(stream));
		    	
		    	while ((line = buffer.readLine()) != null) {
		    		lines.add(line);
		    	}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
						if(null != buffer) buffer.close();
						if(null != stream) stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}	
			}
	    	
	    	return lines;
	}
	
	public static void addLine(String filePath, String newLine) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(filePath, true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
		    out.println(newLine);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}		
	}
}
