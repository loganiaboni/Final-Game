package Game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class GoodGuy extends JComponent  {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 7;
	public static final int PIXELS = 50;
	public static final String[] IMAGES = new String[] { "frog.png", "car.png", "lily.png" };
	public static final Color[] COLORS = new Color[] { Color.BLACK, Color.BLUE, Color.GRAY } ;
	public static final int EMPTY = 0;
	public static final int CAR = 1;
	public static final int LILY = 2;
    
    private int [][] grid = new int [WIDTH][HEIGHT];
    BadGuy [] Bad = new BadGuy [7];
    Image [] imgs = new Image [3];
    private int x = 0;
    private int y = 6;
    private boolean dead;
    
	private Image readImage(String filename) {
		Image image = null;
		try {
			image = ImageIO.read(new File(filename));
		}
		catch (IOException e) {
			System.out.println("Failed to load image '" + filename + "'");
			e.printStackTrace();
		}
		return(image);
	}
    
    private void readBadGuy(String file)
    {
        try
        {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int count = 1;
            
            while((line!=null) && (line!="\n") && (line!="\r"))
            {
                BadGuy r = new BadGuy(line);
                Bad [count] = r;
                line = br.readLine();
                count++;
            }
            BadGuy drt = new BadGuy(2);
            Bad [0] = drt;
            Bad [HEIGHT-1] = drt;

        }
        catch (IOException ex)
        {
            System.out.println("File not found!");
        }
    }
    
    public GoodGuy(String filename)
    {
        setPreferredSize(new Dimension(WIDTH * PIXELS,
                                       HEIGHT * PIXELS));
        readBadGuy(filename);
        for(int a=0; a<3; a++)
        {
            imgs[a] = readImage(IMAGES[a]);
        }
        
        
        
    }
    
    public void reset()
    {
        for (int x = 0; x < grid.length; x++)
            for(int y = 0; y < grid[x].length; y++)
                grid[x][y] = EMPTY;
        
        dead = false;
        x = 0;
        y = 6;
        
        repaint();
    }
    
    private void moveBy(int dx, int dy)
    {
        if (   (x+dx >= 0 && x+dx < WIDTH)
            && (y+dy >= 0 && y+dy < HEIGHT))
        {
            x += dx;
            y += dy;
        }
    }
    
    public boolean isWin()
    {
        return (y == 0);
    }
    
    public void checkCollision()
    {
        if((Bad[y]).getType()==1)
        {
            if(grid[x][y]==0)
            {
                dead = true;
            }
        }
        else
        {
            if(grid[x][y]==1)
            {
                dead = true;
            }
        }
    }
	
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for(int a = 0; a < HEIGHT; a++)
        {
            g.setColor(COLORS[(Bad[a]).getType()]);
            g.fillRect(0, a*PIXELS, WIDTH*PIXELS, PIXELS);
            
            for(int i = 0; i<grid.length; i++)
            {
                if(grid[i][a] != 0)
                {
                    g.drawImage(imgs[grid[i][a]], i*PIXELS, a*PIXELS, PIXELS, PIXELS, null);
                }
            }
        }
        g.drawImage(imgs[0], x*PIXELS, y*PIXELS, PIXELS, PIXELS, null);
        
        if(dead)
        {
            g.setColor(Color.WHITE);
            g.drawLine(x*PIXELS, y*PIXELS, (x+1)*PIXELS, (y+1)*PIXELS);
            g.drawLine((x+1)*PIXELS, y*PIXELS, x*PIXELS, (y+1)*PIXELS);
        }
        
    }
    
    public void key(int code)
    {
        if(code == KeyEvent.VK_UP && !dead)
        {
            moveBy(0, -1);
        }
        else if(code == KeyEvent.VK_DOWN && !dead)
        {
            moveBy(0, 1);
        }
        else if(code == KeyEvent.VK_LEFT && !dead)
        {
            moveBy(-1, 0);
        }
        else if(code == KeyEvent.VK_RIGHT && !dead)
        {
            moveBy(1, 0);
        }
        checkCollision();
        repaint();
        
    }
    
    public void tick(int round)
    {
        
        for(int i = 1; i<HEIGHT-1 && !dead; i++)
        {
            if(Bad[i].isTurn(round))
            {
                for(int j = WIDTH-1; j > 0; j--)
                {
                    if(Bad[i].getType()==1)
                    {
                        if(x==j && y==i && grid[j][i]==2)
                        {
                            if(x<WIDTH-1)
                            {
                                moveBy(1, 0);
                            }
                            else
                            {
                                dead = true;
                                return;
                            }
                        }
                    }
                    grid[j][i] = grid[j-1][i];
                    
                }
                if(Bad[i].isAdd())
                {
                    if(Bad[i].getType()==1)
                    {
                        grid[0][i] = 2;
                    }
                    else if(Bad[i].getType()==0)
                    {
                        grid[0][i] = 1;
                    }
                }
                else
                {
                    grid[0][i] = 0;
                }
            }
            
            
        }
        
        
        checkCollision();
        repaint();
        
    }
}


