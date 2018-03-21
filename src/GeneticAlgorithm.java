import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class GeneticAlgorithm {
    Random random = new Random();
    FileReader fileReader = new FileReader(Constants.DATA_SOURCE_PATH);
    // CSV csvWriter = new CSV(Constants.CSV_DESTINATION_GA);  4 miejsca w kodzie
    int [][] flowMatrix;
    int [][] distanceMatrix;
    int dimention;

    public void geneticAlgorithm(int population_size) throws FileNotFoundException {
        //region Pobranie danych z FileReader i wpisanie ich w flow i distance
        fileReader.gainData();
       // csvWriter.initiateCSVFile();
        flowMatrix = fileReader.getFlow_matrix();
        distanceMatrix = fileReader.getDistance_matrix();
        dimention = fileReader.getDimention();
        //endregion

        //region Co jest do zrobienia- instukcja
        /*TODO

            1. Wygeneruj populacje w ilosci population_size i policz dla nich koszt
               1a. Wpisz im losową permutację
               1b. Policz dla nich koszt i zapisz wartosc w obiekcie
               1c. Wpisz im wartość size
            2. Wybierz osobnika o najniższym koszcie i go zapisz
            3. Zrób ranking ( nowy ArrayList dla osobników)
                -Pobierz wartość kosztu dla kazdego i zrób z tego wartość 1/koszt
                -1/koszt *100 ilośc osobników do dodania do puli ruletki
                (-Im mniejszy koszt tym więcej osobników będzie w ruletce)
            5. Wybierz losowo z puli ruletki population_size*2 osobników
            6. Crossuj parami osobników biorąc z jednego połowę i z drugiego drugą połowę tworząc nowe dziecko// stworzy się population_size dzieci
            7. Dla każdego nowego dziecka z jakimś prawdopodobieńswem rób mutację tzn. losuj jakąś liczbe od 1-10 i np. próg mutacji to 3 więc jeżeli wylosujesz
                  3-10 to zmutuj to dziecko
            8. Otrzymujesz population_size nowych osobników i wpisujesz randomowo najlepszego osobnika
            9. Sprawdź w wygenerowanej populacji najlepszego osobnika i sprawdź czy ma lepszy koszt od dotychczasowego najlepszego osobnika
                -Jeżeli tak to nadpisz najlepsze rozwiązanie
            10.
                a. Rób tak n razy w pętli
                b. Rób tak jeżeli od 20 obiegów nie dostałeś lepszego wyniku
         */
        //endregion

        ArrayList<Individual> start_population = generatePopulation(population_size); // Wyznacz populacje startowa
        Individual best_individual = findBestIndividual(start_population); //wyznacz najlepszego osobnika
        Individual worst_individual = findWorstIndividual(start_population); //wyznacz najgorszego osobnika
        int avgCost = avgCostOfPopulation(start_population); //sredni koszt dla populacj
        boolean found = false;

        for(int i= 1; found==false || i<Constants.GA_GENERATIONS ; i++)
        {
            System.out.print("STEP "+i+" "+ "BEST: "); best_individual.printIndividual();
            System.out.print("BEST: "+best_individual.getCost() + " | AVG: "+avgCost + " | WORST: "+worst_individual.getCost()+"\n");
            //csvWriter.addRow(i,best_individual.getCost(),avgCost,worst_individual.getCost());
            ArrayList<Individual> winners_population = new ArrayList<>(); // Stworz nowa pule do ktorej bedzie wrzucana pula po operacjach
            winners_population.add(best_individual); //dodaj najlepszego osobnika do nastepnej tury
            ArrayList<Individual> selection;
            if(Constants.SELECTION_MODE==true) selection = rouletteSelection(start_population, Constants.BASIC_POPULATION_SIZE);
            else selection = tournamentSelection(start_population,Constants.BASIC_POPULATION_SIZE, Constants.BASIC_TOUR);
            selection.add(best_individual); //dodaj do selekcji najlepszego osobnika
            ArrayList<Individual> populationToCross = generatePopulationToCross(selection, Constants.POPULATION_PERCENT_TO_CROSS); //wyznacz ile populacji wyselekcjonowanej ma byc przeznaczonych do crossowania
            ArrayList<Individual> crossChilds = crossover(populationToCross, dimention); // skrzyzuj ta czesc populacji
            winners_population.addAll(crossChilds); // dodaj dzieci po krzyzowaniu do puli
            ArrayList<Individual> populationToMutate = generatePopulationToMutate(selection, start_population.size() - winners_population.size()); // wylosuj populacje do mutowania
            ArrayList<Individual> mutateChilds = mutate(populationToMutate, dimention); // zmutuj populacje wygenerowana
            winners_population.addAll(mutateChilds);
            Individual new_best_individual = findBestIndividual(winners_population);
            Individual new_worst_individual = findWorstIndividual(winners_population);
            int new_avgCost = avgCostOfPopulation(winners_population);
            best_individual = new_best_individual;
            worst_individual = new_worst_individual;
            avgCost = new_avgCost;
            start_population = winners_population;
            if(best_individual.getCost()==1652) found=true;
        }
        System.out.print("STEP "+Constants.GA_GENERATIONS+" "+ "BEST: "); best_individual.printIndividual();
        System.out.print("BEST: "+best_individual.getCost() + " | AVG: "+avgCost + " | WORST: "+worst_individual.getCost()+"\n");
        //csvWriter.addRow(Constants.GA_GENERATIONS,best_individual.getCost(),avgCost,worst_individual.getCost()); //dodaj ostatni rekord
        //csvWriter.closeCSV();

    }



    private int avgCostOfPopulation(ArrayList<Individual> population){
        int sum=0;
        for(Individual i : population){
            sum+=i.getCost();
        }
        return sum/population.size();
    }
    private ArrayList<Individual> rouletteSelection(ArrayList<Individual> basic_population, int amount) {
        ArrayList<Individual> roulettePopulation= new ArrayList<>();

        double min = 1.0/ findBestIndividual(basic_population).getCost();
        double max = 1.0/ findWorstIndividual(basic_population).getCost();

        for(Individual i : basic_population)
        {
            double current = (1.0 / i.getCost());
            double normalization= (current - min) / (max-min);
            int howManyToAdd = (int) ((1-normalization)* 100.00);
            for(int j = 0 ; j<howManyToAdd; j++){
                roulettePopulation.add(i);
            }
        }
        ArrayList<Individual> selectedPopulation = new ArrayList<>();
        for(int k = 0 ; k<amount; k++) {
            selectedPopulation.add(roulettePopulation.get(random.nextInt(roulettePopulation.size()-1)));
        }
        return selectedPopulation;
    }
    private ArrayList<Individual> mutate(ArrayList<Individual> population, int dimention ) {
        ArrayList<Individual> mutates_permutation = new ArrayList<>();
        for(Individual i: population)
        {
            Individual mutated = new Individual();
            mutated.setPermutation(mutateIndividual(i.getPermutation()));
           // mutated.setPermutation(betaMutateIndividual(i.getPermutation(),4));
            mutated.setCost(cost_calculate(mutated.getPermutation()));
            mutates_permutation.add(mutated);
        }
        return mutates_permutation;
    }
    private ArrayList<Integer> mutateIndividual(ArrayList<Integer> toMutate) {
        Integer position1= random.nextInt(toMutate.size()-1);
        Integer position2= random.nextInt(toMutate.size()-1);

        Integer factory1 = toMutate.get(position1);
        Integer factory2 = toMutate.get(position2);

        ArrayList<Integer> newMutate = toMutate;
        newMutate.add(position1, factory2);//dodaj na pozycje fac1 factory2
        newMutate.remove(position1+1);//usun to co bylo na tej pozycji
        newMutate.add(position2,factory1);// dodaj na pozycje fac2 factory1
        newMutate.remove(position2+1);// usun to co byla na tej pozycji

        return newMutate;
    }
    private ArrayList<Individual> generatePopulationToMutate(ArrayList<Individual> population, int sizeToFill) {
        ArrayList<Individual> populationToMutate = new ArrayList<>();
        for(int i = 0; i<sizeToFill; i++)
        {
            populationToMutate.add(population.get(random.nextInt(population.size()-1)));
        }
        return populationToMutate;
    }
    private ArrayList<Individual> generatePopulationToCross(ArrayList<Individual> population, int percent) {
        ArrayList<Individual> populationToCross = new ArrayList<>();
        int size = (population.size()*percent)/100;
        if(size%2!=0) size++;
        for(int i=0; i<size; i++)
        {
            populationToCross.add(population.get(random.nextInt(population.size()-1)));
        }
        return populationToCross;
    }
    private ArrayList<Individual> crossover(ArrayList<Individual> parents, int dimention) {

        ArrayList<Individual> childs = new ArrayList<>();
        for(int i=0; i<parents.size(); i++) {
            Individual child = new Individual();
            int crossPoint = dimention/2; //wyznacz miejsce przeciecia jako polowa
            //int crossPoint = random.nextInt(dimention-1); // wyznacz miejsce przeciecia losowo
            child.setPermutation(crossParents(parents.get(i), parents.get(parents.size()-1), dimention, crossPoint)); // wez dwoch rodziców i pomieszaj ich po czym dodaj ta permutacje do dziecka
            child.setCost(cost_calculate(child.getPermutation()));
            childs.add(child);
        }
        return childs;
    }
    private ArrayList<Integer> crossParents(Individual parent1, Individual parent2, int dimention, int crossPoint) {
        ArrayList<Integer> permutation = new ArrayList<>();
        for(int i=0; i<crossPoint; i++)
        {
            permutation.add(parent1.getPermutation().get(i));
            //region to remove
            /*Integer toAdd = parent1.getPermutation().get(i);
            if(!permutation.contains(toAdd)){
                permutation.add(toAdd);
            }*/
            //endregion
        }
        for(int i=0; i<dimention-crossPoint; i++)
        {
            Integer toAdd = parent2.getPermutation().get(i);
            if(!permutation.contains(toAdd)){
                permutation.add(toAdd);
            }
        }
        for(int i=1; i<=dimention;i++)
        {
            if(!permutation.contains(i)){
                permutation.add(i);
            }
        }
        return permutation;
    }
    private ArrayList<Individual> generatePopulation(int amount) {
        ArrayList<Individual> generated_permutation = new ArrayList<>();
        for(int i = 0; i<amount; i++)
        {
            Individual individual_toADD = new Individual(); //stworz osobnika
            ArrayList<Integer> newPermutation = generateRandomPermutation(dimention); // stworz nowa permuacje
            individual_toADD.setPermutation(newPermutation); //ustaw permutacje nowego osobnika na ta wygenerowana
            individual_toADD.setCost(cost_calculate(newPermutation)); // dla wygenerowanej permutacji oblicz koszt i wpisz jako koszt dla osobnika
            generated_permutation.add(individual_toADD);
        }
        return generated_permutation;
    }
    private Individual findBestIndividual(ArrayList<Individual> individuals){
        Individual bestOne = individuals.get(0);
        int bestcost = individuals.get(0).getCost();
         for(Individual i: individuals)
         {
             if(i.getCost()<bestcost)
             {
                 bestOne = i;
                 bestcost = i.getCost();
             }
         }
         return bestOne;
    }
    private Individual findWorstIndividual(ArrayList<Individual> individuals) {
        Individual worstOne = individuals.get(0);
        int worstcost = individuals.get(0).getCost();
        for(Individual i: individuals)
        {
            if(i.getCost()>worstcost)
            {
                worstOne = i;
                worstcost = i.getCost();
            }
        }
        return worstOne;
    }
    private int cost_calculate(ArrayList<Integer> permutation) {
        int result = 0;
        for (int i = 0; i < dimention; i++) {
            for (int j = 0; j < dimention; j++) {
                result += (distanceMatrix[i][j] * flowMatrix[permutation.get(i) - 1][permutation.get(j) - 1]);
            }
        }
        return result;
    }
    private ArrayList<Integer> generateRandomPermutation(int size){
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
    private void printPopulation(ArrayList<Individual> population) {
        int osobnik=1;
        for(Individual individual : population)
        {
            System.out.print("Osobnik "+osobnik+" dane: "); individual.printIndividual();
            System.out.println();
            osobnik++;
        }
    }
    private ArrayList<Integer> betaMutateIndividual(ArrayList<Integer> toMutate, int swappositions) {
        ArrayList<Integer> mutated = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        int placesToSwap = swappositions;
        if(placesToSwap%2!=0) placesToSwap--; // jezeli ilosc miejsc do zmiany niepodzielna przez dwa to odejmij 1
        if(placesToSwap>dimention) placesToSwap=dimention;
        if(placesToSwap<2) placesToSwap = 2;

        while(placesToSwap>0)
        {
            int next = random.nextInt(dimention-1);
            if(!positions.contains(next)) {
                positions.add(next);
                placesToSwap--;
            }
        }
        mutated = toMutate;
        for(int j=0;j<positions.size()-2;j+=2)
        {
            int fac1 = toMutate.get(positions.get(j));
            int fac2 = toMutate.get(positions.get(j+1));

            mutated.add(positions.get(j),fac2);
            mutated.remove(positions.get(j+1));
            mutated.add(positions.get(j+1), fac1);
            mutated.remove(positions.get(j+2));
        }
        return mutated;
    }
    private ArrayList<Individual> tournamentSelection(ArrayList<Individual> basic_population, int amount,int tour){
        ArrayList<Individual> winnersSet = new ArrayList<>();
        for(int i=0; i<amount;i++) {
            Individual competitor = basic_population.get(random.nextInt(basic_population.size() - 1));
                for (int j = 0; j < tour; j++) {
                    Individual competitorChoosed = basic_population.get(random.nextInt(basic_population.size() - 1));
                    competitor = chooseStronger(competitor, competitorChoosed);
                }
            winnersSet.add(competitor);
        }
        return winnersSet;
    }
    private Individual chooseStronger(Individual comp1, Individual comp2){
        return comp1.getCost()>comp2.getCost()? comp2 : comp1;
    }

}
