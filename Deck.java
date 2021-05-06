import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public enum Deck {

        INSTANCE;

        private List<Card> cards;
        private List<Card> playDeck;

        private Deck() {
            cards = new ArrayList<>(52);
            for (Suit suit : Suit.items) {
                for (Face face : Face.items) {
                    cards.add(new Card(suit, face));
                }
            }
            playDeck = new ArrayList<>(cards);
        }

        public void shuffle() {
            playDeck.clear();
            playDeck.addAll(cards);
            Collections.shuffle(playDeck);
        }

        public Card pop() {
            if (playDeck.isEmpty()) {
                return null;
            }
            return playDeck.remove(0);
        }

        public void push(Card card) {
            playDeck.add(card);
        }

    }
