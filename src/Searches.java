import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class Searches {

    Random random = new Random();
    CSV csvWriter = new CSV(Constants.CSV_DESTINATION_Greedy);
    public void randomSearch(int size, int[][] flowMatrix, int[][] distanceMatrix, int kTimes) throws FileNotFoundException {

        csvWriter.initiateCSVFile();
        ArrayList<Integer> best_permutation_array = randomPermutation(size);
        int lowest_cost = cost(size, best_permutation_array, flowMatrix, distanceMatrix); //= cost zmienic

        for (int i = 0; i < kTimes; i++) {
            ArrayList<Integer> temp_permutation = randomPermutation(size);
            int temp_cost = cost(size, temp_permutation, flowMatrix, distanceMatrix);
            if (temp_cost < lowest_cost) {
                lowest_cost = temp_cost;
                best_permutation_array = temp_permutation;
                printNewBestPermutation(best_permutation_array, lowest_cost);
            }
            csvWriter.addRandomRow(i, lowest_cost);
        }
        csvWriter.closeCSV();
    }

    public void greedySearch(int size, int[][] flowMatrix, int[][] distanceMatrix) throws FileNotFoundException{

        int actualFactories = 2;
        int actualCost;
        int newCost;
        ArrayList<Integer> result_permutation = randomGreedyFirstElements(size, actualFactories, flowMatrix, distanceMatrix);
        actualCost = cost(actualFactories, result_permutation, flowMatrix, distanceMatrix);
        System.out.print("N= " + actualFactories + " | ");
        printNewBestPermutation(result_permutation, actualCost);

        ArrayList<Integer> check_permutation = new ArrayList<Integer>();
        for (++actualFactories; actualFactories <= size; actualFactories++) {

            boolean found = false;
                    /*
                    Find cost to first next avainble permutation ( next n-th element)
                     */
            for (int n = 0; n < size && !found; n++) {
                if (!result_permutation.contains(n + 1)) {
                    result_permutation.add(n + 1);
                    actualCost = cost(actualFactories, result_permutation, flowMatrix, distanceMatrix);
                    found = true;
                }
            }
                    /*
                    Find best permutation for that n-size
                     */
            for (int i = 0; i < size; i++) {
                if (!result_permutation.contains(i + 1)) {
                    check_permutation.clear();
                    check_permutation.addAll(result_permutation);
                    check_permutation.remove(check_permutation.size() - 1);
                    check_permutation.add(i + 1);
                    newCost = cost(actualFactories, check_permutation, flowMatrix, distanceMatrix);
                    if (actualCost > newCost) {
                        result_permutation.clear();
                        result_permutation.addAll(check_permutation);
                        actualCost = newCost;
                    }
                }
            }
            System.out.print("N= " + actualFactories + " | ");
            printNewBestPermutation(result_permutation, actualCost);
        }
        csvWriter.addGreedyRow(actualCost);
    }

    private void printNewBestPermutation(ArrayList<Integer> arrayToPrint, int costToPrint) {
        System.out.print("[");
        for (Integer i : arrayToPrint) {
            System.out.print(i.toString() + " ");
        }
        System.out.println("] --- Cost: " + costToPrint);

    }


    private ArrayList<Integer> randomPermutation(int size) {

        ArrayList<Integer> result_permutation = new ArrayList<>();
        Set<Integer> set = new LinkedHashSet<>();
        while (set.size() < size) {
            set.add(random.nextInt(size) + 1);
        }
        for (Integer i : set) {
            result_permutation.add(i);
        }
        return result_permutation;
    }

    private int cost(int size, ArrayList<Integer> permutation, int[][] flowMatrix, int[][] distanceMatrix) {
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result += (distanceMatrix[i][j] * flowMatrix[permutation.get(i) - 1][permutation.get(j) - 1]);
            }
        }
        return result;
    }


    private ArrayList<Integer> randomGreedyFirstElements(int size, int elements, int[][] flowMatrix, int[][] distanceMatrix) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(random.nextInt(size - 1) + 1);
        boolean firstTry = true;
        int firstBestCost = 0;
        int toAdd = random.nextInt(size - 1) + 1;
        for (int i = 1; i < size + 1; i++) {
            if (!result.contains(i)) {
                result.add(i);
                int tempcost = cost(elements, result, flowMatrix, distanceMatrix);
                if (firstTry) {
                    firstBestCost = tempcost;
                    firstTry = false;
                } else {
                    if (tempcost < firstBestCost) {
                        firstBestCost = tempcost;
                        toAdd = i;
                    }
                }
                result.remove(result.size() - 1);
            }
        }
        result.add(toAdd);
        return result;
    }
}
