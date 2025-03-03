package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

//JPanel의 확장, JPanel Class의 모든 기능 있음, Runnable 구현(멀티스레딩을 위한 인터페이스, Game Loop를 별도 스레드로 실행)
public class GamePanel extends JPanel implements Runnable{

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;


    public GamePanel() {

        //Panel Setting
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null); //절대 좌표 사용(픽셀 단위 정확한 위치 제어)
        //Implement KeyListener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();
    }
    public void launchGame() {
        gameThread = new Thread(this); //this = GamePanel(Runnable 구현체)
        gameThread.start(); //스레드가 시작되면 자동으로 run 메서드 호출
    }

    @Override
    public void run() {

        //Game Loop
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) { //gameThread가 존재하는 한 이 프로세스를 계속 반복함

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) { //1 프레임 시간 도달 시
                //업데이트하고
                update();
                //그리는 행동 반복
                repaint(); //paintComponent가 아니고 repaint임
                delta--;
            }
        }

    }
    public void update() {

        if (!KeyHandler.pausePressed){
            pm.update(); //PlayManager에 업데이트 위임
        }
    }
    public void paintComponent(Graphics g) { //내장 메서드
        super.paintComponent(g); //paintComponent 메서드를 JPanel에서 사용할 때마다 super를 입력해야 함

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
