package mino;

import java.awt.*;

public class Block extends Rectangle {

    public int x, y;
    public static final int SIZE = 30; //30 x 30 block
    public Color c;

    public Block(Color c) { //생성자에서 색상 정보 받기, 각 Tetromino는 다른 색상을 갖기 때문에
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        int margin = 2;
        g2.setColor(c);
        g2.fillRect(x + margin, y + margin, SIZE - (margin * 2), SIZE - (margin * 2));
    }
}
