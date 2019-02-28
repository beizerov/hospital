/*
 * MIT License
 * 
 * Copyright (c) 2018 Alexei Beizerov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.serothim.hospital.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.serothim.hospital.domain.Role;
import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.RoleGetting;
import io.github.serothim.hospital.service.UserDeletion;
import io.github.serothim.hospital.service.UserFinding;
import io.github.serothim.hospital.service.UserGetting;
import io.github.serothim.hospital.service.UserSaving;

@Controller
public class HomeController {
	
	@Autowired
	private UserFinding userFinding;

	@Autowired
	private UserGetting userGetting;

	@Autowired
	private RoleGetting roleGetting;

	@Autowired
	private UserDeletion userDeletion;
	
	@Autowired
	private UserSaving userSaving;

	
	private String whoiam() {
		Authentication auth = SecurityContextHolder
								.getContext()
								.getAuthentication();

		User user = userFinding.findByEmail(auth.getName());

		return user.getFirstName() + " " + user.getLastName();
	}
	
	private Map<String, Object> getModel() {
		Map<String, Object> model = new HashMap<>();
		
		model.put("greeting", "Welcome " + whoiam());
		model.put("users", userGetting.getAllUsers());

		return model;
	}

	@GetMapping("/admin/home")
	public String home(Map<String, Object> model) {

		model.putAll(getModel());
		
		return "admin/home";
	}

	@PostMapping("/admin/home")
	public String actOnUser(
			@RequestParam String email,
			@RequestParam String action,
			Map<String, Object> model
	) {
		String view = null;

		switch (action) {
		case "EDIT":
			List<Role> roles = new ArrayList<>();
			
			roleGetting.getAllRoles().forEach(roles::add);
			
			model.put(
					"userRole", 
					userFinding.findByEmail(email).getRoles().toArray()[0]
			);
			model.put("roles", roles);
			model.put("user", userGetting.getUserByEmail(email));
			view = "admin/editUser";

			break;
		case "DELETE":
			userDeletion.delete(userFinding.findByEmail(email));

			model.putAll(getModel()); 
			view = "admin/home";

			break;
		case "ACTIVITY":
			User user = userFinding.findByEmail(email);

			if(user.getActive() == 1) { 
				user.setActive(0); 
			} else {
				user.setActive(1);
			}

			userSaving.saveUserWithoutPasswordEncoding(user);
			
			model.putAll(getModel());
			view = "admin/home";
			
			break;
		}

		return view;
	}
}