import com.goldasil.pjv.models.RandomPlayer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestMain {

    private static final Logger logger = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) {

        logger.debug("TestMain method has started.");

        //Result result = JUnitCore.runClasses(MoveStateHandlerTest.class);
        Result result = JUnitCore.runClasses(RandomPlayerTest.class);
        writeToFile(result, RandomPlayerTest.class.toString());


    }


    private static void writeToFile(Result result, String testedClassName){
        File file = new File("src/main/testResults/" + testedClassName + ".txt");
        FileWriter fr = null;
        BufferedWriter br = null;
        try{
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            br.write("Tested class name: " + testedClassName + ".\n\n");
            for (Failure failure : result.getFailures()) {
                br.write(failure.toString() + "\n\n");
            }
            br.write("\nNumber of all tests: " + result.getRunCount() + "\nNumber of error tests: " + result.getFailures().size());

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
