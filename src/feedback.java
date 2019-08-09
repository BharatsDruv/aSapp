/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//asappapplication@gmail.com
//shrivastava


import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author sony
 */
public class feedback extends javax.swing.JFrame {
String UEMAIL,UPASS,GENDER,UFNAME,USNAME;                                                  // Global Variables 
Timer LastSeentimer ;Statement stmt=null;
TimerTask LastSeen_Task;
final JDialog d=new JDialog();
    /**
     * Creates new form LogIn
     */
     public feedback()
     {
         initComponents();
        
     }
    public feedback(String UEMAIL1,String UPASS1,String GENDER1,String UFNAME1) // Variable takes from other jFrames
{  
        initComponents();
        
        Print_loading();
        
        UEMAIL=UEMAIL1;
        UPASS=UPASS1;
        GENDER=GENDER1;
        UFNAME=UFNAME1;
        Label_malelogo.setVisible(false);                       // malelogo is a male user LOGO jLabel
        Label_imageprint.setVisible(false);                     // imageprint is a jLable to print PRO PIC
        Label_femalelogo.setVisible(false);
        nichedekhopanel.setVisible(false);
        likebutton.setFocusable(true);
                        
    Thread t = new Thread()
    {
        public  void run()
        {
        // TASK BEGIN 
            ResultSet rs=null;String like="";  
            // BACK END STARTs
                        username.setText(UFNAME);
                        try{
                            Class.forName(Class_Setting.DRIVER_MANAGER);
                            Connection con=DriverManager.getConnection(Class_Setting.URL+Class_Setting.DATABASE_NAME,Class_Setting.USERNAME,Class_Setting.PASSWORD);
                            stmt=con.createStatement();
                         
                         // IN USER'S Portion
                            rs=stmt.executeQuery("Select * from profile_"+ChangeToValidTableName(UEMAIL)+";");   
                                                                                // To find out wheter user has LIKED asapp or not
                            rs.last();
                            like=rs.getString("liked");
                            System.out.print(rs.getString("liked")+"this"); 
                            if(like.equals("liked"))                                                     // If user has LIKED asapp
                                {        
                                    nichedekhopanel.setVisible(true);                                    // jPanel1 = Panel for 'niche dekho'
                                    nichedekhopanel.setLocation(440,210);                                // set Location of jPanel1 to jPanel2
                                    likepanel.setVisible(false);                                         // jPanel2 = Panel for LIKED button
                                    hitlike.setText("     Thank you "+UFNAME+" for Supporting asapp.");  // hitlike = jLable
                                }
                             
                            USNAME=rs.getString("sname");
                            
                        // To take PRO PIC from other field
                                rs= stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(UEMAIL)+";");    
                        
                            String filename = "";                                               // PRO pic name
                            rs.last(); 
                            {

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
                            Label_imageprint.setIcon(imageIcon);                // set icon of imageprint jLable
                                                                 

                            
                            
            // Back end off
             
                            }
                        catch(Exception e)
                            {
           
                                Label_imageprint.setVisible(false);                                                                            
               
                                if(GENDER.equals("M"))                          // if user is a male
                                {   
                                    Label_malelogo.setVisible(true);
                                    System.out.print("here1");
                                    Label_malelogo.setLocation(0,30);
                                                                                // malelogo is a jLabel LOGO for Male
                                }
                                else 
                                { 
                                    Label_femalelogo.setVisible(true);
                                    Label_femalelogo.setLocation(0,30);
                                }
                    
            
                                System.out.print(e.getMessage());               // print error
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
  
       
                        
    // TIMER FOR UPDATING MY LAST SEEN
        
     LastSeentimer = new Timer();
     LastSeen_Task = new TimerTask()
    {
        public void run()
        {
            
            
            
            try{
                        
                        // In USER'S Portion
              
                
                String query="Update profile_"+ChangeToValidTableName(UEMAIL)+" set lastseen=now();";
                stmt.executeUpdate(query);
                System.out.println("chal ra he feedback wala");
                
            }catch(Exception e)
            {JOptionPane.showConfirmDialog(null,"timer error"+e.getMessage());}
        }
        
        
        
        
    };
        
        LastSeentimer.scheduleAtFixedRate(LastSeen_Task, 0, 3000);
                      
                        
} // CONSTRUCTOR END 
    
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
    
    
 // Method to convert email to Table name for sql   
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
        jLabel7 = new javax.swing.JLabel();
        send_JButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        likepanel = new javax.swing.JPanel();
        likebutton = new javax.swing.JLabel();
        Label_feedResponce = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        l1 = new javax.swing.JLabel();
        home = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        feedbackarea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Label_malelogo = new javax.swing.JLabel();
        Label_femalelogo = new javax.swing.JLabel();
        Label_imageprint = new javax.swing.JLabel();
        nichedekhopanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        hitlike = new javax.swing.JLabel();
        website_button1 = new javax.swing.JButton();
        website_button2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Feedback");
        setMinimumSize(new java.awt.Dimension(1010, 651));
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("B H A R A T           S H R I V A S T A V A ' S             P R O D U C T I O N             A S A P P         © 2 0 1 6");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(270, 570, 421, 10);

        send_JButton.setBackground(new java.awt.Color(0, 102, 255));
        send_JButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        send_JButton.setForeground(new java.awt.Color(255, 255, 255));
        send_JButton.setText("Send");
        send_JButton.setToolTipText("Write Us");
        send_JButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        send_JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_JButtonActionPerformed(evt);
            }
        });
        getContentPane().add(send_JButton);
        send_JButton.setBounds(690, 370, 100, 30);

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("in your life.");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(730, 60, 280, 18);

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("ASAPP helps you connect with the people ");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(730, 40, 280, 18);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(260, 560, 470, 10);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(260, 360, 470, 10);

        likepanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        likebutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/output_MSOyQH.gif"))); // NOI18N
        likebutton.setToolTipText("Like asapp");
        likebutton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        likebutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                likebuttonMouseClicked(evt);
            }
        });
        likepanel.add(likebutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 140));

        getContentPane().add(likepanel);
        likepanel.setBounds(440, 210, 120, 140);

        Label_feedResponce.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        Label_feedResponce.setForeground(new java.awt.Color(255, 102, 102));
        Label_feedResponce.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(Label_feedResponce);
        Label_feedResponce.setBounds(190, 500, 660, 42);

        username.setFont(new java.awt.Font("Trebuchet MS", 1, 28)); // NOI18N
        username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(username);
        username.setBounds(120, 30, 130, 40);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/3.jpg"))); // NOI18N
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 0, 1010, 30);
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(120, 70, 130, 10);

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Feedback");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(220, 370, 80, 21);

        l1.setForeground(new java.awt.Color(102, 102, 102));
        l1.setText("Write to us ...");
        getContentPane().add(l1);
        l1.setBounds(320, 380, 80, 14);

        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home2.png"))); // NOI18N
        home.setToolTipText("Home");
        home.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });
        getContentPane().add(home);
        home.setBounds(250, 30, 45, 50);

        feedbackarea.setColumns(20);
        feedbackarea.setRows(5);
        feedbackarea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                feedbackareaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                feedbackareaFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(feedbackarea);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(310, 370, 370, 130);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asapp s.gif"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(430, 30, 70, 110);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asapp A.gif"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(360, 60, 80, 80);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asapp aa.gif"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(480, 70, 70, 70);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asapp P.gif"))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(580, 70, 50, 90);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asapp P.gif"))); // NOI18N
        getContentPane().add(jLabel11);
        jLabel11.setBounds(530, 70, 50, 90);

        Label_malelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/malelogo.png"))); // NOI18N
        Label_malelogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_malelogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_malelogoMouseClicked(evt);
            }
        });
        getContentPane().add(Label_malelogo);
        Label_malelogo.setBounds(890, 500, 100, 110);

        Label_femalelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/femalelogo.png"))); // NOI18N
        Label_femalelogo.setToolTipText("My Profile");
        Label_femalelogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_femalelogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_femalelogoMouseClicked(evt);
            }
        });
        getContentPane().add(Label_femalelogo);
        Label_femalelogo.setBounds(890, 490, 100, 110);

        Label_imageprint.setToolTipText("My Profile");
        Label_imageprint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Label_imageprint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Label_imageprint.setMaximumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setMinimumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setPreferredSize(new java.awt.Dimension(102, 102));
        getContentPane().add(Label_imageprint);
        Label_imageprint.setBounds(0, 30, 120, 120);

        nichedekhopanel.setBackground(new java.awt.Color(255, 255, 255));
        nichedekhopanel.setLayout(null);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hi.gif"))); // NOI18N
        jLabel9.setToolTipText("Hey.");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nichedekhopanel.add(jLabel9);
        jLabel9.setBounds(20, 0, 140, 90);

        getContentPane().add(nichedekhopanel);
        nichedekhopanel.setBounds(180, 230, 160, 100);

        hitlike.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        hitlike.setForeground(new java.awt.Color(102, 102, 102));
        hitlike.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hitlike.setText("Hit LIKE and support us to grow asapp community.");
        getContentPane().add(hitlike);
        hitlike.setBounds(150, 150, 710, 42);

        website_button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Website_gif.gif"))); // NOI18N
        website_button1.setToolTipText("aSapp Official Website");
        website_button1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        website_button1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        website_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                website_button1ActionPerformed(evt);
            }
        });
        getContentPane().add(website_button1);
        website_button1.setBounds(820, 170, 180, 290);

        website_button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Website_gif.gif"))); // NOI18N
        website_button2.setToolTipText("aSapp Official Website");
        website_button2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        website_button2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        website_button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                website_button2ActionPerformed(evt);
            }
        });
        getContentPane().add(website_button2);
        website_button2.setBounds(0, 170, 180, 290);

        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("For more info:");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(880, 156, 80, 14);

        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("For more info:");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(50, 156, 80, 14);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/silver-light-blue-wave-abstract-backgrounds-powerpoint-1024x725.jpg"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(0, 0, 1010, 630);

        pack();
    }// </editor-fold>//GEN-END:initComponents
// SEND BUTTON (feedback send)
    private void send_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_JButtonActionPerformed
Thread t = new Thread()
{
   public  void run()
    {
    // TASK BEGIN 
       
        boolean error_Send=false;
        try{
            
            
            String [] TO={"asappapplication@gmail.com"};                            // Sending to asapp's account
            
            String SUB="Feedback";
            String MAILMSG="By "+UFNAME+" "+USNAME+
                    "\n"+UEMAIL+
                    
                    
                    " :\n\t"+feedbackarea.getText();                                //  Feedback (Matter)
            Class_Mail.sendFromGMail( TO, SUB, MAILMSG);                              // Sends by using NewClass CLASS 
            
            }catch(Exception e)
            {
                error_Send=true;
                JOptionPane.showMessageDialog(rootPane, "Check your connection and try again.\n");
            } 
        if(!error_Send && !Class_Mail.connection_error)
        {
            feedbackarea.setText("");  
           Label_feedResponce.setText("Thank-You For your feedback.");
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
  

    }//GEN-LAST:event_send_JButtonActionPerformed

// LIKE BUTTON (in likepanel)
    private void likebuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_likebuttonMouseClicked
                       
Thread t = new Thread()
{
   public  void run()
    {
    // TASK BEGIN 
    
        System.out.println(likepanel.getLocation());
            
        String [] TO={UEMAIL};                                                      // sends thank you mail to user
            
        String SUB="LIKED .";
        String MAILMSG="Thank you ,"+UFNAME+" for liking and supporting asapp. \n\n"+
                    "|| Feel free to ask and welcome for the feedback and suggestions.||\n"
                    + "(You can send us on this address too, by reply.)\n\n"+
                    " \t B H A R A T   S H R I V A S T A V A   P R O D U C T I O N   © 2017";
            Class_Mail.sendFromGMail( TO, SUB, MAILMSG);
           if(!Class_Mail.connection_error)
           {
                                                                                    // 'niche dekho' jPanel1's location set of jPanel2
           
        // BACK END STARTs        
               try{
                        // IN USER's Portion
                        String query="update profile_"+ChangeToValidTableName(UEMAIL)+" set liked='liked';";               // Updating profilr table and set valued of LIKED column 'liked'
                        stmt.executeUpdate(query);
        // Back end off            
                        hitlike.setText("Thank you "+UFNAME+" for Supporting asapp.");              // hitlike is a jLabel
                        likepanel.setVisible(false);                                                  // LIKED button panel displaying OFF
                        nichedekhopanel.setVisible(true);                                                   // 'niche dekho' jPanel1 displaying ON
                        nichedekhopanel.setLocation(440,210); 
                }catch(Exception e)
                    {                                                                           // if error occurs
                
                    JOptionPane.showMessageDialog(rootPane, "Check your connection and try again.\n"+e.getMessage());
                    likepanel.setVisible(true);
                    nichedekhopanel.setVisible(false);
                    hitlike.setText("Hit Like and support us to grow up asapp Community.");
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
        // Loading portion end : which is set to display load        
  
    }//GEN-LAST:event_likebuttonMouseClicked
// HOME BUTTON
    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        LastSeen_Task.cancel();
        LastSeentimer.cancel();
        new chatpage(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);dispose();
    }//GEN-LAST:event_homeMouseClicked
// feedback jTA
    private void feedbackareaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feedbackareaFocusGained

        l1.setVisible(false);                                                                   // l1 is a 'write to us...' jLabel
    }//GEN-LAST:event_feedbackareaFocusGained

    private void feedbackareaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_feedbackareaFocusLost
if(feedbackarea.getText().isEmpty())
        l1.setVisible(true);       
    }//GEN-LAST:event_feedbackareaFocusLost

    private void Label_malelogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_malelogoMouseClicked
        new MyProfile(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);

        dispose();        
    }//GEN-LAST:event_Label_malelogoMouseClicked

    private void Label_femalelogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_femalelogoMouseClicked
        new MyProfile(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);

        dispose();     
    }//GEN-LAST:event_Label_femalelogoMouseClicked

    private void website_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_website_button1ActionPerformed
Class_Website.GoToAsappOfficial();       
    }//GEN-LAST:event_website_button1ActionPerformed

    private void website_button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_website_button2ActionPerformed
Class_Website.GoToAsappOfficial();       
    }//GEN-LAST:event_website_button2ActionPerformed

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
                new feedback().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Label_feedResponce;
    private javax.swing.JLabel Label_femalelogo;
    private javax.swing.JLabel Label_imageprint;
    private javax.swing.JLabel Label_malelogo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextArea feedbackarea;
    private javax.swing.JLabel hitlike;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel l1;
    private javax.swing.JLabel likebutton;
    private javax.swing.JPanel likepanel;
    private javax.swing.JPanel nichedekhopanel;
    private javax.swing.JButton send_JButton;
    private javax.swing.JLabel username;
    private javax.swing.JButton website_button1;
    private javax.swing.JButton website_button2;
    // End of variables declaration//GEN-END:variables
}
