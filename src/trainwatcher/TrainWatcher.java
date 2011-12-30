/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainwatcher;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ovoloshchuk
 */
public class TrainWatcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrainClient tc = new TrainClient("setekh", "qd4Q2eGF");
        try {
            System.out.println(tc.login());
            tc.getCities("Львів");
        } catch (IOException ex) {
            Logger.getLogger(TrainWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
