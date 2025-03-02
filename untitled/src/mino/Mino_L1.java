package mino;

import java.awt.*;

public class Mino_L1 extends Mino {

    public Mino_L1() {
        create(Color.ORANGE); //super class의 create 메서드 호출
    }

    public void setXY(int x, int y) {
        //  ㅇ : b[1]
        //  ㅇ : b[0] 회전시에도 상대적 위치가 변하지 않기 때문
        //  ㅇ ㅇ : b[2] b[3]
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.SIZE;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }

    public void getDirection1() {
        //  ㅇ
        //  ㅇ
        //  ㅇ ㅇ
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(1);
    }

    public void getDirection2() {
        //
        //  b[2]ㅇ ㅇ ㅇb[1]
        //  b[3]ㅇ
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(2);
    }

    public void getDirection3() {
        // b[3]ㅇ ㅇb[2]
        //        ㅇ
        //        ㅇb[1]
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(3);
    }

    public void getDirection4() {
        // b[3]ㅇ
        // b[1]ㅇ ㅇ ㅇb[2]
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(4);
    }
}
