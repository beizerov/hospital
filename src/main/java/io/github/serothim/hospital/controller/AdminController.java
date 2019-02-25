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
package io.github.serothim.hospital.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.Role;
import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.UserSaving;
import io.github.serothim.hospital.service.RoleGetting;
import io.github.serothim.hospital.service.UserDeletion;
import io.github.serothim.hospital.service.UserFinding;
import io.github.serothim.hospital.service.UserGetting;

/**
 *
 * @author Alexei Beizerov
 */
@Controller
public class AdminController {

	@Autowired
	private UserFinding userFinding;

	@Autowired
	private UserGetting userGetting;

	@Autowired
	private RoleGetting roleGetting;

	@Autowired
	private UserSaving userSaving;

	@Autowired
	private UserDeletion userDeletion;

	private String whoiam() {
		Authentication auth = SecurityContextHolder
								.getContext()
								.getAuthentication();

		User user = userFinding.findByEmail(auth.getName());

		return user.getFirstName() + " " + user.getLastName();
	}

	private ModelAndView getModelAndViewForAdminHome() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userName", "Welcome " + whoiam());
		modelAndView.addObject("users", userGetting.getAllUsers());
		modelAndView.setViewName("admin/home");

		return modelAndView;
	}

	private void setUserRoles(User user, List<String> roleNameList) {
		List<Role> roleList = new ArrayList<>();

		for (String name : roleNameList)
			roleList.add(roleGetting.getRoleByName(name));

		roleNameList.forEach((roleName) -> {
			roleList.add(roleGetting.getRoleByName(roleName));
		});

		Set<Role> roles = new HashSet<>(roleList);

		user.setRoles(roles);
	}

	@GetMapping(value = "/admin/home")
	public ModelAndView home() {

		return getModelAndViewForAdminHome();
	}

	@PostMapping(value = "/admin/home")
	public ModelAndView deleteOrEditUser(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "action") String action
	) {

		ModelAndView modelAndView = null;

		switch (action) {
		case "EDIT":
			modelAndView = new ModelAndView();

			modelAndView.addObject("user", userGetting.getUserByEmail(email));
			modelAndView.addObject("roles", roleGetting.getAllRoles());
			modelAndView.setViewName("admin/editUser");

			break;
		case "DELETE":
			userDeletion.delete(userFinding.findByEmail(email));

			modelAndView = getModelAndViewForAdminHome();

			break;
		}

		return modelAndView;
	}

	@GetMapping(value = "/admin/addUser")
	public ModelAndView addUser() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.addObject("selectedRoles", new ArrayList<String>());
		modelAndView.setViewName("admin/addUser");
		return modelAndView;
	}

	@PostMapping(value = "/admin/addUser")
	public ModelAndView addNewUser(
			@Valid User user, 
			BindingResult bindingResult
	) {

		ModelAndView modelAndView = new ModelAndView();
		User userExists = userFinding.findByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", 
									  "error.user",
									  "There is already a user registered" 
									  + " with the email provided"
			);
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/addUser");
		} else {

			setUserRoles(user, new ArrayList<>());

			userSaving.save(user);
			modelAndView.addObject("successMessage", 
								   "User has been registered successfully"
			);
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/addUser");
		}
		
		return modelAndView;
	}
	
	@PostMapping(value = "/admin/editUser")
	public ModelAndView editUser(
			@Valid User user, 
			BindingResult bindingResult
	) {

		ModelAndView modelAndView = new ModelAndView();
		User userExists = userFinding.findByEmail(user.getEmail());
		if (userExists != null && user.getId() == userExists.getId()) {

			userExists.setFirstName(user.getFirstName());
			userExists.setLastName(user.getLastName());
			userExists.setPassword(
					user.getPassword().isEmpty() ? userExists.getPassword() 
							: user.getPassword()
			);
			setUserRoles(user, new ArrayList<>());

			userSaving.save(userExists);
			modelAndView.addObject("successMessage", 
								   "User has been changed successfully"
			);
			modelAndView.addObject("user", user);
			modelAndView.setViewName("admin/editUser");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/editUser");
		} 
		return modelAndView;
	}
}
