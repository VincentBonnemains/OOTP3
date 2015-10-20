/**
 * Program execution starts at the first statement in method main. Here,
 *	we establish a specialised frame that will operate as the main
 *	application screen.
 *
 * @author	K Barclay
 */

package collie;

import window.*;



public class Collie {

    public static void			main(String[] args) {
    
        System.out.println("PROGRAM START...");

        CollieFrame.getCollieFrame();
        
        System.out.println("PROGRAM END");
    }

}	// class Collie

// ==================================================================


