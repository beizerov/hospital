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

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.Role;
import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.role.RoleGetting;
import io.github.serothim.hospital.service.user.UserGetting;
import io.github.serothim.hospital.service.user.UserSaving;

/**
 * @author Alexei Beizerov
 *
 */
@Controller
public class UserAdditionController {
	
	@Autowired
	private UserGetting userGetting;
	
	@Autowired
	private UserSaving userSaving;
	
	@Autowired
	private RoleGetting roleGetting;

	
	private ModelAndView getModelAndViewForAddUser() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("roles", roleGetting.getAllRoles());
		modelAndView.addObject("user", new User());
		
		return modelAndView;
	}
	
	@GetMapping("/admin/addUser")
	public ModelAndView addUser() {
		return getModelAndViewForAddUser();
	}

	@PostMapping("/admin/addUser")
	public ModelAndView addNewUser(
			User user, 
			BindingResult bindingResult,
			@RequestParam String role
	) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userGetting.getUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue(
					"email", 
					"error.user",
					"There is already a user registered with the email provided"
			);	
		}
		
		if (!bindingResult.hasErrors()) {
			modelAndView = getModelAndViewForAddUser();
			modelAndView.addObject(
					"successMessage", 
					"User has been added successfully"
			);
				
			Set<Role> roleSet = new HashSet<>();
			roleSet.add(roleGetting.getRoleByName(role));
			user.setRoles(roleSet);

			userSaving.save(user);	
		}
		
		return modelAndView;
	}
}
