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
        ApiTodosCategoriesTest.class,
        ApiTodosTest.class,
        ApiTodosTasksofTest.class,
        TestProjectIDCategories.class,
        TestProjects.class,
        TestProjectsID.class,
        TestProjectsIDTask.class,
        TestProjectsIDTaskID.class
})
public class TestSuite {
}  