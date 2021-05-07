//Daschel Cooper and Michael Connelly
//5/6/2021

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


    //Initialize the program
    public SimpleCards() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Card Game!");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new GamePane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    //Holds a hand of cards, just a usefull wrapper for a List<Card>
    public class Hand {

        public List<Card> cards;

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

    
    //The actual game logic is here in GamePane
    public class GamePane extends JPanel {

    	//Hold a list of Hands for the player
    	//In this version it only holds one, but were the game expanded this could easily grow 
        private List<Hand> players;

        //The list of buttons in the game
        private List<Button> buttons;

        //A map collecting where all of the Cards are located
        private Map<Card, Rectangle> mapCards;

        //The current card selected
        private Card selected;

        //The current card at the top of the Place pile
        private Card scoringCard;
        private Rectangle scoringRect;

        //Some game variables
        private int score = 0;
        private int handsLeft = 4;
        private boolean done = false;

        //The location in the players hand that the currently selected card should return to if they don't finalize it.
        private Rectangle cardReturn = new Rectangle();

        //The background images
        //Originally a spinoff of the pythagorean fractal program, but generating fractals all the time gets slow so I just used a picture of the final fractal
        private BufferedImage bgImage;
        private BufferedImage tutImage;

        //If the help screen is showing
        private boolean inHelp = false;

        //Submits a card, removing it from the hand adding the appropriate score.
        private void submitCard(Card c)
        {
        	if(scoringCard != null){
	        	if(scoringCard.getSuit() == c.getSuit())
	        	{
	        		score+=1;
	        	}
	        	if(scoringCard.getFace().getInt() < c.getFace().getInt())
	        	{
	        		score+=2;
	        	}
	        	if(scoringCard.getFace().getInt() > c.getFace().getInt())
	        	{
	        		score+=1;
	        	}
			}

        	scoringCard = c;
        	scoringRect = mapCards.get(c);
        	scoringRect.x = scoringRect.x - 5;
        	players.get(0).cards.remove(c);
        	mapCards.remove(c);

        	if(players.get(0).size() == 0 && handsLeft > 0)
        	{
        		drawFiveCards();
        		handsLeft--;
        	}
        	else if(players.get(0).size() == 0 && handsLeft == 0)
        	{
        		done = true;
        	}
        }

        //The constructor initializes the game, creates the buttons, and loads the images for the background and help screen.
        public GamePane() {
        	
        	try{
	        	bgImage = ImageIO.read(getClass().getResource("/images/bg.png"));
	        	tutImage = ImageIO.read(getClass().getResource("/images/help.png"));
	        }catch(Exception e){e.printStackTrace();}

        	buttons = new ArrayList<>();
        	buttons.add(new Button("RESET",new Rectangle(10,10,60,20), true));
        	buttons.add(new Button("HELP",new Rectangle(10,40,60,20), true));
        	initializeGame();

            


            
        	//Describes the logic for user interaction with the mouse
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	//If on the hel screen, close it
                	if(inHelp)
                	{
                		inHelp = false;
                		repaint();
                		return;
                	}

                	//Otherwise check the buttons
                	for(Button b : buttons)
                	{
                		if(b.getRect().contains(e.getPoint()))
                		{
                			if(b.isText("RESET"))
                			{
                				initializeGame();
                				selected = null;
                			}
                			if(b.isText("HELP"))
                			{
                				inHelp = true;
                			}

                			repaint();
                			return;
                		}

                	}

                	//Finally see if a card needs to be changed or selected
                    if (selected != null) {
                    	Card newSelected = null;
                    	for (Card card : players.get(0).reveresed()) {
	                        Rectangle bounds = mapCards.get(card);
	                        if (bounds.contains(e.getPoint())) {
	                            newSelected = card;
	                        }
	                    }
	                    if(newSelected == selected)
	                    {
	                    	submitCard(selected);
	                    	repaint();
	                    	selected = null;
	                    	return;
	                    }

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
	                            bounds.x = 175;
	                            repaint();
	                            break;
	                        }
	                    }
	                }
                }
            });
        }

        //Not draw as in paint, but as in load five cards from the shuffled deck into the hand
        private void drawFiveCards()
        {
        	for (int index = 0; index < 5; index++) {
                Hand hand = players.get(0);
                hand.add(Deck.INSTANCE.pop());
            }
            setMapCards();
        }

        //Resets the game
        private void initializeGame()
        {
        	scoringCard = null;
        	scoringRect = null;
        	score = 0;
        	handsLeft = 4;
        	done = false;
        	Deck.INSTANCE.shuffle();
            players = new ArrayList<>(1);
            players.add(new Hand());

            for (int index = 0; index < 5; index++) {
                Hand hand = players.get(0);
                hand.add(Deck.INSTANCE.pop());
                
            }

            mapCards = new HashMap<>(players.size() * 5);
            setMapCards();
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }

        //Set the cardMap
        private void setMapCards() {
        	int h = 400;
            mapCards.clear();
            Hand hand = players.get(0);
            int cardHeight = (h - 20) / 3;
            int cardWidth = (int) (cardHeight * 0.6);
            int xDelta = cardWidth / 2;
            int xPos = (int) ((h / 2) - (cardWidth * (hand.size() / 4.0)));
            int yPos = (h - 20) - cardHeight;
            for (Card card : hand.cards()) {
                Rectangle bounds = new Rectangle(xPos, yPos, cardWidth, cardHeight);
                mapCards.put(card, bounds);
                xPos += xDelta;
                System.out.println(bounds + "oof");
            }
        }

        //Draw the graphics
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            //Background
            AffineTransform at = new AffineTransform();
            g2d.drawImage(bgImage, at, null);

            //UI
            Hand hand = players.get(0);
            g2d.drawString("Place",160,60);
            g2d.drawRect(160,70,100,150);
            g2d.drawString("Hand",100,230);
            g2d.drawRect(100,240,235,150);

            g2d.drawString("Score: " + score,50,100);
            g2d.drawString("Hands: " + handsLeft,50,150);

            if(done)
            {
            	g2d.drawString("Final Score: " + score,160,315);
            }

            //Place pile
            if(scoringCard != null)
            {
            	Rectangle bounds = scoringRect;
                System.out.println(bounds);
                if (bounds != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.fill(bounds);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(bounds);
                    Graphics2D copy = (Graphics2D) g2d.create();
                    paintCard(copy, scoringCard, bounds);
                    copy.dispose();
                }
            }

            //Cards
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

            //Highlight
            if(selected != null)
            {
            	g2d.setColor(Color.RED);
            	g2d.drawRect(160,70,100,150);
            }

            //Buttons
            for(Button b : buttons)
            {
            	b.draw(g2d);
            }

            //Help
            if(inHelp)
            {
            	g2d.drawImage(tutImage, at, null);
            }

            g2d.dispose();
        }


        //Draw card in the rectangle bounds
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
