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
 * Author:  Alexei Beizerov
 * Created: Jan 26, 2019
 */

REPLACE INTO `roles` VALUES (1,'ADMIN');
REPLACE INTO `roles` VALUES (2,'RECEPTIONIST');
REPLACE INTO `roles` VALUES (3,'DOCTOR');

/* 
 * Adding an  administrator account by default, 
 * which you can later delete or change. 
 * Default account created automatically if there 
 * are no any other records in the database.
 */
REPLACE INTO `users` (`user_id`, `email`, `password`, `active`)
SELECT * 
FROM (SELECT 0,'admin@dmin', '$2a$10$refpi22VWy7nCsfoImqZ/O/MncSw6vZLiJGiJvsx3KR2w3fHBxZFO', 1) AS tmp
WHERE NOT EXISTS (SELECT `email` FROM `users` WHERE user_id > 0 );

REPLACE INTO `user_roles` (`user_id`, `role_id`) 
SELECT * 
FROM (SELECT 0, 1) AS tmp
WHERE NOT EXISTS (SELECT 'user_id' FROM `user_roles` WHERE user_id = 0);
