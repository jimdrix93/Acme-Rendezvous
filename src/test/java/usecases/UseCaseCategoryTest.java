
package usecases;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.CategoryService;
import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseCategoryTest extends AbstractTest {

	@Autowired
	private CategoryService	categoryService;


	/*
	 * Requerimientos:
	 * 9. Services belong to categories, which may be organised into arbitrary hierarchies. A category
	 * is characterised by a name and a description.
	 * 10. An actor who is not authenticated must be able to:
	 * 1. List the rendezvouses in the system grouped by category.
	 * 11. An actor who is authenticated as an administrator must be able to:
	 * 1. Manage the categories of services, which includes listing, creating, updating, delet-
	 * ing, and re-organising them in the category hierarchies
	 */

	/*
	 * Caso de uso:
	 * Usuario administrador creando categorías
	 */
	@Test
	public void createCategoryTest() {

		System.out.println("-----Create Category test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Create category without categoryParent
				"P1", "admin", "Citas a ciegas", "Citas de ensueño para todos", "", null
			}, {// P2: Create category with categoryParent
				"P2", "admin", "Cita a sordas", "Vente con nosotros!", "category1", null
			},

			//Negative test cases
			{// N1: Anonymous tries create category
				"N1", "", "Citas culturales", "Description Category Test 1", "", IllegalArgumentException.class
			}, {//N2: User tries create category
				"N2", "user1", "Citas de auto-aprendizaje", "Description Category Test 1", "category1", ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCategoryTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Name category
				(String) testingData[i][3], //Description category
				(String) testingData[i][4], //Category Parent Id
				(Class<?>) testingData[i][5]); //Exception class
	}

	protected void templateCreateCategoryTest(final Integer i, final String nameTest, final String username, final String nameCategory, final String descriptionCategory, final String categoryParentId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Category category = this.categoryService.create();
			category.setName(nameCategory);
			category.setDescription(descriptionCategory);
			if (categoryParentId != "") {
				final Category parent = this.categoryService.findOne(super.getEntityId(categoryParentId));
				category.setParentCategory(parent);
			}
			this.categoryService.save(category);

			System.out.println(i + " Create Category: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Category: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario administrador editando categorías
	 */
	@Test
	public void editCategoryTest() {

		System.out.println("-----Edit Category test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Edit category without categoryParent
				"P1", "admin", "category1", "Viajes", "Description Category Test 1", "", null
			}, {// P2: Edit category with categoryParent
				"P2", "admin", "category2", "Quedadas de freakis", "Description Category Test 2", "category1", null
			},

			//Negative test cases
			{// N1: Anonymous tries edit category
				"N1", "", "category1", "Star wars", "Obligado venir disfrazado!", "", IllegalArgumentException.class
			}, {//N2: User tries edit category
				"N2", "user1", "category1", "Star trek", "Todos han de ir en pantalones ajustados", "category1", ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditCategoryTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //CategoryId
				(String) testingData[i][3], //Name category
				(String) testingData[i][4], //Description category
				(String) testingData[i][5], //Category Parent Id
				(Class<?>) testingData[i][6]); //Exception class
	}

	protected void templateEditCategoryTest(final Integer i, final String nameTest, final String username, final String categoryId, final String nameCategory, final String descriptionCategory, final String categoryParentId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Category category = this.categoryService.findOne(super.getEntityId(categoryId));
			category.setName(nameCategory);
			category.setDescription(descriptionCategory);
			if (categoryParentId != "") {
				final Category parent = this.categoryService.findOne(super.getEntityId(categoryParentId));
				category.setParentCategory(parent);
			}
			this.categoryService.save(category);

			System.out.println(i + " Edit Category: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Category: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario administrador borrando categorías
	 */
	@Test
	public void deleteCategoryTest() {

		System.out.println("-----Delete Category test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Delete category1
				"P1", "admin", "category2", null
			}, {// P2: Delete category2
				"P2", "admin", "category1", null
			},

			//Negative test cases
			{// N1: Anonymous tries delete category
				"N1", "", "category1", IllegalArgumentException.class
			}, {//N2: User tries delete category
				"N2", "admin", "category1.1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteCategoryTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //CategoryId
				(Class<?>) testingData[i][3]); //Exception class
	}

	protected void templateDeleteCategoryTest(final Integer i, final String nameTest, final String username, final String categoryId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Category category = this.categoryService.findOne(super.getEntityId(categoryId));
			this.categoryService.delete(category);

			System.out.println(i + " Delete Category: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Category: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

}
