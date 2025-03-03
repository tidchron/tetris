package mino;

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

        //Check frame collision
        //Left wall
        for (int i = 0; i < b.length; i++) { //블록 배열을 스캔하고 x값을 확인
            if (b[i].x == PlayManager.left_x) { //x값이 플레이 영역 왼쪽의 x와 같으면
                leftCollision = true; //왼쪽 충돌 발생
            }
        }
        //Right wall
        for (int i = 0; i < b.length; i++) { //블록 배열을 스캔하고 x값을 확인
            if (b[i].x + Block.SIZE == PlayManager.right_x) { //x값이 플레이 영역 오른쪽의 x와 같으면
                rightCollision = true; //오른쪽 충돌 발생
            }
        }
        //Bottom floor
        for (int i = 0; i < b.length; i++) { //블록 배열을 스캔하고 y값을 확인
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) { //y값이 플레이 영역 바닥의 y와 같으면
                bottomCollision = true; //바닥 충돌 발생
            }
        }
    }

    public void checkRotationCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        //Check frame collision
        //Left wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
        }
        //Right wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
        }
        //Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void update() {

        //Move the mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            KeyHandler.upPressed = false;
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
            active = false;
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

    public void draw(Graphics2D g2) {

        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
