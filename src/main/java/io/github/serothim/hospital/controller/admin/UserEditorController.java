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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.service.UserFinding;
import io.github.serothim.hospital.service.UserSaving;

@Controller
public class UserEditorController {

	@Autowired
	private UserFinding userFinding;

	@Autowired
	private UserSaving userSaving;

	
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
