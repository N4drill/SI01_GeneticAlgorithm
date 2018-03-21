import java.io.*;

public class CSV{
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    PrintWriter printWriter;
    String path;
    public CSV(String path) {
        this.path = path;
    }

    public void initiateCSVFile() throws FileNotFoundException{
        printWriter = new PrintWriter(path);
        printWriter.write("id;best;avg;worst;opt\n");

    }
    public void addRow(int nrPokolenia, int bestCost, int avgCost, int worstCost)
    {
        String toADD = String.valueOf(nrPokolenia) + ";" + String.valueOf(bestCost) + ";" + String.valueOf(avgCost) + ";" + String.valueOf(worstCost);
        toADD+=";6922";
        toADD+="\n";
        printWriter.write(toADD);
    }

    public void addRandomRow(int id, int lowestCost)
    {
        String toAdd = String.valueOf(id) + ";" + String.valueOf(lowestCost) + "\n";
        printWriter.write(toAdd);
    }

    public void addGreedyRow(int lowestCost)
    {
        String toAdd =String.valueOf(lowestCost)+ "\n";
        printWriter.write(toAdd);
    }

    public void closeCSV(){
       printWriter.close();
    }
}
