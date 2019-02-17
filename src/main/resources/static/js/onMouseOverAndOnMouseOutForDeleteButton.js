/**
 * Added event listeners for a delete user button
 */

document.addEventListener("DOMContentLoaded", () => {
	let buttonsCollection = document.getElementsByClassName("delete-btn");
	for (let button of buttonsCollection)
		button.addEventListener("mouseover", function() {
			this.src = "/images/red-recycle-bin.png";
		});
});

document.addEventListener("DOMContentLoaded", () => {
	let buttonsCollection = document.getElementsByClassName("delete-btn");
	for (let button of buttonsCollection)
		button.addEventListener("mouseout", function() {
			this.src = "/images/green-recycle-bin.png";
	});
});