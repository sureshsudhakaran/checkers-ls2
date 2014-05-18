import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.* ;
import java.io.*;

public class Board implements ActionListener
{
	JFrame J;										//The Frame for the Board
	JPanel P;
	int move;										//The move number
	int turn;
	int flag;
	Process prologProcess;
	String prolog=new String();
	String ss=new String();
	String str1=new String();
	String str2=new String();
	public class Sq extends JButton 				//Every square on the board
	{
		private int xco;							//the x-coordinate of the square
		private int yco;							//the y-coordinate of the square
		private int state; 						//occupied by white = 1, occupied by black=2, unoccupied=0
		private int color; 						//black=2 white=1
		String s=new String();						//will store the x and y coordinates in the list format for prolog
		
		public void setstate(int occ)				//Sets the state of the square to the value passed as parameter
		{											
			state=occ;
		}
		public int getstate()						//Returns the state of the square
		{
			return state;
		}
		public void setx(int x)					//Sets the x-coordinate of the square to the value passed as parameter
		{
			xco=x;
		}
		public int getx()							//Returns the x-coordinate of the square
		{
			return xco;
		}
		public void sety(int y)					//Sets the y-coordinate of the square to the value passed as parameter
		{
			yco=y;
		}
		public int gety()							//Returns the y-coordinate of the square
		{
			return yco;
		}
		public int getcol()						//Returns the color of the square
		{
			return color;
		}
		public String getstring()					//Returns the String of the coordinates in the list format for prolog
		{
			return s;
		}
		Sq(int x,int y)							//Constructor for Sq. Takes the x and y coordinates as parameters and computes values for other fields
		{
			s="[" + Integer.toString(x) + "," + Integer.toString(y) + "]"; //list  format for prolog
			xco=x;															//Setting the coordinates
			yco=y;
			state=0;
			if((x+y)%2==0)													//Setting the color
			color=1;
			else
			color=2;
			if((x<=3)&&(((x+y)%2)==1))										//Setting the state of a square for the initial configuration of the 8x8 board
			state=1;
			if((x>=6)&&(((x+y)%2)==1))
			state=2;
		}
		
	}
	boolean firclick;								//Set to true if the player has clicked the square
	boolean morecappos;							//Set to true if more captures are possible with the piece used for capture in this move
	Sq s1,s2,s3;
	ArrayList <Sq> Bt=new ArrayList <Sq>();		//ArrayList of Squares
	Board()
	{
		firclick=false;
		Sq B;
		move=1;
		turn=0;
		flag=0;
		ss="";
		str1="";
		str2="";
		J = new JFrame();
		P = new JPanel();
		for(int i=1;i<=8;i++)
		for(int j=1;j<=8;j++)
		{
			B=new Sq(i,j);
			if(B.getcol()==1)
			B.setBackground(new Color(255,255,255));
			else
			B.setBackground(new Color(0,0,0));
			if(B.getstate()==1)
			{
				try{
					Image img = ImageIO.read(new File("red.png"));
					B.setIcon(new ImageIcon(img));
				}catch(IOException Ex){}
			}
			if(B.getstate()==2)
			{
				try{
					Image img = ImageIO.read(new File("brown.png"));
					B.setIcon( new ImageIcon(img));
				}catch(IOException Ex){}
			}
			B.setActionCommand(Integer.toString(i)+","+Integer.toString(j));
			B.addActionListener(this);
			Bt.add(B);
		}
		J.setSize(640,640);
		P.setSize(640,640);
		P.setLayout(new GridLayout(8,8));
		J.add(P);
		for(Sq Bi:Bt)
			P.add(Bi);
		J.setVisible(true);
		P.setVisible(true);
		for(Sq Bi:Bt)
			Bi.setVisible(true);
		J.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent act)					//Defines actions for Action Performed
	{
		morecappos=false;
		/*System.out.println("Current System Stat");
		for(Sq Bj:Bt)
		{
			if(Bj.getstate()!=0)
			System.out.println("X and Y are "+Bj.getstring());
		}*/
		if(!stalemate(2)){
		if((((Sq)act.getSource()).getstate()==2)&&(firclick==false))
		{
			firclick=true;
			s1=(Sq)act.getSource();
		}
		else
		{
			if((((Sq)act.getSource()).getstate()==0)&&(firclick==true))
			{
				s2=(Sq)act.getSource();
				//System.out.println("S1 is position (" + s1.getx() + "," + s1.gety()+")");
				//System.out.println("S2 is position (" + s2.getx() + "," + s2.gety()+")");
				if(valmove(s1,s2))
				{
					//System.out.println("Changing icons");
					s2.setIcon(s1.getIcon());
					s2.setstate(s1.getstate());
					s1.setstate(0);
					s1.setIcon(null);
					s1.repaint();
					s2.repaint();
					//System.out.println("Changing icons complete");
					if((s2.getx()-s1.getx())==-2)
					{
						for(Sq Bi:Bt)
						{
							if((Bi.getx()==s2.getx()+1)&&(Bi.gety()==(s2.gety()+s1.gety())/2))
							{
								//System.out.println("Found dem");
								Bi.setIcon(null);
								Bi.setstate(0);
							}
						}
						morecappos=mulcappos(s2,2);
					}
					str1=s1.getActionCommand();
					str2=s2.getActionCommand();
					str1="["+str1+"]";
					str2="["+str2+"]";
					//System.out.println("Str 1 is " + str1);
					//System.out.println("Str 2 is " + str2);
					ss+="[";
					ListIterator<Sq> lI = Bt.listIterator(Bt.size());
					 while (lI.hasPrevious())
					{
						Sq Bj=lI.previous();
						if(Bj.getstate()==1)
						{
							ss+=Bj.getstring();
							ss+=",";
						}
					}
					ss+="[]],[";
					for(Sq Bj:Bt)
					{
						if(Bj.getstate()==2)
						{
							ss+=Bj.getstring();
							ss+=",";
						}
					}	
					ss+="[]]";
					ss="["+ss+"].";	
					//System.out.println("Sent string is "+ss);
					turn=1;
					try
					{
						File file= new File("un.txt");
						file.createNewFile();
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(ss);
						bw.close();	
						fw.close();
					}catch(IOException ex){System.out.println("failed");ex.printStackTrace();}
					firclick=false;
					for(Sq Bi:Bt)
					{
						if((Bi.getx()==1)&&(Bi.getstate()==2))
						{
							JOptionPane.showMessageDialog(J,"You win");
							flag=1;
							System.exit(0);
						}
						
					}
					if((flag==0)&&(!(morecappos)))
					compmove();
					if(morecappos)
					ss="";
				}
				else
				{
					firclick=false;
					s1=null;
				}
			}
			else
			{
				firclick=false;
				s1=null;
			}
		}
	}}
	public boolean sear(int i,int j,int w)						//Returns true if position i,j has state w
	{
		for(Sq Bi:Bt)
		{
			if((Bi.getx()==i)&&(Bi.gety()==j)&&(Bi.getstate()==w))
			return true;
		}
		return false;
	}
	public void serset(int i, int j, int w)						//Sets the state of Square at position i,j to w
	{
		for(Sq Bi:Bt)
		{
			if((Bi.getx()==i)&&(Bi.gety()==j)&&(Bi.getstate()==w))
			s3=Bi;
		}
	}
	public boolean valmove(Sq si, Sq sj)							//Returns true if the move from si to sj is valid
	{
		//System.out.println("Entering valmove");
		boolean t=capmovepos();
		if(((si.getx()-1)==sj.getx())&&((((si.gety())+1)==sj.gety())||(((si.gety())-1)==sj.gety()))&&(!t))
		{
			//System.out.println("Exiting valmove");
			return true;
		}
		if((t)&&((si.getx()-2)==sj.getx())&&((si.gety()+2==sj.gety())||(si.gety()-2==sj.gety()))&&(sear(si.getx()-1,(si.gety()+sj.gety())/2,1)))
		{
			//System.out.println("Exiting valmove for capture");
			//serset(si.getx()+1,(si.gety()+sj.gety())/2,1);
			return true;
		}
		else
		{
			//System.out.println("Exiting valmove with false");
			return false;
		}
	}
	public boolean mulcappos(Sq csq,int w)					//Returns true if captures are possible from the piece csq
	{
		boolean cappos=false;
		if(csq==null)
		System.out.println("Mulcappose called with null");
		if(w==2){
		for(Sq Bj:Bt)
		{
			if((csq.getx()-1==Bj.getx())&&(csq.gety()+1==Bj.gety())&&(csq.getstate()==w)&&(Bj.getstate()==1))
			{
				for(Sq Bk:Bt)
				{
					if((Bk.getx()==csq.getx()-2)&&(csq.gety()+2==Bk.gety())&&(Bk.getstate()==0))
					{
						cappos=true;
						break;
					}
				}
			}
			else
			{
				if((csq.getx()-1==Bj.getx())&&(csq.gety()-1==Bj.gety())&&(csq.getstate()==w)&&(Bj.getstate()==1))
				{
					for(Sq Bk:Bt)
					{
						if((Bk.getx()==csq.getx()-2)&&(csq.gety()-2==Bk.gety())&&(Bk.getstate()==0))
						{
							cappos=true;
							break;
						}
					}
				}
			}
		}}
		else
		{
			if(w==1){
				for(Sq Bj:Bt)
		{
			if((csq.getx()+1==Bj.getx())&&(csq.gety()+1==Bj.gety())&&(csq.getstate()==w)&&(Bj.getstate()==2))
			{
				for(Sq Bk:Bt)
				{
					if((Bk.getx()==csq.getx()+2)&&(csq.gety()+2==Bk.gety())&&(Bk.getstate()==0))
					{
						cappos=true;
						break;
					}
				}
			}
			else
			{
				if((csq.getx()+1==Bj.getx())&&(csq.gety()-1==Bj.gety())&&(csq.getstate()==w)&&(Bj.getstate()==2))
				{
					for(Sq Bk:Bt)
					{
						if((Bk.getx()==csq.getx()+2)&&(csq.gety()-2==Bk.gety())&&(Bk.getstate()==0))
						{
							cappos=true;
							break;
						}
					}
				}
			}
		}}
	}
		/*if(cappos==true)
		System.out.println("mulcappos returns true");
		else
		System.out.println("mulcappos returns false");*/
		return cappos;
	}
	public boolean capmovepos()					//Returns true if a capture move is possible
	{
		boolean cappos=false;
		for(Sq Bi:Bt)
		{
			for(Sq Bj:Bt)
			{
				if((Bi.getx()-1==Bj.getx())&&(Bi.gety()+1==Bj.gety())&&(Bi.getstate()==2)&&(Bj.getstate()==1))
				{
					for(Sq Bk:Bt)
					{
						if((Bk.getx()==Bi.getx()-2)&&(Bi.gety()+2==Bk.gety())&&(Bk.getstate()==0))
						{
							cappos=true;
							break;
						}
					}
				}
				else
				{
					if((Bi.getx()-1==Bj.getx())&&(Bi.gety()-1==Bj.gety())&&(Bi.getstate()==2)&&(Bj.getstate()==1))
					{
						for(Sq Bk:Bt)
						{
							if((Bk.getx()==Bi.getx()-2)&&(Bi.gety()-2==Bk.gety())&&(Bk.getstate()==0))
							{
								cappos=true;
								break;
							}
						}
					}
				}
			}
		}
		return cappos;
	}
	public boolean stalemate(int w)
	{
		boolean staleflag=true;
		for(Sq Bi:Bt)
		{
			if(Bi.getstate()==w)
			{
				for(Sq Bj:Bt)
				{
					if(valmove(Bi,Bj))
					{
						staleflag=false;
						break;
					}
				}
			}
		}
		if(staleflag==true)
		{
			System.out.println("Stalemate");
			return staleflag;
		}
		else
		return staleflag;
	}
						
	public void compmove()					//Calls the prolog program to generate the computer move
	{
		if(!stalemate(1)){
		ss="";
		boolean capflag=false;
		Sq n1=null;
		turn=0;
		try
		{
			prologProcess =  Runtime.getRuntime().exec("sh dem");
			BufferedReader in = new BufferedReader(new InputStreamReader(prologProcess.getInputStream()));  
			String line = null;  
			while ((line = in.readLine()) != null) 
			{  
				//System.out.println("The line is "+ line);
				String d1=new String();
				String d2=new String();
				d1=line.substring(1,6);
				d2=line.substring(7,12);
				System.out.println("Move given by computer "+move);
				System.out.println(d1);  
				System.out.println(d2);  
				
				char c1=d1.charAt(1);
				char c2=d1.charAt(3);
				char c3=d2.charAt(1);
				char c4=d2.charAt(3);
				for(Sq Bi:Bt)
				{
					if((Bi.getx()==c1-48)&&(Bi.gety()==c2-48))
					{
						Icon I1=Bi.getIcon();
						Bi.setIcon(null);
						Bi.setstate(0);
						for(Sq Bj:Bt)
						{
							if((Bj.getx()==c3-48)&&(Bj.gety()==c4-48))
							{
								Bj.setIcon(I1);
								Bj.setstate(1);
								
								if(((Bj.getx()-Bi.getx())==2)||(((Bj.getx()-Bi.getx())==-2)))
								{
									n1=Bj;
									//System.out.println("Capture flag set");
									capflag=true;
									for(Sq Bk:Bt)
									{
									if((Bk.getx()==(Bi.getx()+Bj.getx())/2)&&(Bk.gety()==(Bi.gety()+Bj.gety())/2))
									{
										Bk.setIcon(null);
										Bk.setstate(0);
										//s[Bk.getx()][Bk.gety()]="";
										Bk.repaint();
									}
								}
							}
						}
						Bj.repaint();
					}
				}
			}
			
			//System.out.println("Destroyed in seconds");
		}  
		}catch(Exception xx) {System.out.println(xx) ; }
		prologProcess.destroy();
		//System.out.println("Current System Stat");
		
		/*for(Sq Bj:Bt)
		{
			if(Bj.getstate()!=0)
			System.out.println("X and Y are [" + Bj.getx() +"," + Bj.gety()+")");
		}*/
		for(Sq Bi:Bt)
		{
			if((Bi.getx()==8)&&(Bi.getstate()==1))
			{
				JOptionPane.showMessageDialog(J,"Computer wins");
				flag=1;
				System.exit(0);
			}
			
		}
		if(capflag)
		{
			//System.out.println("Capflag is set here");
		if(mulcappos(n1,1))
		{
			//System.out.println("mulcappos is set here");
			ss+="[";
			ss+=n1.getstring();
			ss+=",[]],[";
			for(Sq Bj:Bt)
			{
				if(Bj.getstate()==2)
				{
					ss+=Bj.getstring();
					ss+=",";
				}
			}	
			ss+="[]]";
			ss="["+ss+"].";	
			//System.out.println("Sent string is "+ss);
			turn=1;
			try
			{
				File file= new File("un.txt");
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(ss);
				bw.close();	
				fw.close();
			}catch(IOException ex){System.out.println("failed");ex.printStackTrace();}
			//System.out.println("Multiple capture sequence. Writing to file string " + ss);
			compmove();
		}
		else
		move++;
	}
	else
	move++;
	}}
	public void wr()								//Makes the first move on the computer's behalf
	{
		ss+="[";
		for(Sq Bj:Bt)
		{
			if(Bj.getstate()==1)
			{
				ss+=Bj.getstring();
				ss+=",";
			}
		}
		ss+="[]],[";
		for(Sq Bj:Bt)
		{
			if(Bj.getstate()==2)
			{
				ss+=Bj.getstring();
				ss+=",";
			}
		}	
		ss+="[]]";
		ss="["+ss+"].";	
		//System.out.println("Sent string is "+ss);
		turn=1;
		try
		{
			File file= new File("un.txt");
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ss);
			bw.close();	
			fw.close();
		}catch(IOException ex){System.out.println("failed");ex.printStackTrace();}
		compmove();
	}
	public static void main(String[] args)
	{
		Board wa= new Board();
		wa.wr();
	}
}
