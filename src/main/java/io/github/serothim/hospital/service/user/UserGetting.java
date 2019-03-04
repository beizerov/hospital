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
package io.github.serothim.hospital.service.user;

import java.util.Set;

import org.springframework.stereotype.Service;

import io.github.serothim.hospital.domain.User;
import io.github.serothim.hospital.repository.RoleRepository;
import io.github.serothim.hospital.repository.UserRepository;

/**
 * @author Alexei Beizerov
 *
 */
@Service("userGetting")
public class UserGetting {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	/**
	 * @param userRepository {@link 
	 * io.github.serothim.hospital.repository.UserRepository}
	 */
	public UserGetting(
			UserRepository userRepository, 
			RoleRepository roleRepository
	) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public Set<User> getUserByRole(String roleName) {
		return userRepository.findByRoles(roleRepository.findByRole(roleName));
	}
}