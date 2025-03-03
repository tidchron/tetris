package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

//JPanel의 확장, JPanel Class의 모든 기능 있음, Runnable 구현(멀티스레딩을 위한 인터페이스, Game Loop를 별도 스레드로 실행)
public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;
    private BufferedImage backgroundImage;
    public static Sound music = new Sound();
    public static Sound se = new Sound();


    public GamePanel() {

        //Panel 설정
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //backgroundImage 설정
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/bg.jpg")));
        } catch (NullPointerException e) {
            System.err.println("배경 이미지 파일을 찾을 수 없음: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("배경 이미지 파일을 읽을 수 없음");
            e.printStackTrace();
        }

        this.setLayout(null); //절대 좌표 사용(픽셀 단위 정확한 위치 제어)

        //KeyListener 구현
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();
    }

    public void launchGame() {
        gameThread = new Thread(this); //this = GamePanel(Runnable 구현체)
        gameThread.start(); //스레드가 시작되면 자동으로 run 메서드 호출

        music.play(0, true);
        music.loop();
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

            if (delta >= 1) { //1프레임 시간 도달 시
                //업데이트하고
                update();
                //그리는 행동 반복
                repaint(); //paintComponent가 아니고 repaint임
                delta--;
            }
        }

    }

    public void update() {

        if (!KeyHandler.pausePressed && !pm.gameOver) {
            pm.update(); //PlayManager에 업데이트 위임
        }
    }

    @Override
    public void paintComponent(Graphics g) { //내장 메서드
        super.paintComponent(g); //paintComponent 메서드를 JPanel에서 사용할 때마다 super를 입력해야 함

        Graphics2D g2 = (Graphics2D) g;

        // backgroundImage 그리기 (panel 크기에 맞게 스케일)
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        pm.draw(g2);
    }
}
