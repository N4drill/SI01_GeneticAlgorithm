import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class VisualTester {
    public static void main (String [] args) throws FileNotFoundException{

        //region Description
       FileReader fileReader = new FileReader(Constants.DATA_SOURCE_PATH);
        fileReader.gainData();
        int [][] flowMatrix = fileReader.getFlow_matrix();
        int [][] distanceMatrix = fileReader.getDistance_matrix();
        int dimention = fileReader.getDimention();
        //endregion

        Searches searches = new Searches();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        //region Description
       // System.out.println("------SEARCH------");
        //searches.randomSearch(dimention,flowMatrix,distanceMatrix,Constants.GA_GENERATIONS);
        /*System.out.println("\n------GREEDY------");
        searches.csvWriter.initiateCSVFile();
        for(int i=0; i<Constants.GA_GENERATIONS; i++) {
            searches.greedySearch(dimention, flowMatrix, distanceMatrix);
        }
        searches.csvWriter.closeCSV();*/
        //endregion
        System.out.println("\n------GENETIC------");
        geneticAlgorithm.geneticAlgorithm(Constants.BASIC_POPULATION_SIZE);
    }






    private void printDistanceMatrix(int dimention, int [][]distanceMatrix)
    {
        System.out.println("Distance Matrix: "+dimention);
        for(int i = 0 ; i<dimention; i++)
        {
            for(int j = 0 ; j<dimention; j++)
            {
                System.out.print(distanceMatrix[i][j]+ " ");
            }
            System.out.println();
        }
    }

    private void printFlowMatrix(int dimention, int [][]flowMatrix)
    {
        System.out.println("Distance Matrix: "+dimention);
        for(int i = 0 ; i<dimention; i++)
        {
            for(int j = 0 ; j<dimention; j++)
            {
                System.out.print(flowMatrix[i][j]+ " ");
            }
            System.out.println();
        }
    }

    private void printDimention(int dimention){ System.out.println("Dimention: "+dimention);}
}
