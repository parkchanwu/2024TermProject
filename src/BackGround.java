import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class BackGround extends JPanel implements ActionListener, KeyListener {
	private Clip Music;//뮤직


	//스플래쉬 이미지 생성
	private ImageIcon waterSplashImage=new ImageIcon("src\\image\\물 펑펑.gif");
	private ImageIcon fireSplashImage=new ImageIcon("src\\image\\파이리 splash (1) (1).gif");

	//라디오 버튼을 위한 이미지 생성
	private ImageIcon radio1Button = new ImageIcon("src\\image\\꼬부기.png");
	private ImageIcon radio2Button = new ImageIcon("src\\\\image\\\\파이리 (2).png");

	//배경 이미지 생성
	private ImageIcon backgroundImage = new ImageIcon("src\\image\\첫번째 배경화면.jpg");//첫 화면
	private ImageIcon backgroundImage2 = new ImageIcon("src\\image\\두번째 배경화면.jpg");//꼬부기 배경화면
	private ImageIcon backgroundImage3 = new ImageIcon("src\\image\\Ошибка 429.jpg");//파이리 배경화면

	//버튼 이미지 생성
	private ImageIcon startButtonImage = new ImageIcon("src\\image\\Startbutton_pressed.png");
	private ImageIcon retryButtonImage = new ImageIcon("src\\image\\Retrybutton_pressed.png");
	private ImageIcon quitButtonImage = new ImageIcon("src\\image\\Quitbutton.png");

	//버튼
	private JButton retryButton = new JButton(retryButtonImage);
	private JButton startButton = new JButton(startButtonImage);
	private JButton quitButton = new JButton(quitButtonImage);

	private Image attackImg; //공격 이미지
	private	ImageIcon attackIcon; //공격 이미지
	private Image userImage;//꼬부기 이미지
	private Image userImage2;//파이리 이미지

	//JRadioButton 꼬부기,파이리 선택
	private JRadioButton radio1 = new JRadioButton("꼬부기",radio1Button);
	private JRadioButton radio2 = new JRadioButton("파이리",radio2Button);

	//패널
	private JPanel buttonPanel=new JPanel();

	//라벨
	private JLabel stageLabel =new JLabel(); //몇 스테이지인지 알려줄 라벨
	private JLabel currentScoreLabel =new JLabel(); //몇 점인지 알려줄 라벨

	//ArrayList
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();//적 생성에 사용
	private ArrayList<Attack> attackList = new ArrayList<Attack>();//공격 이미지 생성에 사용
	private ArrayList<Attack> AttackToRemove = new ArrayList<>();//
	private ArrayList<Enemy> EnemyToRemove = new ArrayList<>();//

	//타이머 생성
	private Timer t = new Timer(10, new MyClass());

	//로직을 위한 변수들
	private int score; //점수 변수
	private int currentImage = 1; // 이미지 선택을 위한 변수
	private boolean visible=false;//스플래시 이미지 보이게하기위한 boolean변수
	private static int imageX = 100; //user의 이미지 크기X
	private static int imageY = 100;//user의 이미지 크기Y	
	private int gameOver;//종료조건
	private int moveSpeed=5;//꼬북이 이동속도
	private int moveSpeed2=15;//이상해씨 이동속도
	private int moveAttackSpeed=30;//물대포 이동속도

	int selectedCharacter = 1; // 꼬부기: 1, 파이리: 2  캐릭터 선택

	int count=0; //몇 명을 죽였는지 카운트
	int stageCount=1;// 기본은 1단계 스테이지 몇인지 알려주는 코드이며 카운트와 같이 쓰임
	//타이머 변수들
	int sec=0; //초
	int addEnemy=3000;//적의 생성 속도 3초
	int enemySpeed=50; //적이 다가오는 속도 
	int splash=200; //공격 splash 이미지를 보여줌 2초
	int attackSpeed=50; //공격 이동속도
	private int effectionX;//splash의 좌표X
	private int effectionY;//splash의 좌표Y

	public BackGround() {
		// 꼬부기 이미지 로드
		ImageIcon icon = new ImageIcon("src\\image\\꼬부기 움직임.gif");
		userImage = icon.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);

		// 파이리 이미지 로드
		ImageIcon icon2 = new ImageIcon("src\\image\\파이리 움직임.gif");
		userImage2 = icon2.getImage().getScaledInstance(imageX, imageY, Image.SCALE_DEFAULT);

		setLayout(null); // 컴포넌트를 절대적 위치에 배치하기 위함
		//시작 버튼의 설정
		startButton.setBorderPainted(false);//버튼의 테두리 false
		startButton.setContentAreaFilled(false);// 버튼 내용 영역 false


		//종료 버튼 설정
		quitButton.setBorderPainted(false);//버튼의 테두리 false
		quitButton.setContentAreaFilled(false);// 버튼 내용 영역 false


		//다시하기 버튼의 설정
		retryButton.setBorderPainted(false);//버튼의 테두리 false
		retryButton.setContentAreaFilled(false);// 버튼 내용 영역 false
		retryButton.setVisible(false);

		//라벨 텍스트의 색상 변경
		stageLabel.setForeground(Color.RED);
		currentScoreLabel.setForeground(Color.RED);

		//위치 설정
		startButton.setBounds(800, 420, 400, 80);
		quitButton.setBounds(1150, 10, 100, 30);
		retryButton.setBounds(800, 420, 400, 80);
		currentScoreLabel.setBounds(700,-30, 100, 100);
		buttonPanel.setBounds(0, 10, 300, 300);
		stageLabel.setBounds(630,-30, 100, 100);

		// 버튼 그룹 생성
		ButtonGroup group = new ButtonGroup();
		group.add(radio1);
		group.add(radio2);        
		radio1.setSelected(true); // 기본값을 꼬부기로 

		// 프레임에 JRadioButton 추가
		buttonPanel.add(radio1);
		buttonPanel.add(radio2);

		buttonPanel.setVisible(true);
		radio1. setOpaque(false); //버튼의 배경을 지움
		radio2. setOpaque(false);//버튼의 배경을 지움
		buttonPanel.setOpaque(false);//버튼패널의 배경을 지움

		//리스너 장착
		radio1.addActionListener(this);
		radio2.addActionListener(this);
		retryButton.addActionListener(this);
		startButton.addActionListener(this);
		quitButton.addActionListener(this);

		//키리스너 장착
		addKeyListener(this);

		// JPanel의 크기를 배경 이미지의 크기로 설정
		setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));
		add(buttonPanel,BorderLayout.NORTH);
		add(stageLabel);
		add(currentScoreLabel);
		add(quitButton);
		add(startButton);
		add(retryButton);

		setFocusable(false);    
		quitButton.setFocusable(false);
		startButton.setFocusable(true);
		// 배경 음악 로드 및 재생
		SoundPalyer("src\\audio\\backgroundmusic.wav"); 
	}
	// 전체 음악소리 같은것 처리 사운드 반복 재생
	private void SoundPalyer(String audioFilePath) {
		try {
			File audioFile = new File(audioFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			// Clip을 사용하여 음악을 재생
			Music = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			Music.open(audioStream);
			//            Music.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
			// 배경 음악 재생 시작
			Music.start();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//사운드 한번만 재생 (효과음 같은것 처리)
	private void OneSoundPalyer(String audioFilePath) {
		try {
			File audioFile = new File(audioFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			// Clip을 사용하여 음악을 재생
			Music = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			Music.open(audioStream);
			// 배경 음악 재생 시작
			Music.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void restartGame() {

		// 타이머 초기화
		addEnemy=3000;//적의 생성 속도 3초
		enemySpeed=50; //적이 다가오는 속도 
		splash=200; //물 터지는 이미지를 보여줌 2초
		attackSpeed=50; //뮬대포 이동속도
		count=0; //죽인 명수 초기화
		score=0;//점수 초기화
		stageCount=1;//스테이지 초기화

		gameOver=0;//종료조건 초기화
		startButton.setVisible(true);
		retryButton.setVisible(false);
		// 꼬부기 초기 위치로 이동
		imageX = 100;
		imageY = 100;

		// 이미지 선택 초기화
		currentImage = 1;
		stageLabel.setVisible(false);
		currentScoreLabel.setVisible(false);

		// 어레이 리스트 초기화
		enemyList.clear();
		attackList.clear();
		AttackToRemove.clear();
		EnemyToRemove.clear();

		// 타이머 초기화
		moveSpeed = 8;
		moveSpeed2 = 15;
		moveAttackSpeed = 30;


		// 다시 그리기
		repaint();

	}
	//스테이지 업의 메소드
	public void StageUp() {

		if(count>=2&&count<5) { //2명을 죽였으면
			stageCount=2;
			addEnemy=2000; //적 생성 시간
			enemySpeed=40; //적의 이동속도
			moveSpeed=8; //꼬북이 이동속도
			moveAttackSpeed=33; //물대포 이동속도(기본값 30)
		}
		else if(count>=5 &&count<10) {
			stageCount=3;//3단계라고 알려줌
			addEnemy=1000;
			enemySpeed=30;
			moveSpeed=10; //꼬북이 이동속도
			moveAttackSpeed=33; //물대포 이동속도(기본값 30)
		}
		else if(count>=10 &&count<15) {
			stageCount=4; //4단계라고 알려줌
			addEnemy=500;
			enemySpeed=20;
			moveSpeed=10; //꼬북이 이동속도
			moveAttackSpeed=35; //물대포 이동속도(기본값 30)
		}
		else if(count>=15 &&count<20) {
			stageCount=5; //5단계라고 알려줌
			addEnemy=500;
			enemySpeed=15;
			moveSpeed=10; //꼬북이 이동속도
			moveAttackSpeed=37; //물대포 이동속도(기본값 30)
		}

		else if(count>=20) {
			stageCount=6; //6단계라고 알려줌
			addEnemy=300;
			enemySpeed=15;
			moveSpeed=20; //꼬북이 이동속도
			moveAttackSpeed=39; //물대포 이동속도(기본값 50)
		}

		stageLabel.setText("현재 단계: " + stageCount);
		currentScoreLabel.setText("점수:"+ score);

	}
	@Override
	//이미지 그리기
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 배경 이미지 그리기
		if (currentImage == 1) {//현재 배경
			g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
			buttonPanel.setVisible(true);
		} else if (currentImage == 2) {//현재 배경
			if(selectedCharacter==1) {//캐릭터가 꼬부기일때의 배경
				g.drawImage(backgroundImage2.getImage(), 0, 0, getWidth(), getHeight(), this);
			}
			else if(selectedCharacter==2){//캐릭터가 파이리일때의 배경
				g.drawImage(backgroundImage3.getImage(), 0, 0, getWidth(), getHeight(), this);
			}
			// 선택한 캐릭터에 따라 이미지 변경
			if (selectedCharacter == 1) {
				// 꼬부기 이미지 그리기
				g.drawImage(userImage, imageX, imageY, this);
			} else if (selectedCharacter == 2) {
				// 파이리 이미지 그리기
				g.drawImage(userImage2, imageX, imageY, this);
			}
			//스플래쉬 선택 꼬부기/파이리
			if(visible==true) {
				if(selectedCharacter==1) {
					g.drawImage(waterSplashImage.getImage(),effectionX, effectionY, this);
					repaint();
				}
				else if(selectedCharacter==2) {
					g.drawImage(fireSplashImage.getImage(),effectionX, effectionY, this);
					repaint();
				}
			}
			// 적 그리기
			for (Enemy enemy : enemyList) {
				g.drawImage(enemy.getEnemyImg(), enemy.getX(), enemy.getY(), this);
			}
			//공격 이미지 그리기
			for (Attack attack : attackList) {
				g.drawImage(attack.getAttackImg(), attack.getX(), attack.getY(), this);
			}
		}
	}
	@Override
	//버튼의 동작
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			setFocusable(true); // 포커스를 받을 수 있도록 설정
			requestFocusInWindow();

			StageUp();
			currentImage = 2;
			buttonPanel.setVisible(false);
			startButton.setVisible(false);
			stageLabel.setVisible(true);
			currentScoreLabel.setVisible(true);
			t.start();
			repaint();


		} else if (e.getSource() == quitButton) {
			System.exit(0);
		}
		else if(e.getSource()==retryButton) {

			restartGame();
			setFocusable(true);
		}

		if(e.getSource()==radio1) {
			selectedCharacter=1;
		}
		else if(e.getSource()==radio2) {
			selectedCharacter=2;
		}

	}
	// Timer와 연관된 핸들러
	private class MyClass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (sec % addEnemy == 0) {
				// 3초마다 새로운 이상해씨 객체 생성
				enemyList.add(new Enemy());
				repaint();
			}
			if (sec % enemySpeed == 0) {
				//적의 움직임
				EnemyMove();
				repaint(); // 화면 다시 그리기
			}
			if (sec % splash == 0) {
				visible=false;
				repaint();
			}

			if (sec % attackSpeed == 0) {
				//공격의 움직임
				attackMove();			
				for (Attack attack : attackList) {
					for (Enemy enemy : enemyList) {

						if (isCrash(attack, enemy)) {
							AttackToRemove.add(attack); //충돌시 AttackToRemove에 저장
							EnemyToRemove.add(enemy);	//충돌시 	EnemyToRemove에 저장				
							effectionX = enemy.enemyX; //스플래쉬의 위치 조정 X
							effectionY = enemy.enemyY - 75; //스플래쉬의 위치 조정 Y
							visible=true; //스플래쉬 보이기!
							//선택한 캐릭터의 공격 소리
							if(selectedCharacter==1) {
								OneSoundPalyer("src\\audio\\hitten_sound.wav"); 
							}
							else if(selectedCharacter==2){
								OneSoundPalyer("src\\audio\\파이어볼_맞는소리.wav");
							}
												
							score += 10;
							count++;
						}
					}  				
				}
				for (Attack attack : attackList) {
					// Attack 객체의 X 좌표가 화면 우측을 벗어나면 제거
					if (attack.getX() > getWidth()) {
						AttackToRemove.add(attack);
					}
				}

				// AttackToRemove에 저장된거 삭제
				for (Attack attack : AttackToRemove) {
					attackList.remove(attack);
				}
				//EnemyToRemove에 저장된거 삭제
				for (Enemy enemy : EnemyToRemove) {
					enemyList.remove(enemy);
				}
				repaint(); // 화면 다시 그리기
			}

			sec+=10; // 시간 증가
			repaint(); // 화면 다시 그리기
		}
	}

	private void EnemyMove() {
		// 타이머 이벤트 발생 시 적들의 X 좌표를 감소시켜 다가오게 함		
		for (Enemy enemy : enemyList) {
			enemy.moveX(moveSpeed2);				
			// 왼쪽 벽을 벗어난 적 제거
			if (enemy.getX()+100== -10 ) {			
				gameOver+=1;
				
			}
		}
		if (gameOver ==3 ) {
			// 게임을 재시작합니다.
			gameOver();
		}
	}

	private void attackMove() {
		// 타이머 이벤트 발생 시 적들의 X 좌표를 감소시켜 다가오게 함
		for (Attack attack : attackList) {
			attack.moveX(moveAttackSpeed); //어택 이동속도
		}
	}
	//게임 종료 타이머
	public  void gameOver() {
		t.stop();
		//화면의 크기는 1280,720
		retryButton.setVisible(true);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// 키를 입력했을 때 동작할 내용    
	}

	@Override
	//키 리스너
	public void keyPressed(KeyEvent e) {
		StageUp();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			imageY -= moveSpeed;

			break;
		case KeyEvent.VK_S:
			imageY += moveSpeed;
			break;
		case KeyEvent.VK_A:
			imageX -= moveSpeed;
			break;
		case KeyEvent.VK_D:
			imageX += moveSpeed;
			break;
		case KeyEvent.VK_SPACE:
			//소리 선택 꼬부기/파이리
			if (selectedCharacter == 1) {
				// 사운드 파일 재생
				OneSoundPalyer("src\\audio\\물_발사.wav"); 
				//이미지 생성
				attackList.add(new Attack());
			} else if (selectedCharacter == 2) {
				// WAV 파일 재생
				OneSoundPalyer("src\\audio\\short-fireball-woosh-6146.wav");   
				//이미지 생성
				attackList.add(new Attack());
			}

		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// 키를 놓았을 때 동작할 내용
	}
	// 충돌 감지 메소드
	private boolean isCrash(Attack attack, Enemy enemy) {
		int attackRight = attack.getX() + attackImg.getWidth(this);
		int attackBottom = attack.getY()+attackImg.getHeight(this);
		int enemyLeft = enemy.getX();
		int enemyBottom = enemy.getX();

		return (attackRight >= enemyLeft && attackBottom <= enemy.getEnemyImg().getHeight(this) + 
				enemy.getY() && attackBottom >= enemy.getY());
	}
	//적 객체의 클래스
	public class Enemy {
		public Image enemyImg;
		public int enemyX;
		public int enemyY;

		public Enemy() {
			ImageIcon enemyIcon = new ImageIcon("src\\image\\적_이상해씨-removebg-preview (1) (1).png");
			enemyImg = enemyIcon.getImage();
			enemyImg = enemyImg.getScaledInstance(130, 130, Image.SCALE_DEFAULT);

			// 무작위 Y축 위치 생성
			Random random = new Random();
			enemyY = random.nextInt(720 - BackGround.imageY); // 이미지의 높이에 맞게 Y 위치 생성
			enemyX = 1300; // 초기 X 위치 설정
		}


		public int getX() {
			return enemyX; // X 좌표를 반환
		}

		public int getY() {
			return enemyY; // Y 좌표를 반환
		}

		public void moveX(int x) {
			enemyX -= x; // X 좌표를 이동
		}

		public Image getEnemyImg() {
			return enemyImg;
		}
	}
	//공격이미지의 클래스
	public class Attack {

		int attackX = imageX + 100;
		int attackY = imageY;

		public Attack() {
			if(selectedCharacter==1) {
				attackIcon = new ImageIcon("src\\image\\waterAttack.png");
			}
			else if(selectedCharacter==2) {
				attackIcon = new ImageIcon("src\\image\\파이리 공격 (1) (2).gif");
			}
			//이미지로드 없으면 안그려짐
			attackImg = attackIcon.getImage();
		}
		public int getX() {
			return attackX; // X 좌표를 반환
		}

		public int getY() {
			return attackY; // Y 좌표를 반환
		}

		public void moveX(int x) {
			attackX += x; // X 좌표를 이동
		}

		public Image getAttackImg() {
			return attackImg;
		}
	}
}