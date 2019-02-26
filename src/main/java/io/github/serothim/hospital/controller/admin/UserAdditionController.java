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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.UserFinding;
import io.github.serothim.hospital.service.UserSaving;

@Controller
public class UserAdditionController {
	
	@Autowired
	private UserFinding userFinding;

	@Autowired
	private UserSaving userSaving;

	
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
			userSaving.save(user);
			modelAndView.addObject("successMessage", 
								   "User has been registered successfully"
			);
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/addUser");
		}
		
		return modelAndView;
	}
}
