package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.UUID;



@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {


	@Autowired
	private RoleRepository roleRepository;

	@Value("${normal.role.id}")
	private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	public static void main(String[] args){

		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		try
		{
			Role admin_role= Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role normal_role = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();

			roleRepository.save(admin_role);
			roleRepository.save(normal_role);


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
