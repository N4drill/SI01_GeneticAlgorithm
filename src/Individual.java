import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Individual {
    Random random = new Random();
    private ArrayList<Integer> permutation;
    private int cost;

    public Individual(){
        //czysty osobnik
    };

    public ArrayList<Integer> getPermutation() {
        return permutation;
    }

    public void setPermutation(ArrayList<Integer> permutation) {
        this.permutation = permutation;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void printIndividual() {
        ArrayList<Integer> arrayToPrint = permutation;
        int costToPrint = cost;
        System.out.print("[");
        for (Integer i : arrayToPrint) {
            System.out.print(i.toString() + " ");
        }
        System.out.println("] --- Cost: " + costToPrint);
    }




    //region Potencjalne do usuniecia
    /*
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

*//*
    public int cost(int size, ArrayList<Integer> permutation, int[][] flowMatrix, int[][] distanceMatrix) {
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result += (distanceMatrix[i][j] * flowMatrix[permutation.get(i) - 1][permutation.get(j) - 1]);
            }
        }
        return result;
    }*/
    //endregion



}
