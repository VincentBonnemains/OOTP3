/**
 *  An actor symbol object represents those appearing in a collaboration
 *	diagram. The property we maintain for such an object is its rectangular
 *	position within the diagram.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;



public class ActorSymbol	extends NodeElement {

    public		ActorSymbol(int upperLeftX, int upperLeftY) {
    	super(upperLeftX, upperLeftY);
    }

    public		ActorSymbol(Point mousePoint) {
    	this(mousePoint.x, mousePoint.y);
    }
    
    
    
    public void		draw(Graphics graphicsContext) {
    	graphicsContext.setFont(COLLIE_FONT);
    	FontMetrics fontMetrics = graphicsContext.getFontMetrics(COLLIE_FONT);
    	
    	String caption = this.getName() + ":" + this.getClassifier();
    	int captionWidth = fontMetrics.stringWidth(caption);
    	bounds.width = this.getMaximumPixelWidth(fontMetrics);
    	bounds.height = this.getMaximumPixelHeight(fontMetrics);
    	
    	Color lineColor = this.isSelected() ? Color.blue : Color.black;
    	graphicsContext.setColor(Color.white);
    	graphicsContext.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    	graphicsContext.setColor(lineColor);
    	
    	int centreLine = (bounds.x + bounds.x + bounds.width) / 2;
    	graphicsContext.drawOval((bounds.x + bounds.x + bounds.width - HEADDIAMETER) / 2, bounds.y, HEADDIAMETER, HEADDIAMETER);
    	graphicsContext.drawLine(centreLine, bounds.y + HEADDIAMETER, centreLine, bounds.y + HEADTOTORSO);
    	graphicsContext.drawLine(centreLine - ARMLENGTH, bounds.y + HEADTONECK, centreLine + ARMLENGTH, bounds.y + HEADTONECK);
    	graphicsContext.drawLine(centreLine, bounds.y + HEADTOTORSO, centreLine - ARMLENGTH, bounds.y + HEADTOTOE);
    	graphicsContext.drawLine(centreLine, bounds.y + HEADTOTORSO, centreLine + ARMLENGTH, bounds.y + HEADTOTOE);
    	
    	if(caption.equals(":") == false) {
    	    int left = bounds.x + (bounds.width - captionWidth) / 2;
    	    int right = left + captionWidth;
    	    int bottom = bounds.y + bounds.height - Y_TEXTOFFSET;
    	    graphicsContext.drawString(caption, left, bottom);
    	    graphicsContext.drawLine(left, bottom + 2, right, bottom + 2);
    	}
    }
    
    
    
    private int			getMaximumPixelWidth(FontMetrics fontMetrics) {
    	String caption = this.getName() + ":" + this.getClassifier();
    	int captionWidth = fontMetrics.stringWidth(caption);
    	int width = captionWidth + fontMetrics.charWidth('W');
    	width = Math.max(width, DEFAULTWIDTH);
    	
    	return width;
    }
    
    
    
    private int			getMaximumPixelHeight(FontMetrics fontMetrics) {
    	String caption = this.getName() + ":" + this.getClassifier();
    	int height = Y_TEXTOFFSET + 2;
    	if(caption.equals(":") == false)
    	    height = HEADTOTOE + Y_TEXTOFFSET + 2;
    	height = Math.max(height, HEADTOTOE);
    	    
    	return height;
    }
    
    
    
// ---------- statics -------------------------------------

    private final static int		X_TEXTOFFSET =  5;
    private final static int		Y_TEXTOFFSET = 20;
    
    private final static Font		COLLIE_FONT	= new Font("Arial", Font.PLAIN, 12);
    
    private static final int		HEADDIAMETER	= 16;
    private static final int		ARMLENGTH	= 10;
    private static final int		TORSOLENGTH	= 25;
    private static final int		LEGLENGTH	= 15;
    private static final int		NECKLENGTH	= 5;
    private static final int		HEADTOTORSO	= HEADDIAMETER + TORSOLENGTH;
    private static final int		HEADTONECK	= HEADDIAMETER + NECKLENGTH;
    private static final int		HEADTOTOE	= HEADDIAMETER + TORSOLENGTH + LEGLENGTH;
    
}	// class: ActorSymbol

// ============================================================================
