package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    //메인 플레이 영역
    final int WIDTH = 360;
    final int HEIGHT = 600;

    //플레이 영역 경계 좌표
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino 관련 변수
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); //staticBlocks를 저장하는 리스트 (바닥에 닿은 mino)

    //기타
    public static int dropInterval = 60; //mino 자동 하강 (60프레임마다 1회)
    boolean gameOver;

    //효과
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //점수
    int level = 1;
    int lines;
    int score;

    //생성자: 플레이 영역 초기화
    public PlayManager() {

        //메인 플레이 영역 계산
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); //1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        //currentMino 시작 좌표
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        //nextMino 표시 위치
        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        //시작 mino 생성
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {
        //랜덤한 mino 고르기
        Mino mino = null;
        int i = new Random().nextInt(7);
        switch (i) {
            case 0 -> mino = new Mino_L1();
            case 1 -> mino = new Mino_L2();
            case 2 -> mino = new Mino_Square();
            case 3 -> mino = new Mino_Bar();
            case 4 -> mino = new Mino_T();
            case 5 -> mino = new Mino_Z1();
            case 6 -> mino = new Mino_Z2();
        }
        return mino;
    }

    public void update() {

        //currentMino가 비활성화인지 확인
        if (!currentMino.active) {
            // 비활성화된 mino의 블록들을 staticBlocks 리스트에 추가
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //게임오버 확인
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                //mino를 비활성화할 때 시작 위치에서 mino가 움직이지 않았는지를 확인
                //움직이지 않았다면 게임오버
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            currentMino.deactivating = false; //비활성화 상태 초기화

            //nextMino로 교체
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //라인 삭제 체크
            checkDelete();
        } else {
            currentMino.update();
        }
    }

    //완성된 라인 삭제 체크 메서드
    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        //전체 플레이 영역 스캔
        //x가 오른쪽 경계(right_x)를 넘지 않고, y가 하단 경계(bottom_y)를 넘지 않을 때까지 반복(한 줄씩 아래로 이동하며 모든 좌표를 검사)
        while (x < right_x && y < bottom_y) {

            //현재 x,y 위치에 staticBlocks가 있는지 확인
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }

            x += Block.SIZE; //다음 block 위치로 이동

            if (x == right_x) { //한 줄 전체 검사 완료시

                if (blockCount == 12) { //가로 12칸 모두 채워졌으면

                    effectCounterOn = true;
                    effectY.add(y);

                    //가장 큰 숫자부터 슬롯을 확인
                    //리스트를 앞에서부터 제거하면 요소가 삭제될 때마다 뒤의 인덱스가 변경되어 건너뛰는 요소 발생되므로 뒤에서부터 제거함
                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        //같은 Y(해당 라인)를 가진 모든 block을 제거
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    //Drop 속도 (1이 가장 빠름)
                    //10줄 마다 level 상승, Drop 속도 증가 (drop 간격 10 증가)
                    if (lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }

                    for (int i = 0; i < staticBlocks.size(); i++) {
                        //block의 Y가 staticBlock의 Y보다 크면 block 크기를 추가하여 1 block 아래로 이동
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0; //다음 라인으로 초기화
                x = left_x; //X 좌표 리셋 (왼쪽 경계)
                y += Block.SIZE; //Y 좌표 아래로 이동
            }
        }

        //Score 추가
        if (lineCount > 0) {
            GamePanel.se.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2) {

        //플레이 영역 테두리
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        //nextMino 미리보기
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        //score 프레임 그리기
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        y += 70;
        g2.drawString("LINES: " + lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + score, x, y);

        //currentMino 그리기
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        //nextMino 그리기
        nextMino.draw(g2);

        //staticMino 그리기
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //효과 그리기
        if (effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.YELLOW);

            //2번 깜빡임
            boolean isVisible = (effectCounter / 5) % 2 == 0; //0 ~ 4: ON, 5 ~ 9: OFF 반복

            if (isVisible) {
                for (int i = 0; i < effectY.size(); i++) {
                    g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
                }
            }

            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        //Pause 상태 표시, 게임오버 표시
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        if (gameOver) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        } else if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        //게임 타이틀 그리기
        x = 35;
        y = top_y + 320;
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("Simple Tetris", x + 20, y);

    }
}
