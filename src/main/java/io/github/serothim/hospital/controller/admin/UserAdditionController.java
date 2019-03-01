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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.serothim.hospital.domain.Role;
import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.RoleGetting;
import io.github.serothim.hospital.service.UserFinding;
import io.github.serothim.hospital.service.UserSaving;

/**
 * @author Alexei Beizerov
 *
 */
@Controller
public class UserAdditionController {
	
	@Autowired
	private UserFinding userFinding;

	@Autowired
	private UserSaving userSaving;
	
	@Autowired
	private RoleGetting roleGetting;

	
	private Map<String, Object> getModel() {
		Map<String, Object> model = new HashMap<>();
		
		model.put("user", new User());

		List<Role> roles = new ArrayList<>();
		roleGetting.getAllRoles().forEach(roles::add);
		model.put("roles", roles);
		
		return model;
	}
	
	@GetMapping("/admin/addUser")
	public String addUser(Map<String, Object> model) {

		model.putAll(getModel());
		
		return "admin/addUser";
	}

	@PostMapping("/admin/addUser")
	public String addNewUser(
			User user, 
			BindingResult bindingResult,
			@RequestParam String role,
			Map<String, Object> model
	) {
		User userExists = userFinding.findByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue(
					"email", 
					"error.user",
					"There is already a user registered with the email provided"
			);	
		}
		
		if (!bindingResult.hasErrors()) {
			model.put(
					"successMessage", 
					"User has been added successfully"
			);
				
			Set<Role> roleSet = new HashSet<>();
			roleSet.add(roleGetting.getRoleByName(role));
			user.setRoles(roleSet);

			userSaving.save(user);
			
			model.putAll(getModel());		
		}
		
		return "admin/addUser";
	}
}
