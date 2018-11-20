

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalfilter;

import java.math.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krodg
 */
public class DigitalFilter {

    final private int SL = 44100;

    public DigitalFilter()
    {
    }
    /**
     * @param args the command line arguments
     */


        public void applyFilter() throws FileNotFoundException, IOException
        {
            Double inputSample;
            //ArrayList<Double> filteredSignal = new ArrayList<>();
            //input from user
            int A = 5; //1-10
            int D = 5; //1-10
            int S = 5; //1-10
            int R = 5; //1-10
            

            //depending on value entered, adjust value to form used by filter
            int aA = A * 110;  //1/400th - 10/400th of a second
            int aD = D * 220;  //1/200th - 10/200ths of a second = 
            int aS = S * 4410; //1/10th - 1 second
            int aR = R * 220;  //1/100th - 10-100ths of a second
            
            System.out.print(A + " " + D + " " + S + " " + R + '\n');
            System.out.print(aA + " " + aD + " " + aS + " " + aR + '\n');
            
            String temps1, temps2;
            double tempd;
            int j = 0;
            
            
            Scanner scan = new Scanner(new FileReader("adsrtest.dat"));
            scan.useDelimiter("0");
            
            FileWriter output = new FileWriter("adsroutput.dat");
            
            //print file imperatives
            output.write(scan.nextLine());
            output.write('\n');
            output.write(scan.nextLine());
            output.write('\n');
            


            for (int i = 0; i < SL; i++)
            {
                
                temps1 = (scan.nextLine());
                //get sample line
                
                //get time
                temps2 = temps1.substring(0, 17);
                //write time
                output.write(temps2 + " ");
                //get sample amplitude
                
                temps2 = temps1.substring(18);
                
                temps1 = temps2.trim();
                //System.out.print(temps1);
                //System.out.print('\n');
                //write time    
                
                //convert string to double and store as input sample
                tempd = new Double(temps1);
                inputSample = tempd;
                
                
                double x;
               // filteredSample = (BigDecimal.valueOf(j/aA) * BigDecimal.valueOf(inputSample)));
                
                //get value to multiply by the incoming sample based on time
                //attack
                if (i < aA)
                {
                    x = new Double(new Double(j)/new Double(aA));

                    j++;
                    
                    if (j + 1 == aA)
                    {
                        j = aD;
                    }
                }
                    //decay
                    else if((i > aA) && (i < (aA+aD)))
                    {
                    //filteredSample = (((0.5 + (j/aD)*0.5)
                    x = new Double((new Double(0.8)) + new Double((new Double(j)/new Double(aD))*new Double(0.5)));
                           // *(inputSample)));
                    //System.out.print(filteredSignal.get(i) + '\n');
                    j--;
                        if ((j - 1) == 0)
                        {
                        j = S;
                        }
                    }
                        //sustain
                        else if((i > (aA+aD)) && (i < (aA+aD+aS)))
                        {
                            x = new Double((new Double(0.2) - (new Double(new Double(2)/(new Double(j*j))))));
                              //  *(inputSample));
                            j--;
                            if ((j - 1) == 0)
                            {
                            j = aR;
                            }
                        }
                            //release 
                            else if(i > (aA+aD+aS))
                            {
                                //needs work - does not drop in amplitude
                                x = new Double(new Double(0.10) - ((new Double(1)/(new Double(j))) * new Double(0.10)));
                               //filteredSample = (((1/4 - ((1/(aR - j))*(0.25)))
                                  //   *(inputSample)));
                               
                               j--;
                            }
                        else
                            { x = new Double(0.0);}
                String os;
                
                //if 
                if (i < aA+aD+aS+aR)
                {
                    double filteredSample = new Double(inputSample * x);
                    String temps = Double.toString(filteredSample);
                    if (filteredSample < 0)
                    {os = temps.substring(0, Math.min(temps.length(), 16));}
                    else {os = temps.substring(0, Math.min(temps.length(), 15));}
                }
                else
                    {
                        os = "             0.0";
                    }
              
              output.write(os);
              output.write(" \n");

            }
            
            output.close();


        }
        
        public static void main(String[] args) throws FileNotFoundException 
    {
        DigitalFilter DF = new DigitalFilter();
        try {
            //generateSignal();
            DF.applyFilter();
        } catch (IOException ex) {
            Logger.getLogger(DigitalFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
