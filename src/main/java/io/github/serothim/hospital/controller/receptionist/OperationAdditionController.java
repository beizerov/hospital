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
/**
 * 
 */
package io.github.serothim.hospital.controller.receptionist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.github.serothim.hospital.domain.Operation;
import io.github.serothim.hospital.service.operatingtheater.GettingTheOperatingTheater;
import io.github.serothim.hospital.service.operation.SavingTheOperation;
import io.github.serothim.hospital.service.user.UserGetting;

/**
 * @author Alexei Beizerov
 *
 */
@Controller
public class OperationAdditionController {
	
	@Autowired
	private UserGetting userGetting;
	
	@Autowired
	private SavingTheOperation savingTheOperation;
	
	@Autowired
	private GettingTheOperatingTheater gettingTheOperatingTheater;
	
	private ModelAndView getModelAndViewForAddOperation() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("doctors", userGetting.getUsersByRole("DOCTOR"));
		modelAndView.addObject("operation", new Operation());
		modelAndView.addObject(
				"operatingTheaters", 
				gettingTheOperatingTheater.getAllOperatingTheater()
		);
		
		return modelAndView;
	}
	
	@GetMapping("/receptionist/addOperation")
	public ModelAndView addOperation() {
		return getModelAndViewForAddOperation();
	}
	
	@PostMapping("/receptionist/addOperation")
	public ModelAndView addNewOperation(
			Operation operation,
			@RequestParam String email,
			@RequestParam long id
										 
	) {
		operation.setDoctor(userGetting.getUserByEmail(email));
		
		operation.setOperatingTheater(
		gettingTheOperatingTheater.getOperatingTheaterById(id));
		 
		savingTheOperation.save(operation);

		return getModelAndViewForAddOperation();
	}
}
