package com.company.dept;

import static com.company.dept.util.CommonUtil.getSalted;
import static com.company.dept.util.CommonUtil.matchConditions;
import static com.company.dept.util.FileOps.addLine;
import static com.company.dept.util.FileOps.getAccount;
import static com.company.dept.util.FileOps.getAccounts;
import static com.company.dept.util.FileOps.getLinesFromFile;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.dept.constants.DAConstants;
import com.company.dept.model.Account;

@RestController
@RequestMapping("/dictionary-attack")
public class AttackController {
	
	@Value("${common.salt.key}")
	private String saltKey;
	
	@Value("${dictionary.datasource.file}")
	private String dictionaryFile;
	
	@Value("${account.datasource.file}")
	private String acntFile;
	
	/**
	 * Simple GET call to process all internal userids to expose(match with dictionary)
	 * their passwords in console [http://localhost:8080/dictionary-attack/console-out]
	 */
	@RequestMapping("/console-out")
	public String process() throws Exception{
		List<String> words = getLinesFromFile(dictionaryFile);
		List<Account> accounts = getAccounts(acntFile);
		
		StringBuffer sb = new StringBuffer();
		for(String word : words) {
			for(Account acnt: accounts) {
				if(acnt.getPassword().equals(getSalted(saltKey, word))) {
					System.out.println("Password is " + word);
					sb.append("User : " + acnt.getUserId() + " - ");
					sb.append("Password : " + word + " || ");
				}
			}
		}
		
		return sb.toString();
	}
	
	@GetMapping("/{user}/password")
	public String getPassword(@PathVariable String user) throws Exception{
		List<String> words = getLinesFromFile(dictionaryFile);
		Account acnt = getAccount(acntFile, user);
		
		for(String word : words) {	
			String checked = matchConditions(saltKey, acnt.getPassword(), word);
			if(null != checked) return checked;
		}
		
		return DAConstants.UNAVAILABLE;
	}
	
	@PostMapping(value = "/add-account")
	@ResponseStatus(HttpStatus.CREATED)
	public String addAccount(@RequestBody Account account) throws Exception{
		String passwd_hash = getSalted(saltKey, account.getPassword());  // User validation is omited due to scope
		String newLine = account.getUserId() + " " + passwd_hash;
		addLine(acntFile, newLine);
		return DAConstants.SUCCESS;
	}
}
