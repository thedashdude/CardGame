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


    enum Suit {
        CLUBS(0), DIAMONDS(1), HEARTS(2), SPADES(3);

        private int value;
        private BufferedImage icon;

        private Suit(int value) {
            this.value = value;
            String iconPath = "/images/";
            if(value == 0)
            {
            	iconPath += "club_suit.png";
            }
            if(value == 1)
            {
            	iconPath += "diamond_suit.png";
            }
            if(value == 2)
            {
            	iconPath += "heart_suit.png";
            }
            if(value == 3)
            {
            	iconPath += "spade_suit.png";
            }

            try{
	        	icon = ImageIO.read(getClass().getResource(iconPath));
	        }catch(Exception e){e.printStackTrace();}
	        
        }

        public int getValue() {
            return value;
        }
        public BufferedImage getIcon()
        {
        	return icon;
        }



        public static Suit[] items = new Suit[]{
            CLUBS, DIAMONDS, HEARTS, SPADES
        };
    }