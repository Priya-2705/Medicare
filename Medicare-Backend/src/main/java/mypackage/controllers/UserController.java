package mypackage.controllers;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mypackage.entities.Role;
import mypackage.entities.User;
import mypackage.entities.UserRole;
import mypackage.services.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//init admin user
	@PostConstruct
	public void createAdmin(){
		User admin = new User();
		admin.setUsername("priya@medicare.com");
		admin.setPassword("Priya@123");
		admin.setFirstName("Priya");
		admin.setLastName("P");
		admin.setContactNumber("7891234560");
		Role role = new Role();
		role.setRoleId(101L);
		role.setRoleName("ADMIN");
		UserRole ur = new UserRole();
		ur.setUser(admin);
		ur.setRole(role);
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(ur);
		User adminCreated = this.userService.createUser(admin, userRole);
		System.out.println("Admin username: "+adminCreated.getUsername());
		System.out.println("Admin Password: "+adminCreated.getPassword());
	}
	
	//create new user
	@PostMapping("/user/signup")
	public ResponseEntity<?> createNewUser(@Valid @RequestBody User user){
		Role role = new Role();
		role.setRoleId(102L);
		role.setRoleName("USER");
		UserRole ur = new UserRole();
		ur.setUser(user);
		ur.setRole(role);
		Set<UserRole> userRole = new HashSet<>();
		userRole.add(ur);
		if(this.userService.getByUsername(user.getUsername())!=null) {
			System.out.println("Username already exists!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}else {
			User newUser = this.userService.createUser(user, userRole);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getUserId()).toUri();
			return ResponseEntity.created(location).build();
		}
	}
}