/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

// GLOBALS For LIVE CHAT Mode.
import com.sun.speech.freetts.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author sony
 */
public class chatpage extends javax.swing.JFrame {
String UEMAIL; String UPASS;String UFNAME, USNAME,GENDER;Connection con=null;Statement stmt=null;String chatID=null; // ChatID is friend Email
Timer timer=null,LastSeentimer=null;int CHATMSGNO=0;String FRIENDLASTSEEN="0";long i=1;  
Voice voice;String VOICENAME="kevin16";
TimerTask LastSeen_Task=null,UpdatingChat_Task=null;
final JDialog d=new JDialog();
// This GLOBALS are for iSay mode.

    /**                                                                                // Global variables
     * Creates new form LogIn
     */
    public chatpage() 
{ 
        initComponents();
}
    
    public chatpage(String EMAIL,String PASS,String GENDER1,String FNAME) 
{             // to take varibles from other jFrames
    initComponents();
        
    Print_loading();
        
    Thread t = new Thread()
    {
        public  void run()
        {
       // TASK BEGIN 

            VoiceManager vm=VoiceManager.getInstance();
            voice =vm.getVoice(VOICENAME);
            voice.allocate();
            Label_online.setVisible(false);
            Slider_LIVECHAT.setValue(0);
            Separator_belowName.setVisible(false);
            Panel_LiveChat.setVisible(false);
            
            GENDER=GENDER1;                                                     // user's GENDER (M or F)
         try{  
            
                DefaultListModel DLM=(DefaultListModel)L_BuddieList.getModel();
            // BACK END STARTs            
                Class.forName(Class_Setting.DRIVER_MANAGER);
                Connection con=DriverManager.getConnection(Class_Setting.URL+Class_Setting.DATABASE_NAME,Class_Setting.USERNAME,Class_Setting.PASSWORD);
                stmt=con.createStatement();
                
                TA_msg.setEnabled(false);
                jLabel10.setVisible(false);                                     // jLabel 10 is a 'chat activated with ' jLabel
                sendpanel.setEnabled(false);
                Label_malelogo.setVisible(false);                               // malelogo is a male user LOGO jLabel
                Label_imageprint.setVisible(false);                             // imageprint is a jLable to print PRO PIC
                Label_femalelogo.setVisible(false);                             // femalelogo is a female user LOGO jLabel
                Button_RemoveFriend.setVisible(false);
                        
                UEMAIL=EMAIL;
                UPASS=PASS;
                UFNAME=FNAME;
                uname.setText(UFNAME);                                          // uname is a jLabel to set user's first name
                Label_Fname.setVisible(false);
                UserEmail_Label.setText(UEMAIL);
       
             try{
                        
                 // In user's Table
                    String query="";
                    query="select sname from profile_"+ChangeToValidTableName(UEMAIL)+";";
                    ResultSet rs=stmt.executeQuery(query);
                    rs.last();
                    USNAME=rs.getString("sname");
                    
                    query="Select * from chats_"+ChangeToValidTableName(UEMAIL)+";";
                    rs=stmt.executeQuery(query);
                                    
                    
                    while(rs.next())                                
                        {                                                       // To set friends NAME in buddielist jList
                            DLM.addElement(" ("+rs.getString("fname")+") "+rs.getString("femail"));
                            System.out.println(rs.getString("femail"));
                        }
                                    
                            
                            
                }
             catch(Exception e2)
                {
                    JOptionPane.showMessageDialog(rootPane, e2.getMessage());
                }
       
                    ResultSet rs = stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(UEMAIL)+";");            // Extracting PRO PIC from profile table of user
                    String filename = "";             
                    rs.last(); 
                    {
                                                                                // conversion of PRO PIC from buytes to JPEG format 
                        Blob test = rs.getBlob("image");         
                        InputStream x = test.getBinaryStream();                 
                        int size = x.available();                 

                        byte b[] = new byte[size];                 
                        x.read(b);                 

                    } 



                        byte imag[] = rs.getBytes("image");
                        Image img = Toolkit.getDefaultToolkit().createImage(imag);

                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));

                        Label_imageprint.setVisible(true);
                        Label_imageprint.setLocation(1000, 0);
                        Label_imageprint.setIcon(imageIcon);

                        Label_imageprint.setLocation(890, 0);

        
        
     
   
            }
         catch(Exception e)
            {                                                                   // if there is no pro pic
                Label_imageprint.setVisible(false);                                                                            
               
                if(GENDER.equals("M"))                                          // if user is a male
                    {   
                        Label_malelogo.setVisible(true);
                        System.out.print("here1");
                        Label_malelogo.setLocation(890, 0);                     // malelogo is a jLabel LOGO for Male
        
                                    
                    }
                else 
                    { 
                        Label_femalelogo.setVisible(true);
                        Label_femalelogo.setLocation(890, 0);
                    }
                        System.out.print("here");

               
        
            }
        // TASK END
        SwingUtilities.invokeLater(new Runnable()
                    {                                                               //do swing work on EDT
                      
                        public void run()
                        {
                        d.dispose();
                        }
                    }                       );
        }
    };
                t.start();
                d.setVisible(true); 
        // Loading portion end : which is set to display load        
        
        //TIMER   for updating my last seen               
        LastSeentimer= new Timer();
         LastSeen_Task=new TimerTask()
    {  
        public void run()
           {
                try{

                        String query="";
                        query="Update profile_"+ChangeToValidTableName(UEMAIL)+" set lastseen=now();";
                        stmt.executeUpdate(query);
                        System.out.println("chal ra he");

                    }
                catch(Exception e)
                    {JOptionPane.showConfirmDialog(null,"Something went wrong.Please Try again.");}
            }
                
                
                
                
    };
            LastSeentimer.scheduleAtFixedRate(LastSeen_Task, 0, 5000);

 
}   // constructor CLOSE       
    
        // METHOD    
    public void Print_loading()
{
        
        JPanel p1 = new JPanel(new GridBagLayout());
                JLabel jLabel1=new JLabel();
                jLabel1.setIcon(new ImageIcon("src\\images\\Loading_gif.gif"));
                p1.add( jLabel1,new GridBagConstraints());
                
                d.getContentPane().add(p1);
                d.setSize(251,246);

                d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                d.setModal(true);
                d.setUndecorated(true);
       
                d.setLocationRelativeTo(null);
}
    
    // METHOD To change email to dname 
    String ChangeToValidTableName(String a)                                     
{
  
        String validname=""; 
        
        for(int i=0;i<a.length();i++)
        {
            char k=0;
            k=a.charAt(i);
            if(k=='.' || k=='@');
            else
                validname+=k;

        }
            
        return validname;
}

    
    
// TAKING CHAT AND LASTSEEN METHOD    
    
    void UpdatingChat(String ChatID,String UEMAIL)
{ 
  try{    

          timer= new Timer();
          UpdatingChat_Task=new TimerTask()
            {
                       public void run()
                    {
                      try{        
                            String Fullchat="";
                            String query="";
                                       
                            query="select lastseen from profile_"+ChangeToValidTableName(ChatID)+";";
                            ResultSet rs=stmt.executeQuery(query);
                            rs.last();
                            System.out.println("yha");          
                            lastseenlable.setText(rs.getString(1));
                            if(!rs.getString(1).equals(FRIENDLASTSEEN) && !rs.getString(1).equals("0") && 1<i++ )
                            {
                                Label_online.setVisible(true);
                                lastseenlable.setVisible(false);
                            }
                            else
                            {
                                lastseenlable.setVisible(true);
                                Label_online.setVisible(false);
                            }
                            
                            FRIENDLASTSEEN=rs.getString(1);
                                       
                            query="select count(*) from "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+";";
                            rs=stmt.executeQuery(query);
                            rs.last();
                            if(Integer.parseInt(rs.getString(1))>CHATMSGNO)             // IF NEW MSG ARRIVES
                            {
                                int CHATFROM=CHATMSGNO; 
                                CHATMSGNO=Integer.parseInt(rs.getString(1));
                                
                                query="select * from "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+";";
                                rs=stmt.executeQuery(query);
                                rs.absolute(CHATFROM);
                                while(rs.next())
                                {
                                    String chat=rs.getString("fullchat");
                                    Fullchat+=chat;
                                }
                                TA_ChatArea.append(Fullchat);
                                TA_ChatArea.setAutoscrolls(true);
                                    //  LIVE CHAT Portion
                                if(Slider_LIVECHAT.getValue()==1)
                                {
                                    voice.speak(Fullchat );
                                }
                                    // LIVE CHAT PORTION END
                                            
                                msg_arrive();                                   // IT is a method 
                            }
                         }
                      catch(Exception e)
                      {System.out.println(" Something went wrong.Please Try again.");}
                      

               
               
                    }
            };
        timer.scheduleAtFixedRate(UpdatingChat_Task, 0, 10000);
        
    }
    catch(Exception m)
    {JOptionPane.showMessageDialog(null," Something went wrong.Please Try again.");}
            
} // Method closed.
        
     // MESSAGE ARRIVE SOUND
    void msg_arrive()                                                         
{
    InputStream music;  
        try{
                music=new FileInputStream(new File("src\\audio\\msg_arrive_tone.wav"));
                AudioStream audios=new AudioStream(music);
                AudioPlayer.player.start(audios);
                 
            }
        catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,"Connect your speakers.");
            }
} // Method closed

    // REFRESH METHOD
    void refresh()
{
        new chatpage(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);dispose();   
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor. ///\\\
     * 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        uname = new javax.swing.JLabel();
        Label_Fname = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        friendpic = new javax.swing.JLabel();
        Panel_LiveChat = new javax.swing.JPanel();
        Label_Livechat = new javax.swing.JLabel();
        Slider_LIVECHAT = new javax.swing.JSlider();
        Separator_belowName = new javax.swing.JSeparator();
        Logout_Label = new javax.swing.JLabel();
        Refresh_jLabel = new javax.swing.JLabel();
        Label_typemsg = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        L_BuddieList = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        Button_QandF = new javax.swing.JButton();
        Label_femalelogo = new javax.swing.JLabel();
        Label_malelogo = new javax.swing.JLabel();
        Label_imageprint = new javax.swing.JLabel();
        lastseenlable = new javax.swing.JLabel();
        Label_online = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TA_ChatArea = new javax.swing.JTextArea();
        sendpanel = new javax.swing.JPanel();
        Button_sending = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        TA_msg = new javax.swing.JTextArea();
        Button_AddFriend = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TF_AddFriend = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        UserEmail_Label = new javax.swing.JLabel();
        Button_RemoveFriend = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        print = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home");
        setMinimumSize(new java.awt.Dimension(1010, 651));
        setResizable(false);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));

        jLabel5.setFont(new java.awt.Font("Tekton Pro", 0, 75)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/front.gif"))); // NOI18N
        jLabel5.setToolTipText("asapp v1.0.0");

        uname.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        uname.setForeground(new java.awt.Color(255, 255, 255));
        uname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Label_Fname.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        Label_Fname.setForeground(new java.awt.Color(204, 255, 255));
        Label_Fname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Fname.setToolTipText("go to Friend Profile");
        Label_Fname.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_Fname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_FnameMouseClicked(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Chat activated with -");

        Panel_LiveChat.setBackground(new java.awt.Color(0, 102, 204));
        Panel_LiveChat.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Panel_LiveChat.setForeground(new java.awt.Color(255, 255, 255));
        Panel_LiveChat.setToolTipText("iSay :\nit will speak aloud\nyour messages.");
        Panel_LiveChat.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        Panel_LiveChat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Panel_LiveChat.setName("iSay"); // NOI18N

        Label_Livechat.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        Label_Livechat.setForeground(new java.awt.Color(255, 255, 255));
        Label_Livechat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Livechat.setText("Live Chat OFF");

        Slider_LIVECHAT.setMaximum(1);
        Slider_LIVECHAT.setToolTipText("Live Chat");
        Slider_LIVECHAT.setValue(0);
        Slider_LIVECHAT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Slider_LIVECHAT.setOpaque(false);
        Slider_LIVECHAT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Slider_LIVECHATMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout Panel_LiveChatLayout = new javax.swing.GroupLayout(Panel_LiveChat);
        Panel_LiveChat.setLayout(Panel_LiveChatLayout);
        Panel_LiveChatLayout.setHorizontalGroup(
            Panel_LiveChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_LiveChatLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(Slider_LIVECHAT, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addComponent(Label_Livechat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Panel_LiveChatLayout.setVerticalGroup(
            Panel_LiveChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LiveChatLayout.createSequentialGroup()
                .addComponent(Label_Livechat)
                .addGap(16, 16, 16)
                .addComponent(Slider_LIVECHAT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Separator_belowName.setForeground(new java.awt.Color(255, 255, 255));
        Separator_belowName.setOpaque(true);

        Logout_Label.setForeground(new java.awt.Color(255, 255, 255));
        Logout_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit-button1.png"))); // NOI18N
        Logout_Label.setText("Log Out");
        Logout_Label.setToolTipText("Log Out");
        Logout_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Logout_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Logout_LabelMouseClicked(evt);
            }
        });

        Refresh_jLabel.setForeground(new java.awt.Color(255, 255, 255));
        Refresh_jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        Refresh_jLabel.setText("Refresh");
        Refresh_jLabel.setToolTipText("Refresh this page.");
        Refresh_jLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Refresh_jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Refresh_jLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_LiveChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Logout_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Refresh_jLabel)))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(friendpic, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(Separator_belowName, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(Label_Fname, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134)))
                        .addComponent(uname, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(uname, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(friendpic, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Label_Fname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(Separator_belowName, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logout_Label)
                    .addComponent(Refresh_jLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(Panel_LiveChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_LiveChat.getAccessibleContext().setAccessibleName("iSay");
        Panel_LiveChat.getAccessibleContext().setAccessibleDescription("iSay");

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 890, 120);

        Label_typemsg.setForeground(new java.awt.Color(102, 102, 102));
        Label_typemsg.setText("Type a message . . .");
        getContentPane().add(Label_typemsg);
        Label_typemsg.setBounds(260, 430, 160, 20);

        L_BuddieList.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        L_BuddieList.setModel(new DefaultListModel());
        L_BuddieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        L_BuddieList.setToolTipText("Select to chat.");
        L_BuddieList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        L_BuddieList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                L_BuddieListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(L_BuddieList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 220, 230, 290);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("B H A R A T           S H R I V A S T A V A ' S             P R O D U C T I O N             A S A P P         © 2 0 1 6");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(250, 560, 421, 10);

        Button_QandF.setBackground(new java.awt.Color(0, 102, 255));
        Button_QandF.setForeground(new java.awt.Color(255, 255, 255));
        Button_QandF.setText("Feedback or Query");
        Button_QandF.setToolTipText("asapp");
        Button_QandF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_QandF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_QandFActionPerformed(evt);
            }
        });
        getContentPane().add(Button_QandF);
        Button_QandF.setBounds(830, 570, 140, 23);

        Label_femalelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/femalelogo.png"))); // NOI18N
        Label_femalelogo.setToolTipText("My Profile");
        Label_femalelogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_femalelogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_femalelogoMouseClicked(evt);
            }
        });
        getContentPane().add(Label_femalelogo);
        Label_femalelogo.setBounds(900, 10, 100, 110);

        Label_malelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/malelogo.png"))); // NOI18N
        Label_malelogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_malelogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_malelogoMouseClicked(evt);
            }
        });
        getContentPane().add(Label_malelogo);
        Label_malelogo.setBounds(900, 10, 100, 110);

        Label_imageprint.setToolTipText("My Profile");
        Label_imageprint.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Label_imageprint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_imageprint.setMaximumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setMinimumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setPreferredSize(new java.awt.Dimension(102, 102));
        Label_imageprint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_imageprintMouseClicked(evt);
            }
        });
        getContentPane().add(Label_imageprint);
        Label_imageprint.setBounds(890, 0, 120, 120);

        lastseenlable.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lastseenlable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lastseenlable);
        lastseenlable.setBounds(520, 130, 220, 21);

        Label_online.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        Label_online.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_online.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online.gif"))); // NOI18N
        Label_online.setText("online ");
        getContentPane().add(Label_online);
        Label_online.setBounds(520, 130, 210, 20);

        TA_ChatArea.setEditable(false);
        TA_ChatArea.setBackground(new java.awt.Color(255, 255, 204));
        TA_ChatArea.setColumns(20);
        TA_ChatArea.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        TA_ChatArea.setLineWrap(true);
        TA_ChatArea.setRows(5);
        TA_ChatArea.setWrapStyleWord(true);
        TA_ChatArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(TA_ChatArea);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(250, 130, 750, 280);

        sendpanel.setBackground(new java.awt.Color(255, 255, 255));

        Button_sending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sending.gif"))); // NOI18N
        Button_sending.setToolTipText("Send");
        Button_sending.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_sending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_sendingMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sendpanelLayout = new javax.swing.GroupLayout(sendpanel);
        sendpanel.setLayout(sendpanelLayout);
        sendpanelLayout.setHorizontalGroup(
            sendpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Button_sending, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sendpanelLayout.setVerticalGroup(
            sendpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendpanelLayout.createSequentialGroup()
                .addComponent(Button_sending)
                .addGap(0, 49, Short.MAX_VALUE))
        );

        getContentPane().add(sendpanel);
        sendpanel.setBounds(910, 430, 60, 90);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(1);
        jTextArea2.setRows(1);
        jTextArea2.setTabSize(0);
        jTextArea2.setBorder(null);
        jScrollPane4.setViewportView(jTextArea2);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(910, 420, 70, 110);

        TA_msg.setColumns(20);
        TA_msg.setFont(new java.awt.Font("Segoe UI Symbol", 0, 13)); // NOI18N
        TA_msg.setLineWrap(true);
        TA_msg.setRows(5);
        TA_msg.setToolTipText("Your message.");
        TA_msg.setWrapStyleWord(true);
        TA_msg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TA_msgFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TA_msgFocusLost(evt);
            }
        });
        jScrollPane3.setViewportView(TA_msg);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(250, 420, 670, 110);

        Button_AddFriend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InviteMemberIcon1.png"))); // NOI18N
        Button_AddFriend.setToolTipText("Add friend");
        Button_AddFriend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_AddFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_AddFriendMouseClicked(evt);
            }
        });
        getContentPane().add(Button_AddFriend);
        Button_AddFriend.setBounds(180, 130, 70, 70);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Type Friend's Email Address.");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 150, 160, 40);

        TF_AddFriend.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TF_AddFriendFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TF_AddFriendFocusLost(evt);
            }
        });
        getContentPane().add(TF_AddFriend);
        TF_AddFriend.setBounds(10, 140, 160, 50);

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Add friend to talk with.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 120, 130, 20);

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("My Buddies :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 200, 220, 18);

        UserEmail_Label.setBackground(new java.awt.Color(255, 255, 255));
        UserEmail_Label.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        UserEmail_Label.setForeground(new java.awt.Color(255, 255, 204));
        UserEmail_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserEmail_Label.setToolTipText("");
        getContentPane().add(UserEmail_Label);
        UserEmail_Label.setBounds(370, 600, 250, 20);

        Button_RemoveFriend.setBackground(new java.awt.Color(255, 255, 255));
        Button_RemoveFriend.setForeground(new java.awt.Color(102, 102, 102));
        Button_RemoveFriend.setText("Remove Friend");
        Button_RemoveFriend.setToolTipText("Remove this friend from my buddie list.");
        Button_RemoveFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_RemoveFriendActionPerformed(evt);
            }
        });
        getContentPane().add(Button_RemoveFriend);
        Button_RemoveFriend.setBounds(10, 510, 230, 23);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/silver-light-blue-wave-abstract-backgrounds-powerpoint-1024x725.jpg"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(0, 120, 1010, 510);

        print.setBackground(new java.awt.Color(0, 102, 204));
        print.setOpaque(true);
        getContentPane().add(print);
        print.setBounds(880, 0, 130, 120);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Button_QandFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_QandFActionPerformed
    if(timer!=(null))
        {
            timer.cancel();
            UpdatingChat_Task.cancel();
        }
            LastSeentimer.cancel();
            LastSeen_Task.cancel();
    new feedback(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);dispose();               
    }//GEN-LAST:event_Button_QandFActionPerformed

    private void TF_AddFriendFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TF_AddFriendFocusGained
jLabel3.setVisible(false);       
    }//GEN-LAST:event_TF_AddFriendFocusGained
// ADD FRIEND BUTTON (add friend to buddie list)
    private void Button_AddFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_AddFriendMouseClicked
try{
        String FEMAIL=TF_AddFriend.getText();                                            // FEMAIL is a friend's EMAIL
        String FDNAME=ChangeToValidTableName(FEMAIL);                                 // DNAME is friend's Table's name
                        
        String query="";                                                        // OPEN FRIEND'S Portion
        boolean error1=false;   
        
        try {    
     
                stmt.executeQuery("Select count(*) from profile_"+FDNAME+";");
                                
            }
        catch(Exception er)
            {
                    error1=true;
            }
                    // IF friend doesn't exist
        if(error1 && !FEMAIL.isEmpty())
            {               
                if(JOptionPane.showConfirmDialog(null,"Your friend doesn't exist in assap\n "
                                                        + "Do you want to invite her/him ?" )==0)
                    {
                        String FROM, FROMPASS, SUB, MAILMSG;
                        String [] TO={FEMAIL};
                        SUB="Your ASAPP account has succesfully created.";
                        MAILMSG="Hey ! "+FEMAIL+" "+UFNAME+" signed up for ASAPP. \n "
                                            +"and "+ UFNAME+" just added you as a Friend.\n  "+
                                            "To connect and chat with "+UFNAME+", sign up in ASAPP.\n"+
                                            " hope that you enjoy the application.\n\n"+
                                            "|| Feel free to ask and welcome for the feedback and suggestions.||\n"
                                            + "(You can send us on this address too, by reply.)\n\n"+
                                            " \t B H A R A T   S H R I V A S T A V A   P R O D U C T I O N   © 2016";
                        Class_Mail.sendFromGMail( TO, SUB, MAILMSG);
                    }
                
                TF_AddFriend.setText("");
                jLabel3.setVisible(true);
            }
        else  if(!(error1 && FEMAIL.isEmpty()))          // IF friend exist.
            {
            
             boolean error_Friend_duplicate=false; 
                if(FEMAIL.equals(UEMAIL))
                    {
                        JOptionPane.showMessageDialog(null,"Sorry, You can't chat with yourself.");
                        error_Friend_duplicate=true;
                    }
                if(!error_Friend_duplicate)
                    {      
                    try{
                        query="create table "+FDNAME+"_"+ChangeToValidTableName(UEMAIL)+"( fullchat varchar(2000));";
                        stmt.executeUpdate(query);
                                            
                        }
                    catch(Exception eo)
                        {
                            JOptionPane.showMessageDialog(null, "Friend is in your buddie list. "+eo.getMessage());
                            error_Friend_duplicate=true;
                        }
                 
                                        
               
                    }
                if(!error_Friend_duplicate)
                    {  
                                       // In friend's portion
                        query="Select * from profile_"+FDNAME;
                        ResultSet rs=stmt.executeQuery(query);
                        rs.last();

                        String REALEMAIL=rs.getString("email");
                        String REALPASS=rs.getString("pass");
                        String FNAME=rs.getString("name");
                        String FSNAME=rs.getString("sname");
                        lastseenlable.setText(rs.getString("lastseen"));
                        String GENDER=rs.getString("GENDER");
                        jLabel10.setVisible(true);              
                                        
                       query="Insert into chats_"+FDNAME+" values('"+UEMAIL+"','"+UFNAME+"','"+USNAME+"');";
                       stmt.executeUpdate(query);
                       DefaultListModel DLM=(DefaultListModel)L_BuddieList.getModel();
                       DLM.addElement( " ("+FNAME+") "+REALEMAIL );
                       Label_Fname.setText(FNAME+" "+FSNAME);

                                             //IN USER's Portion    

                       query="create table "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(REALEMAIL)+"( fullchat varchar(2000));";
                       stmt.executeUpdate(query);
                       query="Insert into chats_"+ChangeToValidTableName(UEMAIL)+" values('"+REALEMAIL+"','"+FNAME+"','"+FSNAME+"');";
                       stmt.executeUpdate(query);
                      System.out.println("my :"+UEMAIL+" f: "+FEMAIL);

                    }
                        TF_AddFriend.setText("");
                                     
            }
                        
                        
    }
catch(Exception e)
    {
            JOptionPane.showMessageDialog(null,"Something went wrong.Please Try again.");
    }

    }//GEN-LAST:event_Button_AddFriendMouseClicked

    private void TF_AddFriendFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TF_AddFriendFocusLost
    if(TF_AddFriend.getText().isEmpty())
        jLabel3.setVisible(true);   
    }//GEN-LAST:event_TF_AddFriendFocusLost
// BuddieList (jList) 
    private void L_BuddieListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_L_BuddieListMouseClicked
Thread t = new Thread()
{
   public  void run()
    {
    // TASK BEGIN 

        i=1;
        Label_online.setVisible(false);
        if(L_BuddieList.getMinSelectionIndex()!=-1)
        {   
        ResultSet rs=null;

        String query;
        TA_ChatArea.setText("");
        String chatname=(String)L_BuddieList.getSelectedValue();
        Label_Fname.setVisible(true);
        TA_msg.setEnabled(true);
        sendpanel.setEnabled(true);
    
        try 
            {       
                chatID="";int i;
                for(i=0;i<chatname.length();i++)                                   // TO GET FRIEND'S FIRST NAME  
                    {
                        char k=chatname.charAt(i);
                        if(k==')')
                        break;
                    } 
                       i+=2;

                for(;i<chatname.length();i++)                                    // TO GET EMAIL OF FRIEND'S
                    {
                        char k=chatname.charAt(i);
                        chatID+=k;
                    }

                System.out.println(chatID);


                       // In user's Portion
                query="Select * from chats_"+ChangeToValidTableName(UEMAIL)+" where femail='"+chatID+"';";
                System.out.println(query);
                rs=stmt.executeQuery(query);

                rs.last();
                Label_Fname.setText(rs.getString("fname")+" "+rs.getString("sname"));
                jLabel10.setVisible(true);
                        
                query="select count(*) from "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+";";
                rs=stmt.executeQuery(query);
                rs.last();
                CHATMSGNO=Integer.parseInt(rs.getString(1));
                        
                query="Select * from "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+";";
                rs=stmt.executeQuery(query);
                while(rs.next())
                    {
                       String Fullchat=rs.getString("fullchat");
                       TA_ChatArea.append(Fullchat+"\n");
                    }

                boolean error2=false;
                // IN FRIEND"S PROFILE
                query="select * from profile_"+ChangeToValidTableName(chatID)+";";
                try{
                          stmt.executeQuery(query);
                    }
                catch(Exception er)
                    {
                                   error2=true;

                                   JOptionPane.showMessageDialog(rootPane, chatname +" has diactivated asapp account.");
                    }
                if(!error2 )                        // IF FRIEND EXIST.
                    {
                        rs = stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(chatID)+";");            // Extracting PRO PIC from profile table of user
                               
                                            // Extracting PRO PIC from profile table of user
                        String filename = "";             
                        boolean image_error=false;
                        rs.last();
                        
                        try{                                                                               // conversion of PRO PIC from buytes to JPEG format 
                                Blob test = rs.getBlob("image");
                                InputStream x = test.getBinaryStream();                 
                                int size = x.available();                 
                                byte b[] = new byte[size];                 
                                x.read(b);                 

                            }
                        catch(Exception w)
                            {   image_error=true;
                                System.out.print("image doesn't exist.");
                                query="Select gender from profile_"+ChangeToValidTableName(chatID) +";";
                                rs=stmt.executeQuery(query);
                                rs.last();
                                if((rs.getString(1)).equals("M"))                                                         // if user is a male
                                    {   
                                        friendpic.setVisible(true);
                                        System.out.print("here1");
                                        ImageIcon imageIcon = new ImageIcon("src\\images\\friendpic_male.png");
                                        friendpic.setIcon(imageIcon);
                                    }
                                else 
                                    { 
                                        friendpic.setVisible(true);
                                        System.out.print("here1");
                                        ImageIcon imageIcon = new ImageIcon("src\\images\\friendpic_female.png");
                                        friendpic.setIcon(imageIcon);
                                    }
                            }
                        if(!image_error)
                            {
                                byte imag[] = rs.getBytes("image");
                                Image img = Toolkit.getDefaultToolkit().createImage(imag);
                                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(51, 54, Image.SCALE_DEFAULT));
                                friendpic.setIcon(imageIcon);
                            }  
                                Button_RemoveFriend.setVisible(true);
                                
                                query="Select lastseen from profile_"+ChangeToValidTableName(chatID)+";";
                                ResultSet lsrs=stmt.executeQuery(query);
                                lsrs.last();
                                lastseenlable.setText(lsrs.getString(1));
                                
                            // LIVECHAT arrangment PROTION
                                Panel_LiveChat.setVisible(true);
                            // LIVECHAT PORTION END
                                Separator_belowName.setVisible(true);
                            if(timer!=null)
                                { timer.cancel(); }
                               UpdatingChat(chatID,UEMAIL);
                    }

            }
        catch(Exception e)
            {
            JOptionPane.showConfirmDialog(null,"Something went wrong.Please Try again.");
            System.out.print(e.getMessage());
            }
        }
        
    // TASK END
    SwingUtilities.invokeLater(new Runnable()
                    {                                                               //do swing work on EDT
                      
                        public void run()
                        {
                        d.dispose();
                        }
                    }                       );
    }
};
                t.start();
                d.setVisible(true); 
 
    }//GEN-LAST:event_L_BuddieListMouseClicked

    private void TA_msgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TA_msgFocusGained
Label_typemsg.setVisible(false);       
    }//GEN-LAST:event_TA_msgFocusGained

    private void TA_msgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TA_msgFocusLost
if(TA_msg.getText().isEmpty())
        Label_typemsg.setVisible(true);       
    }//GEN-LAST:event_TA_msgFocusLost

    private void Label_imageprintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_imageprintMouseClicked
            if(timer!=(null))
                {
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                }
                LastSeentimer.cancel();
                LastSeen_Task.cancel();
        new MyProfile(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);
                dispose();
    }//GEN-LAST:event_Label_imageprintMouseClicked

    private void Label_femalelogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_femalelogoMouseClicked
            if(timer!=(null))
                {
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                }
                LastSeentimer.cancel();
                LastSeen_Task.cancel();
        new MyProfile(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);
                dispose();   
    }//GEN-LAST:event_Label_femalelogoMouseClicked

    private void Label_malelogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_malelogoMouseClicked
            if(timer!=(null))
                {
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                }
                LastSeentimer.cancel();
                LastSeen_Task.cancel();
        new MyProfile(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);
                dispose();        
    }//GEN-LAST:event_Label_malelogoMouseClicked
// send button GIF
    private void Button_sendingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_sendingMouseClicked
Thread t = new Thread()
{ 
    public  void run()
    {
    // TASK BEGIN 

        if(!TA_msg.getText().isEmpty())
        {
         try{
                String chat=UFNAME+" :\n\t"+TA_msg.getText()+"\n";              // UFNAME is User's first name
        
               // This is to save chat in userTABLE_friendTABLE
                String query="insert into "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+" values(\""+chat+"\");";
                System.out.println(query);
                stmt.executeUpdate(query);
                TA_ChatArea.append(chat);
        
               // This is to save chat in friendTABLE_userTABLE
        
                query="insert into "+ChangeToValidTableName(chatID)+"_"+ChangeToValidTableName(UEMAIL)+" values(\""+chat+"\");";
                System.out.println(query);
                stmt.executeUpdate(query);
        
                TA_msg.setText("");
                CHATMSGNO++;
            }
         catch(Exception e)
            {JOptionPane.showMessageDialog(rootPane,"Something went wrong.Please Try again.");}
        }
    // TASK END
    SwingUtilities.invokeLater(new Runnable()
                    {                                                               //do swing work on EDT
                      
                        public void run()
                        {
                        d.dispose();
                        }
                    }                       );
    }
};
                t.start();
                d.setVisible(true); 
 
    }//GEN-LAST:event_Button_sendingMouseClicked
// To open friend's profile    
    private void Label_FnameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_FnameMouseClicked
            
                try{
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                
                LastSeentimer.cancel();
                LastSeen_Task.cancel();
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null, "Something went wrong.Please Try again.");
                }
        new Fprofile(UEMAIL,UPASS,GENDER,UFNAME,chatID).setVisible(true); dispose();// ChatID is friend's email 
    }//GEN-LAST:event_Label_FnameMouseClicked

    private void Slider_LIVECHATMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Slider_LIVECHATMouseReleased
if(Slider_LIVECHAT.getValue()==1)                        
{
    if(JOptionPane.showConfirmDialog(null, "Your chat is LIVE now."+"\n Make sure you are connected with 3G or 4G network.")==0)
    { 
        Label_Livechat.setText("Live chat ON");
        voice.speak(UFNAME+" ,,, have, fun, with, LIVE Chat.");
    }   
    else
        Slider_LIVECHAT.setValue(0);
}
else
{
            JOptionPane.showMessageDialog(null, "LIVE chat OFF."+
                    "\n Thank you for using iSay"
                    );
            Label_Livechat.setText("Live chat OFF"); 
            voice.speak(UFNAME+" ,,, we hope you enjoyed, Live chat.");
           
}    
    }//GEN-LAST:event_Slider_LIVECHATMouseReleased

    private void Logout_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Logout_LabelMouseClicked
int a=1;
    a = 1 + (int)(Math.random() * 2);
        try{
            if(timer!=(null))
                {
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                }
                    LastSeentimer.cancel();
                    LastSeen_Task.cancel();
                    new LogIn(a).setVisible(true);dispose();       
            }
        catch(Exception e)
            {
            JOptionPane.showMessageDialog(null,"Something went wrong.Please Try again.");
            }
    }//GEN-LAST:event_Logout_LabelMouseClicked

    private void Button_RemoveFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_RemoveFriendActionPerformed
Thread t = new Thread()
{ 
    public  void run()
    {
    // TASK BEGIN 
        try{
                // IN USER'S PORTION
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                    
                    String query="drop table "+ChangeToValidTableName(UEMAIL)+"_"+ChangeToValidTableName(chatID)+";";
                    stmt.executeUpdate(query);
                    query="delete from chats_"+ChangeToValidTableName(UEMAIL)+" where femail='"+chatID+"';";
                    stmt.executeUpdate(query);
                  
                // IN FRIEND'S PORTION
                    
                    query="drop table "+ChangeToValidTableName(chatID)+"_"+ChangeToValidTableName(UEMAIL)+";";
                    stmt.executeUpdate(query);
                    query="delete from chats_"+ChangeToValidTableName(chatID)+" where femail='"+(UEMAIL)+"';";
                    stmt.executeUpdate(query);
                    
                // IN USER'S PORTION
                    
                    query="Select * from chats_"+ChangeToValidTableName(UEMAIL)+";";
                    ResultSet rs=stmt.executeQuery(query);
                    DefaultListModel DLM=(DefaultListModel)L_BuddieList.getModel();
                    DLM.clear();
                        while(rs.next())                                
                            {                                                   // To set friends NAME in buddielist jList
                                DLM.addElement(" ("+rs.getString("fname")+") "+rs.getString("femail"));
                                System.out.println(rs.getString("femail"));
                            }
                    
                    L_BuddieList.clearSelection();
                    Button_RemoveFriend.setVisible(false);
                    JOptionPane.showMessageDialog(null," '"+chatID +"' has removed from your buddie list succesfully.");
                
                // To refresh page : 
                    refresh();
            }
        catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,"Something went wrong.Please Try again.");
                System.out.println(e.getMessage());
            }
        
    // TASK END
    SwingUtilities.invokeLater(new Runnable()
                    {                                                               //do swing work on Event Dispatch Thread
                      
                        public void run()
                        {
                        d.dispose();
                        }
                    }                       );
    }
};
                t.start();
                d.setVisible(true); 
 
    }//GEN-LAST:event_Button_RemoveFriendActionPerformed

    private void Refresh_jLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Refresh_jLabelMouseClicked
            if(timer!=(null))
                {
                    timer.cancel();
                    UpdatingChat_Task.cancel();
                }
                    LastSeentimer.cancel();
                    LastSeen_Task.cancel();
        
        refresh();     
    }//GEN-LAST:event_Refresh_jLabelMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new chatpage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Button_AddFriend;
    private javax.swing.JButton Button_QandF;
    private javax.swing.JButton Button_RemoveFriend;
    private javax.swing.JLabel Button_sending;
    private javax.swing.JList L_BuddieList;
    private javax.swing.JLabel Label_Fname;
    private javax.swing.JLabel Label_Livechat;
    private javax.swing.JLabel Label_femalelogo;
    private javax.swing.JLabel Label_imageprint;
    private javax.swing.JLabel Label_malelogo;
    private javax.swing.JLabel Label_online;
    private javax.swing.JLabel Label_typemsg;
    private javax.swing.JLabel Logout_Label;
    private javax.swing.JPanel Panel_LiveChat;
    private javax.swing.JLabel Refresh_jLabel;
    private javax.swing.JSeparator Separator_belowName;
    private javax.swing.JSlider Slider_LIVECHAT;
    private javax.swing.JTextArea TA_ChatArea;
    private javax.swing.JTextArea TA_msg;
    private javax.swing.JTextField TF_AddFriend;
    private javax.swing.JLabel UserEmail_Label;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel friendpic;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lastseenlable;
    private javax.swing.JLabel print;
    private javax.swing.JPanel sendpanel;
    private javax.swing.JLabel uname;
    // End of variables declaration//GEN-END:variables
}
