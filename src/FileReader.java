import java.io.*;
import java.util.Scanner;

public class FileReader {
    private String path="";
    private int dimention;
    private int [][] flow_matrix;
    private int [][] distance_matrix;

    public FileReader(String path){
        this.path= path;
    }

    public  void gainData() throws FileNotFoundException {
        try{
            Scanner scanner = new Scanner(new File(path));
            dimention = scanner.nextInt();

            flow_matrix = new int[dimention][dimention];
            distance_matrix = new int[dimention][dimention];


            for(int i = 0 ; i<dimention ; i++)
            {
                for(int j = 0; j<dimention ; j++)
                {
                    flow_matrix[i][j] = scanner.nextInt();
                }
            }

            for(int ii = 0 ; ii<dimention ; ii++)
            {
                for(int jj = 0; jj<dimention ; jj++)
                {
                    distance_matrix[ii][jj] = scanner.nextInt();
                }
            }


        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  int [][] getFlow_matrix () { return flow_matrix; }
    public  int [][] getDistance_matrix () { return distance_matrix; }
    public  int getDimention() { return dimention; }
}
