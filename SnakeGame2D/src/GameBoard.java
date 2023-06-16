import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {
    public int height = 400;
    public int width = 400;
    public int dots  = 3;
    public int dot_max = 1600;
    public int dot_size = 10;
    public int[] x = new int[dot_max];
    public int[] y = new int[dot_max];
    public int apple_x;
    public int apple_y;
    Image body;
    Image head;
    Image apple;

    boolean leftDirection = false;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = true;
    int DELAY = 100;

    private boolean inGame = true;
    GameBoard(){
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        loadImages();
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        initGame();
    }
    //Initialize the Snake Game
    public void initGame(){
        locateApple();
        for(int i = 0;i<dots;i++){
            x[i] = 50+(i*dot_size);
            y[i] = 50;
        }
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }
    //Load images from resources folder
    private void loadImages(){
        //load image of body
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        //load image of head
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        //load image of apple
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        doDrawing(graphics);
    }

    //draw images at their position
    private void doDrawing(Graphics graphics){
        //draw image of apple
        if(inGame){
            graphics.drawImage(apple, apple_x, apple_y, this);
            for(int i = 0;i<dots;i++){
                if(i==0){
                    //draw image of head
                    graphics.drawImage(head, x[0], y[0], this);
                }
                else{
                    //draw image of body
                    graphics.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else{
            gameOver(graphics);
        }
    }
    //Make Graphics for Game over
    private void gameOver(Graphics graphics){
        String msg = "Game Over!";
        int score=(dots-3)*100;
        String scoremsg="\nScore: "+ score;
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);
        graphics.setColor(Color.white);
        graphics.setFont(font);
        graphics.drawString(msg, (width-metrics.stringWidth(msg))/2, (height/2)-10);
        graphics.drawString(scoremsg, (width-metrics.stringWidth(scoremsg))/2, (height/2)+10);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            checkCollision();
            checkApple();
            move();
        }
        repaint();
    }

    //Randomize position of apple
    private void locateApple(){
        apple_x= ((int) (Math.random()*39))*dot_size;
        apple_y = ((int)(Math.random()*39))*dot_size;
    }


    //make snake move
    private void move(){
        for(int i = dots-1;i>=0;i--){
            if(leftDirection){
                if(i==0){
                    x[0]-=10;
                }
                else{
                    x[i] = x[i-1];
                    y[i] = y[i-1];
                }
            }
            if(rightDirection){
                if(i==0){
                    x[0]+=10;
                }
                else{
                    x[i] = x[i-1];
                    y[i] = y[i-1];
                }
            }
            if(upDirection){
                if(i==0){
                    y[0]-=10;
                }
                else{
                    y[i] = y[i-1];
                    x[i] = x[i-1];
                }
            }
            if(downDirection){
                if(i==0){
                    y[0]+=10;
                }
                else{
                    y[i] = y[i-1];
                    x[i] = x[i-1];
                }
            }
        }
    }
    //Make Snake eat food
    private void checkApple(){
        if(x[0]==apple_x&&y[0]==apple_y){
            dots++;
            locateApple();
        }
    }

    //Checks if there is a collision
    private void checkCollision(){
        //Collision with left border
        if(x[0]<0){
            inGame = false;
        }
        //Collision with right border
        if(x[0]>=width){
            inGame = false;
        }
        //Collision with up border
        if(y[0]<0){
            inGame = false;
        }
        //collision with down border
        if(y[0]>=height){
            inGame = false;
        }

        for(int i = 4;i<dots;i++){
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
                break;
            }
        }
    }
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key==KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_UP && (!downDirection)){
                leftDirection = false;
                upDirection = true;
                rightDirection = false;
            }
            if(key==KeyEvent.VK_DOWN && (!upDirection)){
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }

        }
    }
}