	
import java.awt.Dimension;

import javax.swing.JFrame;
	
	public class TermProject extends JFrame {
	
		private BackGround background;

	public TermProject() {
			setTitle("텀 프로젝트");
			setSize(1280,720);
			setPreferredSize(new Dimension(1280, 720));// 안하면 패널의 크기가 프레임보다 큼
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setUndecorated(true);//프레임의 틀을 제거
			setLocationRelativeTo(null);//프레임을 중앙으로 정렬시킴
			setResizable(false); // 프레임 크기 조절 비활성화
			background = new BackGround(); // BackGround 객체 생성
			add(background);
			

			pack();
			setVisible(true);
	
		}
		
	

		
	
		public static void main(String[] args) {
			TermProject t = new TermProject();
			
		}
	}
	
