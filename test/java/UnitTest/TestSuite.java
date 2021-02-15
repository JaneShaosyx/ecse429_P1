package UnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCategories.class,
        TestCategoriesId.class,
        TestCategoriesIdProjects.class,
        TestCategoriesIdProjectsId.class,
        TestCategoriesIdTodos.class,
        TestCategoriesIdTodosId.class,
})
public class TestSuite {
}  