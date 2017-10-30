package investigator;

/**
 * Created by stasenkv on 10/29/2017.
 */
public class InvestigatorMain {
    private static final String INPUT_FILE = "C:\\test\\InputFile.txt";
    private static final String OUTPUT_FILE = "C:\\test\\OutputFile.txt";

    public static void main(String[] args) {
        Investigator investigator = new Investigator();
        if (args.length == 2){
            System.out.println("Input file - " + args[0]);
            System.out.println("Output file - " + args[1]);
            investigator.singleWordPatternCheck(args[0], args[1]);
        } else {
            System.out.println("Arguments size is not 2, using default values:");
            System.out.println("Input file - " + INPUT_FILE);
            System.out.println("Output file - " + OUTPUT_FILE);
            investigator.singleWordPatternCheck(INPUT_FILE, OUTPUT_FILE);
        }
    }
}
