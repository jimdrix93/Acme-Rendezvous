/**
 * 
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query("select c from Category c where c.parentCategory.id= ?1")
	Collection<Category> getChildCategories(int parentCategoryId);
	
	@Query("select c from Category c where c.parentCategory = null")
	Collection<Category> getFirstLevelCategories();

}
