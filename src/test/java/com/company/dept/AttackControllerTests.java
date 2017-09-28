package com.company.dept;

import static com.company.dept.util.CommonUtil.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.dept.model.Account;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AttackControllerTests {
	@Autowired
    private MockMvc mockMvc;
	
	@Test
    public void addAccountShouldReturnOK() throws Exception {
		this.mockMvc.perform(post("/dictionary-attack/add-account")
      		  .content(asJsonString(new Account("user password")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful());
    }
	
	@Test
    public void addAccountAndPasswordShouldReturnSame() throws Exception {
		String user = "user3451", passwd = "password";
		this.mockMvc.perform(post("/dictionary-attack/add-account")
      		  .content(asJsonString(new Account(user + " " + passwd)))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful());
		this.mockMvc.perform(get("/dictionary-attack/" + user +"/password")).andDo(print()).andExpect(status().is2xxSuccessful())
		.andExpect(content().string(containsString(passwd)));
    }
}
