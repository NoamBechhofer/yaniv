package com.noambechhofer;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	//Create an image
        MBFImage image = new MBFImage(320,70, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.WHITE);
        		        
        //Render some test into the image
        image.drawText("Hello World", 10, 60, HersheyFont.CURSIVE, 50, RGBColour.BLACK);
        
        //Display the image
        DisplayUtilities.display(image);

        File imageFile = new File("C:\\Users\\noamb\\OneDrive\\Programming\\yanivMaven\\yaniv\\src\\main\\resources\\cards\\TWO_CLUBS.png");
        MBFImage card = ImageUtilities.readMBF(imageFile);
        DisplayUtilities.display(card);
    }
}
