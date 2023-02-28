package com.noambechhofer.yaniv;

import java.io.IOException;

import org.junit.Test;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;

public class CardTest {
    @Test
    public void testToImage() throws IOException {
        Card c2 = new Card(FaceValue.TWO, Suit.CLUBS);

        MBFImage image = c2.toImage();

        DisplayUtilities.display(image);
    }
}