package ku_rum.backend.global.dataInit;

import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.domain.category.domain.model.CategoryType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CategoryInitializer {
    public static ArrayList<Category> initialize() {
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(Category.of(CategoryType.친구.getText()));
        categories.add(Category.of(CategoryType.단과대.getText()));
        categories.add(Category.of(CategoryType.케이큐브.getText()));
        categories.add(Category.of(CategoryType.케이허브.getText()));
        categories.add(Category.of(CategoryType.편의점.getText()));
        categories.add(Category.of(CategoryType.레스티오.getText()));
        categories.add(Category.of(CategoryType.카페1984.getText()));
        categories.add(Category.of(CategoryType.학생식당.getText()));
        categories.add(Category.of(CategoryType.학과사무실.getText()));
        categories.add(Category.of(CategoryType.기숙사.getText()));
        categories.add(Category.of(CategoryType.은행.getText()));
        categories.add(Category.of(CategoryType.우체국.getText()));


        return categories;
    }
}
