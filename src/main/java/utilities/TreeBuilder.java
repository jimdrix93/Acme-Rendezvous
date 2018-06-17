package utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.Category;

public class TreeBuilder {

	/**
	 * jspTree(Collection) devuelve una cadena para incrustar en datos dinámicos en
	 * forma de arbol en la vista, a partir de una coleccion de pares padre hijo.
	 * 
	 * @return String
	 **/
	public static String jspTree(Collection<Category> categories, String user) {
		String res = "";
		Category node = new Category();
		res += jspTree(categories, node.getId(), user);
		return res;
	}

	/**
	 * Devuenve una cadena html que representa una lista jerarquica de la coleccion
	 * pasada como parametro, con raiz en nodo pasado como parametro.
	 * 
	 * @param categories
	 * @return Strig containing HTML code
	 */

	public static String jspTree(Collection<Category> categories, Integer parentNode, String user) {
		String res = "";
		if (parentNode != null) {
			if (parentNode == 0) {
				res += jspTree(categories, null, user);
			} else {

				for (Category category : categories) {
					if (category.getParentCategory() != null && category.getParentCategory().getId() == parentNode) {
						// lista.add category

						res += "<li><a href=\"category/" + user + "browse.do?categoryId=" + category.getId() + "\">"
								+ category.getName() + "</a>" + "<ul>" + jspTree(categories, category.getId(), user);
						res += "</ul> </li>";

					}
				}
			}
		} else {
			for (Category category : categories) {
				if (category.getId() != 0 && !(category.getParentCategory() != null)) {
					// lista.add category

					res += "<li><a href=\"category/" + user + "browse.do?categoryId=" + category.getId() + "\">"
							+ category.getName() + "</a>" + "<ul>" + jspTree(categories, category.getId(), user);
					res += "</ul> </li>";

				}
			}
		}

		res += "\n";
		return res;

	}

	public static List<Category> getRuta(Category selected) {
		List<Category> res = null;
		Category cat = selected;
		res = new ArrayList<Category>();
		res.add(selected);
		while (cat.getParentCategory() != null) {
			cat = cat.getParentCategory();
			res.add(cat);
		}

		res = reverseList(res);
		return res;
	}

	private static List<Category> reverseList(List<Category> lista) {
		List<Category> res = new ArrayList<>();
		for (int i = 1; i <= lista.size(); i++) {
			res.add(lista.get(lista.size() - i));
		}
		return res;
	}

}
