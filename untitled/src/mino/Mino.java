package mino;

import main.GamePanel;
import main.KeyHandler;
import main.PlayManager;

import java.awt.*;
import java.security.Key;

public class Mino {

    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1; //mino의 4가지 방향(1, 2, 3, 4)
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivationCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {
    }

    public void updateXY(int direction) {

        //충돌 발생 감지
        checkRotationCollision();

        //충돌 발생되지 않으면 회전 가능
        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }

    public void getDirection1() {
    }

    public void getDirection2() {
    }

    public void getDirection3() {
    }

    public void getDirection4() {
    }

    public void checkMovementCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        //프레임 충돌 확인
        for (int i = 0; i < b.length; i++) { //블록 배열을 스캔하고 x값을 확인
            if (b[i].x == PlayManager.left_x) { //x값이 플레이 영역 왼쪽의 x와 같으면
                leftCollision = true; //왼쪽 충돌 발생
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        //프레임 충돌 확인
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision() {

        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) { //staticBlocks 배열 스캔

            //각 블록의 X, Y를 가져옴
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            //TargetX, TargetY를 사용해 충돌 확인
            for (int j = 0; j < b.length; j++) {
                //staticBlock이 mino 바로 아래에 있음을 의미하므로 바닥 충돌 발생
                if (b[j].y + Block.SIZE == targetY && b[j].x == targetX) {
                    bottomCollision = true;
                }
            }
            for (int j = 0; j < b.length; j++) {
                if (b[j].x - Block.SIZE == targetX && b[j].y == targetY) {
                    leftCollision = true;
                }
            }
            for (int j = 0; j < b.length; j++) {
                if (b[j].x + Block.SIZE == targetX && b[j].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }

    public void update() {

        if (deactivating) {
            deactivating();
        }

        //mino 모양 회전
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1 -> getDirection2();
                case 2 -> getDirection3();
                case 3 -> getDirection4();
                case 4 -> getDirection1();
            }
            KeyHandler.upPressed = false;
            GamePanel.se.play(3, false);
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            //mino가 경계에 닿지 않았을 때 움직일 수 있음
            if (!bottomCollision) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                //down키를 누르면, autoDropCounter 리셋
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if (!rightCollision) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }
            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            //바닥 충돌시 사운드 한번만 재생
            if (!deactivating) {
                GamePanel.se.play(4, false);
            }
            deactivating = true;
        } else {
            autoDropCounter++; //매 프레임마다 카운터 늘리기
            if (autoDropCounter == PlayManager.dropInterval) {
                //mino가 떨어짐
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {

        deactivationCounter++;

        //45프레임 뒤에 비활성화
        if (deactivationCounter == 45) {

            deactivationCounter = 0;
            checkMovementCollision(); // Check collision

            //45프레임 뒤에도 충돌이 계속 켜져 있으면 이 mino를 비활성화
            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
