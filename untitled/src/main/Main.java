package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //window(JFrame)에 GamePanel(Jpanel) 추가
        GamePanel gp = new GamePanel(); //GamePanel 생성
        window.add(gp); //프레임에 Panel 추가
        window.pack(); //패널 크기에 맞게 창 조정

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame(); //GamePanel에 lunchGame 메서드 불러오기
    }
}
