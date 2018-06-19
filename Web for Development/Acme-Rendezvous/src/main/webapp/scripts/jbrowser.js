/**
 * Funcion que obtiene el valor de un input por su id
 * y lo pasa a trip/list.d como parametro
 * 
 */

function getKeyWordAndList() {
    var keyWord = document.getElementById("keyWord").value;
    var url = "trip/search.do?keyWord=" + keyWord;
    window.location.href=url;
}
function setCategoria(categ, id) {
	document.getElementById("categoriaName").innerHTML = categ;
	document.getElementById("categoryId").value = id;
	document.getElementById("legatTextId").value = 1792;	
	document.getElementById("categoria2").value = categ;
}


function confirmationWindow(msg, catId) {
	if (confirm(msg)) {
		relativeRedir('category/delete.do?categoryId=' + catId );
	}
}

function deleteConfirmationWindow(msg, Id, url) {
	if (confirm(msg)) {
		relativeRedir(url + Id );
	}
}


/* When the user clicks on the button, 
 toggle between hiding and showing the dropdown content */
function dropDown() {
	document.getElementById("myDropdown").classList.toggle("show");
}


// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
	if (!event.target.matches('.dropbtn')) {

		var dropdowns = document.getElementsByClassName("dropdown-content");
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
};


