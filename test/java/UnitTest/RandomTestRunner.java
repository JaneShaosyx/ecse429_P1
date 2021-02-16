package UnitTest;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTestRunner extends BlockJUnit4ClassRunner {

    public RandomTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = super.computeTestMethods();
        List<FrameworkMethod> newMethods = new ArrayList<>(methods);
        Collections.shuffle(newMethods,new Random(100));
        return newMethods;
    }
}