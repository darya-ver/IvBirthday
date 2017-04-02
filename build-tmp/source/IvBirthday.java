import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class IvBirthday extends PApplet {



private static final int NUM_ROWS = 4;
private static final int NUM_COLS = 4;
private static final int NUM_ORIGINAL_BOXES = 2; 

private int score = 0;

private MSButton[][] buttons;

private ArrayList <MSButton> numberedSquares = new ArrayList <MSButton>();

public boolean gameStarted = true;
public boolean instructions = false;
public boolean playGame = false;
public boolean gameIsOver = false;

public PImage ivPhoto1;
public PImage ivPhoto2;
public PImage ivPhoto3;
public PImage ivPhoto4;
public PImage ivPhoto5;
public PImage ivPhoto6;
public PImage ivPhoto7;
public PImage ivPhoto8;
public PImage ivPhoto9;
public PImage ivPhoto10;
public PImage ivPhoto11;
public PImage ivPhoto12;
public PImage ivPhoto13;
public PImage birthdaySignPhoto;
public PImage emmaPhoto;
public PImage pugPhoto;
public PImage instructionsPhoto;

public GameButton start = new GameButton(1000/2-160, 550, "start");
public GameButton restart = new GameButton(1000/2-160, 700/2-60, "restart");

public void setup ()
{
    
    textAlign(CENTER,CENTER);

    ivPhoto1 = loadImage("Data/imgIv1.PNG");
    ivPhoto2 = loadImage("Data/imgIv2.PNG");
    ivPhoto3 = loadImage("Data/imgIv3.PNG");
    ivPhoto4 = loadImage("Data/imgIv4.PNG");
    ivPhoto5 = loadImage("Data/imgIv5.PNG");
    ivPhoto6 = loadImage("Data/imgIv6.JPG");
    ivPhoto7 = loadImage("Data/imgIv7.JPG");
    ivPhoto8 = loadImage("Data/imgIv8.PNG");
    ivPhoto9 = loadImage("Data/imgIv9.PNG");
    ivPhoto10 = loadImage("Data/imgIv10.jpg");
    ivPhoto11 = loadImage("Data/imgIv11.PNG");
    ivPhoto12 = loadImage("Data/imgIv12.PNG");
    ivPhoto13 = loadImage("Data/imgIv13.JPG");
    birthdaySignPhoto = loadImage("Data/birthdayBan.JPG");
    emmaPhoto = loadImage("Data/emmaStone.jpg");
    pugPhoto = loadImage("Data/pugImg.PNG");
    instructionsPhoto = loadImage("Data/instructionsImg.png");

    // make the manager
    Interactive.make(this);
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];

    for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
            buttons[r][c] = new MSButton(r,c);

    imageMode(CENTER);
    setFirstNums();
}

public void draw ()
{
    background(200,200,200);

    if(gameStarted){startScreen();}
    else if(instructions){instructionsFunc();}
    else if(playGame){gamePlay();}
    else if(gameIsOver){endGame();}
}

public void mousePressed()
{
    if(gameStarted)
    {
        if(start.inButton())
        {
            instructionsFunc();
        }
    }
    else if(instructions)
    {
        if(start.inButton())
        {
            gamePlay();
        }
    }
    else if(gameIsOver)
    {
        if(restart.inButton())
        {
            restartNumbers();
            setFirstNums();
            playGame = true;
            score = 0;
        }
    }
}

public void keyPressed()
{
    for(int c = 0; c<NUM_COLS; c++)
            for(int r = 0; r<NUM_ROWS; r++)
                buttons[r][c].setCombinedBoolean(false);

    if(key==CODED)
    {
        if(keyCode == LEFT && !areTheyStuckLeft())
        {
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeToLeft();
            addAnotherNumber();
        }
        else if(keyCode == RIGHT && !areTheyStuckRight())
        {
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeToRight();
            addAnotherNumber();
        }
        else if(keyCode == UP && !areTheyStuckUp())
        {
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeUp();
            addAnotherNumber();
        }
        else if(keyCode == DOWN && !areTheyStuckDown())
        {
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeDown();
            addAnotherNumber();
        }
    }
}

//classes
public class MSButton
{
    private int r, c, myValue;
    private float x,y, width, height;
    private boolean alreadyCombinedOnce, isStuck, up, down, left, right;
    private PImage faceImage;
    
    public MSButton ( int rr, int cc )
    {
        width = 500/NUM_COLS;
        height = 500/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width + 250;
        y = r*height;
        myValue = 0;
        alreadyCombinedOnce = false;

        Interactive.add( this ); // register it with the manager
    }

    public int getValue(){return myValue;}

    public void setValue(int v){myValue = v;}

    public void setCombinedBoolean(boolean ss){alreadyCombinedOnce = ss;}

    public void draw () 
    {    
    	if(playGame)
    	{
	        noStroke();

            if(myValue == 0){fill(204,192,178);rect(x,y,width,height);}

	        if(myValue == 2){faceImage = ivPhoto8;}
	        else if(myValue == 4){faceImage = ivPhoto2;}
            else if(myValue == 8){faceImage = ivPhoto10;}
	        else if(myValue == 16){faceImage = ivPhoto1;}
	        else if(myValue == 32){faceImage = ivPhoto4;}
	        else if(myValue == 64){faceImage = ivPhoto3;}
	        else if(myValue == 128){faceImage = ivPhoto5;}
	        else if(myValue == 256){faceImage = ivPhoto6;}
	        else if(myValue == 512){faceImage = ivPhoto7;}
	        else if(myValue == 1024){faceImage = ivPhoto9;}
	        else if(myValue == 2048){faceImage = ivPhoto11;}
	        else if(myValue == 4096){faceImage = ivPhoto12;}
            else if(myValue == 8192){faceImage = ivPhoto13;}
	        else{fill(204,192,178);}

            if(myValue > 0){image(faceImage, x+width/2,y+height/2,width,height);}
	        
	        fill(185,173,159);

            //edges of the boxes
	        quad(x, y, x+4, y, x+4, y+height-4, x, y+height);
	        quad(x, y, x+width, y, x+width-4, y+4, x, y+4);
	        quad(x+4, y+height-4, x+width, y+height-4, x+width, y+height, x, y+height);
	        quad(x+width-4, y+4, x+width, y, x+width, y+height, x+width-4, y+height);

	        if(myValue > 0)
	        {
                //value text
                fill(255);
	            textSize(35);
	            text("" + myValue, x+width/2, y+height/2-3);            
	        }
	        else 
	        {
	            
	        }
	    }
    }

    public void swipeToRight()
    {
	        if(myValue > 0 && isValid(r,c+1) && buttons[r][c+1].getValue() == myValue && alreadyCombinedOnce == false)
	        {
	            alreadyCombinedOnce = true;
	            buttons[r][c+1].setValue(myValue + myValue);
	            score += myValue*2;
	            setValue(0);
	            buttons[r][c+1].setCombinedBoolean(true);
	            buttons[r][c+1].swipeToRight();
	        }
	        else if(myValue > 0 && isValid(r,c+1) && buttons[r][c+1].getValue() == 0)
	        {
	            buttons[r][c+1].setValue(myValue);
	            setValue(0);
	            buttons[r][c+1].swipeToRight();
	        }
    }

    public void swipeToLeft()
    {

    	if(myValue >0 && isValid(r,c-1) && buttons[r][c-1].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r][c-1].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r][c-1].setCombinedBoolean(true);
            buttons[r][c-1].swipeToLeft();
        }
        else if(myValue > 0 && isValid(r,c-1) && buttons[r][c-1].getValue() == 0)
        {
            buttons[r][c-1].setValue(myValue);
            setValue(0);
            buttons[r][c-1].swipeToLeft();
        }
    }

    public void swipeUp()
    {
        if(myValue > 0 && isValid(r-1,c) && buttons[r-1][c].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r-1][c].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r-1][c].setCombinedBoolean(true);
            buttons[r-1][c].swipeUp();
        }
        else if(myValue > 0 && isValid(r-1,c) && buttons[r-1][c].getValue() == 0)
        {
            buttons[r-1][c].setValue(myValue);
            setValue(0);
            buttons[r-1][c].swipeUp();
        }
    }

    public void swipeDown()
    {
        if(myValue > 0 && isValid(r+1,c) && buttons[r+1][c].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r+1][c].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r+1][c].setCombinedBoolean(true);
            buttons[r+1][c].swipeDown();
        }
        else if(myValue > 0 && isValid(r+1,c) && buttons[r+1][c].getValue() == 0)
        {
            buttons[r+1][c].setValue(myValue);
            setValue(0);
            buttons[r+1][c].swipeDown();
        }
    }

    public void setOriginalValue()
    {
        if(Math.random() > 0.5f)
            myValue = 2;
    
        else
            myValue = 4;
    }
    
    public boolean isValid(int r, int c)
    {
        if(r<NUM_ROWS && r>=0 && c<NUM_COLS && c>=0) return true;
            
        return false;
    }

    public boolean isStuckLeft()
    {
        if(myValue == 0)	
        	return true;
        if(!isValid(r,c-1))
        	return true;
        if(buttons[r][c-1].getValue() != myValue && buttons[r][c-1].getValue() > 1)
        	return true;
        
        return false;
    }

    public boolean isStuckRight()
    {
    	if(myValue == 0)	
        	return true;
        if(!isValid(r,c+1))
        	return true;
        if(buttons[r][c+1].getValue() != myValue && buttons[r][c+1].getValue() > 1)
        	return true;
        
        return false;
    }

    public boolean isStuckUp()
    {
        if(myValue == 0)	
        	return true;
        if(!isValid(r-1,c))
        	return true;
        if(buttons[r-1][c].getValue() != myValue && buttons[r-1][c].getValue() > 1)
        	return true;
        
        return false;
        
    }

    public boolean isStuckDown()
    {
    	if(myValue == 0)	
        	return true;
        if(!isValid(r+1,c))
        	return true;
        if(buttons[r+1][c].getValue() != myValue && buttons[r+1][c].getValue() > 1)
        	return true;
        
        return false;
    }
}

public class GameButton
{
    private int myX, myY, myColor, widthh, heightt;
    private String myType;

    GameButton(int x, int y, String type)
    {
        myX = x;
        myY = y;
        myType = type;
        myColor = 255;
        widthh = 320;
        heightt = 120;
    }

    public void show()
    {
        fill(myColor);
        rect(myX, myY, widthh, heightt, 10);
        fill(255,0,0);
        textSize(90);

        if(myType == "start")
        {
        	//widthh = 300;
        	//textSize(50);
            fill(66,134,235);
        	text("Start", myX + widthh/2, myY + heightt/2-7);
        }
        else if(myType == "restart")
        {
            //widthh = 300;
            //textSize(50);
            fill(66,134,235);
            text("Restart", myX + widthh/2, myY + heightt/2-7);
        }

        if(inButton())
            highlighted();
        else
            nonHighlighted();
    }
  
    public void highlighted()
    {
        myColor = color(250,237,150);      
    }

    public void nonHighlighted()
    {
        myColor = 255;
    }

    public boolean inButton()
    {
        if(mouseX > myX && mouseX < myX+widthh && mouseY > myY && mouseY < myY+heightt)
            return true;
        return false;
    }
}

//different screen functions
public void startScreen()
{
    gameStarted = true;
	playGame = false;
	gameIsOver = false;
    instructions = false;

    start.show();

    image(birthdaySignPhoto, width/2, 150/2, width, 150);
    image(emmaPhoto, width/4, height/2, 400, 250);
    image(pugPhoto, 3*width/4, height/2);
}
public void instructionsFunc()
{
    gameStarted = false;
    playGame = false;
    gameIsOver = false;
    instructions = true;

    start.show();

    image(instructionsPhoto, width/2, 150/2 + 5, 3*width/4 - 20, 150);

    textSize(30);
    fill(0);
    text("Look at bottom of the page", width/2, height/2 - 40);
    text("(adding text in java is annoying)", width/2, height/2 + 40);
}
public void gamePlay()
{
	gameStarted = false;
    instructions = false;
	gameIsOver = false;
    playGame = true;


    fill(255);
    textSize(40);
	text("Score: " + score, width/2, 600);
	
    if(gameOver())
    {
        gameIsOver = true;
        gameStarted = false;
        playGame = false;
    }
}
public void endGame()
{
    gameIsOver = true;
	gameStarted = false;
	playGame = false;
    instructions = false;

    restart.show();

	text("GAME OVER", width/2, 600);
}

//setting number functions
public void setFirstNums() 
{
    for(int i = 0; i < NUM_ORIGINAL_BOXES; i++)
    {
        addAnotherNumber();
    }
}
public void addAnotherNumber()
{
    int roww = (int)(Math.random()*NUM_ROWS);
    int coll = (int)(Math.random()*NUM_COLS);
    if(buttons[roww][coll].getValue() == 0)
    {
        buttons[roww][coll].setOriginalValue();
    }
    else
        addAnotherNumber();
}
public void restartNumbers()
{
    for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            buttons[r][c].setValue(0);
        }
}

//stuck functions
public boolean areTheyStuckLeft()
{
	for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            if(buttons[r][c].isStuckLeft()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckRight()
{
	for(int c = NUM_COLS -1 ; c>=0; c--)
        for(int r = NUM_ROWS -1; r>=0; r--)
        {
            if(buttons[r][c].isStuckRight()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckUp()
{
	for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            if(buttons[r][c].isStuckUp()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckDown()
{
	for(int c = NUM_COLS -1 ; c>=0; c--)
        for(int r = NUM_ROWS -1; r>=0; r--)
        {
            if(buttons[r][c].isStuckDown()==false)
            	return false;
        }
    return true;
}
public boolean gameOver()
{
	if(areTheyStuckRight()&&areTheyStuckDown()&&areTheyStuckUp()&&areTheyStuckLeft())	return true;
	return false;
}

























  public void settings() {  size(1000, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "IvBirthday" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
