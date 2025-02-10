package ku_rum.backend.domain.menu.domain.repository;

import ku_rum.backend.domain.menu.domain.Menu;
import ku_rum.backend.domain.menu.response.MenuSimpleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

  @Query("SELECT new ku_rum.backend.domain.menu.response.MenuSimpleResponse(" +
          "m.name, m.price, m.imageUrl) " +
          "FROM Menu m " +
          "WHERE m.category.id = :categoryId")
  List<MenuSimpleResponse> findAllByCategoryId(@Param("categoryId") Long categoryId);
}

