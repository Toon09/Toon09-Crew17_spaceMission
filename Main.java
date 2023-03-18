import BackEnd.Models.Model;
import BackEnd.Models.simple1;
import BackEnd.NumericalMethods.*;

class Main {

    /*
     * a simple experiment set up to check how things work so far, before improvements
     */
    public static void main(String[] args) {

        

    }

    /* TO DO
     * start GUI
     * start planets and trajectory planning (staying away from other planets)
     * teamchart?
     */



    public void testing(){
        Model simple = new simple1(0, 1);

        double dx = 0.001;

        int size = 100;
        double[][] errors = new double[2][size];

        //test
        System.out.println("approx");
        for(int i=0; i<size; i++){
            errors[0][i] = Eulers._1DegStep(simple, dx);
            //System.out.println(simple.getX() + ", " + errors[0][i]); 
        }
        System.out.println("\n");


        //solution for test
        System.out.println("solution: y(x) = 3* e^x - 2"); 
        //found with walfram alpha
        // https://www.wolframalpha.com/input?i=y%27+%3D+2+%2B+y%2C+y%280%29+%3D+1

        double x = 0;
        for(int i=0; i<size; i++){
            errors[1][i] = 3 * Math.exp(x) - 2;
            //System.out.println( x + ", " + errors[1][i] );
            x+=dx;
        }
        System.out.println("\n");


        //error finding
        x = 0;
        System.out.println("Errors");
        for(int i=0; i<size; i++){
            System.out.println( x + ", " + (errors[1][i] - errors[0][i]) );
            x+=dx;
        }
    }

}
