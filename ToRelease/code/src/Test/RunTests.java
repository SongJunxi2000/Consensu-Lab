package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.internal.TextListener;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MVTestCases.class,
        DSTestCases.class,
        DSAttackTestCases.class,
})
public class RunTests {
    public static void main(String[] args) {
        JUnitCore runner = new JUnitCore();
        runner.addListener(new TextListener(System.out));
        Result r;
        if (args.length == 0) { // Run all tests
            r = runner.run(RunTests.class);
            return;
        }
        switch (args[0]) {
            case "Majority_Vote_Adversary":
                r = runner.run(MVTestCases.class);
                break;
            case "Student_Player":
                r = runner.run(DSTestCases.class);
                break;
            case "Dolev_Strong_FRound_Adversary":
                r = runner.run(DSAttackTestCases.class);
                break;
            default:
                System.out.println("Invalid test suite");
                return;
        }
    }
}
