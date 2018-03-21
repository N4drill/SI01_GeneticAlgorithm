public  class Constants {
    public static final String DATA_SOURCE_PATH = "datas/had12.dat.txt";

    public static final int BASIC_POPULATION_SIZE = 1000; //100 -> 2500
    public static final int POPULATION_PERCENT_TO_CROSS = 70; //25 -> 75
    public static final int GA_GENERATIONS = 100000; //100 ->2500
    public static final int RANDOM_SEARCH_K = 2000;
    public static final int BASIC_TOUR = 10; // 1 -> 5
    public static final boolean SELECTION_MODE = true;   //TRUE = roulette , //FLASE = tournament
    public static final String CSV_DESTINATION_GA = "graphs20/GA_P"+BASIC_POPULATION_SIZE+"_G"+GA_GENERATIONS+"_Crs"+POPULATION_PERCENT_TO_CROSS+
            "_TOURNAMENT_"+BASIC_TOUR+
            "_-20v1REACHOPT.csv";
    public static final String CSV_DESTINATION_Greedy = "graphs20/Greedy12G10000.csv";
    public static final String CSV_DESTINATION_Search = "graphs20/Search12G100.csv";
}
