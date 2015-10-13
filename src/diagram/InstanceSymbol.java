/**
 *  An instance symbol object represents those appearing in a collaboration
 *	diagram. The property we maintain for such an object is its rectangular
 *	position within the diagram.
 *
 *  @author	K Barclay
 */



package diagram;



import java.awt.*;



public class InstanceSymbol	extends NodeElement {

    public		InstanceSymbol(int upperLeftX, int upperLeftY) {
    	super(upperLeftX, upperLeftY);
    }

    public		InstanceSymbol(Point mousePoint) {
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
    	graphicsContext.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    	
    	if(caption.equals(":") == false) {
    	    int left = bounds.x + (bounds.width - captionWidth) / 2;
    	    int right = left + captionWidth;
    	    int bottom = bounds.y + Y_TEXTOFFSET;
    	    graphicsContext.drawString(caption, left, bottom);
    	    graphicsContext.drawLine(left, bottom + 2, right, bottom + 2);
    	}
    	
    	if(showFlag)
    	    this.drawAttributes(graphicsContext);
    }
    
    
    
    private void		drawAttributes(Graphics graphicsContext) {
    	graphicsContext.setFont(COLLIE_FONT);
    	FontMetrics fontMetrics = graphicsContext.getFontMetrics(COLLIE_FONT);
    	int height = fontMetrics.getHeight();
    	
    	int left = bounds.x + X_TEXTOFFSET;
    	int bottom = bounds.y + Y_TEXTOFFSET + height;
    	
    	int size = attributes.size();
    	for(int k = 0; k < size; k++) {
    	    String attribute = (String)attributes.get(k);
    	    graphicsContext.drawString(attribute, left, bottom);
    	    bottom += height;
    	}
    }
    
    
    
    private int			getMaximumPixelWidth(FontMetrics fontMetrics) {
    	String caption = this.getName() + ":" + this.getClassifier();
    	int captionWidth = fontMetrics.stringWidth(caption);
    	int width = captionWidth + fontMetrics.charWidth('W');
    	
    	if(showFlag) {
    	    int size = attributes.size();
    	    for(int k = 0; k < size; k++) {
    	    	String attribute = (String)attributes.get(k);
    	    	width = Math.max(width, fontMetrics.stringWidth(attribute) + fontMetrics.charWidth('W'));
    	    }
    	}
    	width = Math.max(width, DEFAULTWIDTH);
    	
    	return width;
    }
    
    
    
    private int			getMaximumPixelHeight(FontMetrics fontMetrics) {
    	int height = Y_TEXTOFFSET + 2;
    	
    	if(showFlag)
    	    height += attributes.size() * fontMetrics.getHeight();
    	height = Math.max(height, DEFAULTHEIGHT);
    	    
    	return height;
    }
    
    
    
// ---------- statics -------------------------------------

    private final static int		X_TEXTOFFSET =  5;
    private final static int		Y_TEXTOFFSET = 20;
    
    private final static Font		COLLIE_FONT	= new Font("Arial", Font.PLAIN, 12);
    
}	// class: InstanceSymbol

// ============================================================================
