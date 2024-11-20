package br.com.bdws.AccountsPayable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
public class AccountsPayableApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsPayableApplication.class, args);
	}

}
