package com.noambechhofer.yaniv;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Test;

public class CardTest {
    @Test
    public void testToUnicodeCodePoint() {
        List<Card> cards = new ArrayList<>(54);

        for (Rank r : Rank.values())
            for (Suit s : Suit.values())
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }

        int i = 1;
        for (Card c : cards)
            System.out.printf("%d: %s = %X\n", i++, c, c.toUnicodeCodePoint());
    }

    @Test
    public void testToImage() throws InterruptedException {
        List<Card> cards = new ArrayList<>(54);

        // populate deck
        for (Rank r : Rank.values())
            for (Suit s : Suit.values())
                try {
                    cards.add(new Card(r, s));
                } catch (RuntimeException e) {
                    continue;
                }

        JFrame frame = new JFrame("card toImage() tester");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // display each card, one second each
        JLabel label = null;
        for (Card c : cards) {
            if (label != null)
                frame.remove(label);

            c.flip();
            Image img = c.toImage();
            ImageIcon icon = new ImageIcon(img);
            label = new JLabel(icon);

            frame.add(label, BorderLayout.CENTER);
            frame.pack();

            System.out.println("displaying " + c);

            Thread.sleep(500);
        }
        
        // now test the cardback

        Card c = cards.get(0);
        c.flip();

        frame.remove(label);
        Image img = c.toImage();
        ImageIcon icon = new ImageIcon(img);
        label = new JLabel(icon);

        frame.add(label, BorderLayout.CENTER);
        frame.pack();

        System.out.println("displaying cardback");
        Thread.sleep(500);
    }
}
