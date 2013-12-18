/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.database.DaoDataFile;
import br.edu.utfpr.cm.antinsa.service.googledrive.DataFile;
import br.edu.utfpr.cm.antinsa.util.HashGenerator;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author junior
 */
public class OtherTests {

    public static void main(String[] args) {
        try {
            //        String s1 =  "tete.txt~";
            //        String s2 =  ".tete.txt";
            //        char c1 = s2.charAt(s2.length() - 1);
            //        char c2 = s2.charAt(0);
            //        if (c1 == '~' || c2 == '.') {
            //            System.out.println(false);
            //        }
            //       
            //File f = new File("/home/junior/novo.txt");
            //        System.out.println(f.toURI());
            //        System.out.println(f.hashCode());
            //        GoogleDrive.list();
            //        GoogleDriveLocalController c1 = new GoogleDriveLocalController();
            //  
            //        t1.start();
            //        t1.start();
            
        DaoDataFile d = new DaoDataFile();
            List<DataFile> listAll = d.listAll();
                        for (DataFile dataFile : listAll) {
                            System.out.println(dataFile.toString());
                        }
//          d.dataFileExists("tetsst");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OtherTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(OtherTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
