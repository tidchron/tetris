package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    //Main play area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); //비활성 mino를 staticBlocks에 넣음

    //Others
    public static int dropInterval = 60;

    public PlayManager() {

        //Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); //1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        //시작 미노 setting
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y); //랜덤한 미노를 선택해 이 X, Y를 전달
    }

    private Mino pickMino() {
        //랜덤한 mino 고르기
        Mino mino = null;
        int i = new Random().nextInt(7); //random을 사용해 0~6의 숫자를 무작위로 고름
        switch (i) { //숫자에 따라 다른 mino 반환
            case 0:
                mino = new Mino_L1();
                break;
            case 1:
                mino = new Mino_L2();
                break;
            case 2:
                mino = new Mino_Square();
                break;
            case 3:
                mino = new Mino_Bar();
                break;
            case 4:
                mino = new Mino_T();
                break;
            case 5:
                mino = new Mino_Z1();
                break;
            case 6:
                mino = new Mino_Z2();
                break;
        }
        return mino;
    }

    public void update() {

        //currentMino가 active인지 체크
        if (!currentMino.active) {
            //active가 아닐 경우, staticBlocks에 집어넣기
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //currentMino를 nextMino로 바꾸고 대기실에 표시
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
        } else {
            currentMino.update();
        }
    }

    public void draw(Graphics2D g2) {

        //Play Area Frame 그리기
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        //Next Tetromino Frame 그리기
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        //currentMino(현재 mino) 그리기
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        //nextMino 그리기
        nextMino.draw(g2);

        //staticMino 그리기
        for (int i = 0; i < staticBlocks.size(); i++) { //staticBlocks 목록을 스캔해 하나씩 그림
            staticBlocks.get(i).draw(g2);

        }

        //Pause 상태 그리기
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}
