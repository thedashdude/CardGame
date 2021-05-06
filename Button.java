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

public class Button {

    private String name;
    private boolean enabled;
    private Rectangle spot;
    

    public Button(String name, Rectangle spot, boolean enabled) {
        this.name = name;
        this.spot = spot;
        this.enabled = enabled;
    }

    public boolean isText(String s)
    {
    	return s.equals(name);
    }

    public Rectangle getRect()
    {
    	return spot;
    }
    public Rectangle getRectIfEnabled()
    {
    	if(enabled)
    	{
	    	return spot;
	    }
    	else
    	{
    		return new Rectangle();
    	}
    }
        

    public void draw(Graphics2D g2d) {
    	if(!enabled)
    		return;
    	g2d.setColor(Color.WHITE);
        g2d.fill(spot);
        g2d.setColor(Color.BLACK);
        g2d.draw(spot);
        g2d.drawString(name, spot.x + 5, spot.y + spot.height - 3);
    }
}