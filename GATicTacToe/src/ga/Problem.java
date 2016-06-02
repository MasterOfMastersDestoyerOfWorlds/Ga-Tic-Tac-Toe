package ga;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author benji
 */
public abstract class Problem{
    
    public abstract boolean equalsProblem(Problem problem);

    public abstract Problem[] getAllProblems(int thoroughness, boolean repeats);
    
    public void save(Problem problem, File file) throws IOException {
        String json = new Gson().toJson(problem);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
            fileWriter.flush();
        }
    }

    public void save(Problem[] problems, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Problem problem : problems) {
                fileWriter.write(new Gson().toJson(problem) + "\n");
            }
            fileWriter.flush();
        }
    }
    
    public Problem[] getProblemsFromFile(File file, Class<? extends Problem> type) throws FileNotFoundException, IOException {
        List<Problem> problems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try{
                    problems.add(new Gson().fromJson(line, type));
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return (Problem[]) problems.toArray(new Problem[0]);
    }
}
