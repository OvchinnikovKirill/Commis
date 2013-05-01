package Comm4;

import java.awt.*;

public class Main {


    public static void main(  String[] args ) {
        TspAlg A = new TspAlg();
        A.init();
        A.start();
        javax.swing.JFrame window = new javax.swing.JFrame("TspAlg");
        window.setContentPane(A);
        window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        window.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize(screenSize.width,screenSize.height);
        window.setVisible(true);

    }
}
