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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.role.RoleGetting;
import io.github.serothim.hospital.service.user.UserDeletion;
import io.github.serothim.hospital.service.user.UserGetting;
import io.github.serothim.hospital.service.user.UserSaving;
import io.github.serothim.hospital.service.user.WhoIAm;

/**
 * @author Alexei Beizerov
 *
 */
@Controller
public class AdminController {
	
	@Autowired
	private UserGetting userGetting;

	@Autowired
	private RoleGetting roleGetting;

	@Autowired
	private UserDeletion userDeletion;
	
	@Autowired
	private UserSaving userSaving;

	@Autowired
	private WhoIAm whoIAm;
	
	private ModelAndView getModelAndViewForAdminHome() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject(
				"greeting", 
				"Welcome " + whoIAm.getFullNameOfAuthenticatedUser()
		);
		modelAndView.addObject("users", userGetting.getAllUsers());
		modelAndView.setViewName("admin/home");
		
		return modelAndView;
	}

	@GetMapping("/admin/home")
	public ModelAndView home() {
		return getModelAndViewForAdminHome();
	}

	@PostMapping("/admin/activity")
	public ModelAndView switchActivity(@RequestParam String email) {
		User user = userGetting.getUserByEmail(email);

		if(user.isEnabled()) { 
			user.setActive(0); 
		} else {
			user.setActive(1);
		}
		
		userSaving.saveUserWithoutPasswordEncoding(user);
		
		return getModelAndViewForAdminHome();
	}
	
	@PostMapping("/admin/edit")
	public ModelAndView editUser(@RequestParam String email) {
		ModelAndView modelAndView = new ModelAndView();		
		
		modelAndView.addObject(
				"userRole", 
				userGetting.getUserByEmail(email).getRoles().toArray()[0]
		);
		modelAndView.addObject("roles", roleGetting.getAllRoles());
		modelAndView.addObject("user", userGetting.getUserByEmail(email));
		modelAndView.setViewName("admin/editUser");
		
		return modelAndView;
	}
	
	@PostMapping("/admin/delete")
	public ModelAndView deleteUser(@RequestParam String email) {
		userDeletion.delete(userGetting.getUserByEmail(email));
 
		return getModelAndViewForAdminHome();
	}
}