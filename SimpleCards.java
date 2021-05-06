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

public class SimpleCards {

    public static void main(String[] args) {
        new SimpleCards();
    }

    public SimpleCards() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                Deck.INSTANCE.shuffle();
                List<Hand> players = new ArrayList<>(5);
                for (int index = 0; index < 5; index++) {
                    players.add(new Hand());
                }

                for (int index = 0; index < 5; index++) {
                    for (Hand hand : players) {
                        hand.add(Deck.INSTANCE.pop());
                    }
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new GamePane(players));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class Hand {

        private List<Card> cards;

        public Hand() {
            cards = new ArrayList<>(25);
        }

        public void add(Card card) {
            cards.add(card);
        }

        public int size() {
            return cards.size();
        }

        public Iterable<Card> cards() {
            return cards;
        }

        public Iterable<Card> reveresed() {
            List<Card> reversed = new ArrayList<>(cards);
            Collections.reverse(reversed);
            return reversed;
        }
    }

    

    public class GamePane extends JPanel {

        private List<Hand> players;

        private Map<Card, Rectangle> mapCards;

        private Card selected;

        private Rectangle cardReturn = new Rectangle();

        public GamePane(List<Hand> players) {
            this.players = players;
            mapCards = new HashMap<>(players.size() * 5);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected != null) {
                        Rectangle bounds = mapCards.get(selected);
                        bounds.y = cardReturn.y;
                        bounds.x = cardReturn.x;
                        repaint();
                        selected = null;
                    }
                    else
                    {
	                    
	                    for (Card card : players.get(0).reveresed()) {
	                        Rectangle bounds = mapCards.get(card);
	                        if (bounds.contains(e.getPoint())) {
	                            selected = card;
	                            cardReturn.x = bounds.x;
	                            cardReturn.y = bounds.y;

	                            bounds.y = 80;
	                            bounds.x = 170;
	                            repaint();
	                            break;
	                        }
	                    }
	                }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }

        @Override
        public void invalidate() {
            super.invalidate();
            mapCards.clear();
            Hand hand = players.get(0);
            int cardHeight = (getHeight() - 20) / 3;
            int cardWidth = (int) (cardHeight * 0.6);
            int xDelta = cardWidth / 2;
            int xPos = (int) ((getWidth() / 2) - (cardWidth * (hand.size() / 4.0)));
            int yPos = (getHeight() - 20) - cardHeight;
            for (Card card : hand.cards()) {
                Rectangle bounds = new Rectangle(xPos, yPos, cardWidth, cardHeight);
                mapCards.put(card, bounds);
                xPos += xDelta;
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            Hand hand = players.get(0);
            g2d.drawString("Place",160,60);
            g2d.drawRect(160,70,100,150);
            g2d.drawString("Hand",100,230);
            g2d.drawRect(100,240,235,150);
            for (Card card : hand.cards) {
                Rectangle bounds = mapCards.get(card);
                System.out.println(bounds);
                if (bounds != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.fill(bounds);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(bounds);
                    Graphics2D copy = (Graphics2D) g2d.create();
                    paintCard(copy, card, bounds);
                    copy.dispose();
                }
            }
            g2d.dispose();
        }

        protected void paintCard(Graphics2D g2d, Card card, Rectangle bounds) {
            g2d.translate(bounds.x, bounds.y );
            g2d.setClip(0, 0, bounds.width - 5, bounds.height - 5);

            String text = card.getFace().getValue();
            FontMetrics fm = g2d.getFontMetrics();
            AffineTransform at = new AffineTransform();
            at.translate( bounds.width/2 - 16, bounds.height/2 - 16);
            g2d.drawImage(card.getSuit().getIcon(), at, null);

            g2d.drawString(text, 5, fm.getAscent());
        }
    }

}
