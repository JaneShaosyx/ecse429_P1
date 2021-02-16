package UnitTest.model;

import java.util.List;

/**
 * Categories read result
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
public class CategoriesReadResult {
    private List<Category> categories;
    private String[] errorMessages;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }
}
