package investigator;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by stasenkv on 10/30/2017.
 */
public class InvestigatorTest {

    public static String INPUT_FILE = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "InputFile.txt";
    public static String RESOURCES_DIR = "target" + File.separator + "test-classes" + File.separator + "resources";
    public static String OUTPUT_FILE = "target" + File.separator + "test-classes" + File.separator + "resources" + File.separator + "OutputFile.txt";
    public static String FIRST_MATCH = "The changing word was: George, Naomi";
    public static String SECOND_MATCH = "The changing word was: restaurant, dinner";
    public static String THIRD_MATCH = "The changing word was: Naomi, George";

    @Before
    public void init() throws Exception
    {
        File outputDir = new File(RESOURCES_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    @Test
    public void testSingleWordPatternCheck() throws IOException{
        Investigator investigator = new Investigator();
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);
        investigator.singleWordPatternCheck(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        List<String> lines = readAllLines(OUTPUT_FILE);
        assertTrue("Output is invalid! File must contain 9 lines.", lines.size()==9);
        assertTrue("Algorithm is invalid! There are no match line: " + FIRST_MATCH, lines.contains(FIRST_MATCH));
        assertTrue("Algorithm is invalid! There are no match line: " + SECOND_MATCH, lines.contains(SECOND_MATCH));
        assertTrue("Algorithm is invalid! There are no match line: " + THIRD_MATCH, lines.contains(THIRD_MATCH));
        deleteDir(RESOURCES_DIR);
    }

    private void deleteDir(String path) {
        File dir = new File(path);
        for (File file: dir.listFiles()){
            if (!file.isDirectory())
                file.delete();
        }
        dir.delete();
    }

    private List<String> readAllLines(String filename){
        Path path = Paths.get(filename);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Exception while reading the file " + e);
        }
        return lines;
    }
}
