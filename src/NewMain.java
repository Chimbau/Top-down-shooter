
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.lang.Math;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pichau
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double x;
        double r = 0, ima=0;
        double rmax=0, imamax=0;
        int n = 0;
        double[] m = {27.0711, 20, 7.0711, 10, 12.9289, 0, -7.0711, 10};
        for (int i=0; i < 4; i++){
            r = m[i]*cos(2*3.1416*i*1);
            ima = m[i]*sin(2*3.1416*i*1);
            rmax += r;
            imamax += ima;
        }
        x = sqrt(rmax*rmax + imamax*imamax);
        //double raux = 2*Math.PI*1*0.125 ;
        //r = cos(raux);
        
        System.out.println(rmax);
        System.out.println(imamax);
        System.out.println(x);
        
        
    }
    
}
