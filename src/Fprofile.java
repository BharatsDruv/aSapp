/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




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
public class Fprofile extends javax.swing.JFrame {
String UEMAIL,UPASS,UGENDER,UFNAME,FEMAIL,FNAME,FRIENDLASTSEEN="0";
long i=1;Statement stmt=null;
Timer LastSeentimer ;
TimerTask LastSeen_Task;
Timer timerUpdate;
TimerTask taskUpdate;
final JDialog d=new JDialog();                        //Global Variables
    /**
     * Creates new form LogIn
     */
     public Fprofile()
     {
         initComponents();
        
     }
    public Fprofile(String UEMAIL1,String UPASS1,String GENDER1,String UFNAME1,String FEMAIL1)      // Takes variable from other jFrames
    {  
        initComponents();
        
        loading();
        
        UEMAIL=UEMAIL1;
        UPASS=UPASS1;
        UGENDER=GENDER1;
        UFNAME=UFNAME1;
        System.out.print(UEMAIL1);
        FEMAIL=FEMAIL1;
        System.out.print(GENDER1);
        username1.setText(UFNAME1);
    Label_imageprint.setVisible(false);
    malelogo.setVisible(false);
    
    femalelogo.setVisible(false);
    ResultSet rs=null;
    Label_online.setVisible(false);
        
                                                      // e1 and e2 are '*' to ensure the errors in pass field
 Thread t = new Thread()
{ public  void run()
  {
       // TASK BEGIN 

       
//BACK END STARTs   
         try{           Class.forName(Class_Setting.DRIVER_MANAGER);
                        Connection con=DriverManager.getConnection(Class_Setting.URL+Class_Setting.DATABASE_NAME,Class_Setting.USERNAME,Class_Setting.PASSWORD);
                        stmt=con.createStatement();
                         // IN FRIEND'S Portion
                      ResultSet rs = stmt.executeQuery("SELECT * FROM profile_"+ChangeToValidTableName(FEMAIL)+";");          // to take friends name from friend's profile table 
                      rs.last();
                       FNAME=rs.getString("name");
                      String sname=rs.getString("sname");
                      
                     
                        friendnamelable.setText(FNAME+" "+sname);
                       frienddoblable.setText(rs.getString("dob"));
                       
                       
                       friendlastseenlable.setText(rs.getString("lastseen") );
                   /*    
                     */  
                         rs = stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(FEMAIL)+";");          // to take image from friends's profile table 
                         
            String filename = "";                                                     // filename is a PRO PIC name
        rs.last(); 
            {

            Blob test = rs.getBlob("image");         
            InputStream x = test.getBinaryStream();                 
            int size = x.available();                 

            byte b[] = new byte[size];                 
            x.read(b);                 

            } 
    
                                                                                        // conversion of image from bytes to JPEG format
        byte imag[] = rs.getBytes("image");
        Image img = Toolkit.getDefaultToolkit().createImage(imag);
                                      
                                                                      // To set Image SIZE before display
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        
        Label_imageprint.setVisible(true);
        Label_imageprint.setIcon(imageIcon);   
        
        
        
        
        
        
        
            // imageprint is a jLable
                      

            // Back end off        
        }catch(Exception e)
        { 
            System.out.println("image eorror "+e.getMessage());
            try{
            Label_imageprint.setVisible(false);                                                                            
               
            String query="Select * from profile_"+ChangeToValidTableName(FEMAIL)+";";
            ResultSet rs=stmt.executeQuery(query);
             rs.last();
            String FGENDER=rs.getString("gender");
            if(FGENDER.equals("M"))                                                         // if user is a male
                    {   
                        malelogo.setVisible(true);
                        System.out.print("here1");// malelogo is a jLabel LOGO for Male
                    }
                    else 
                    { 
                        femalelogo.setVisible(true);
                    }
                    System.out.print("here");
            }
            catch(Exception e3)
            {System.out.print(e.getMessage());}
        }
        
        
        
        
        
       check();
       
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
        
                // Loading portion end : which is to display loading.
       update();
       UpdateMyLastSeen();
       
    } // CONSTRUCTOR END
    
    public void loading()
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
    
    void update()
{
        
         timerUpdate= new Timer();
         taskUpdate=new TimerTask()
     {
                public void run()
           {
                 
             
                    
                    try{
                        
                     
            // IN FRIEND'S Portion        
                  String query="select lastseen from profile_"+ChangeToValidTableName(FEMAIL)+";"; 
                    
                   
                    ResultSet rs=stmt.executeQuery(query);
                    rs.last();  
                                       
                                       friendlastseenlable.setText(rs.getString(1));
                                    System.out.println(rs.getString(1) +"    "+FRIENDLASTSEEN);
                                       if( !rs.getString(1).equals(FRIENDLASTSEEN) && !rs.getString(1).equals("0")  && 1<i++ )
                                       {
                                           l1.setVisible(false);                             // l1 is a label of 'was active at'
                                           Label_online.setVisible(true);
                                           friendlastseenlable.setVisible(false);
                                       }
                                       else
                                       {
                                             l1.setVisible(true);
                                           friendlastseenlable.setVisible(true);
                                           Label_online.setVisible(false);
                                       }
                                       FRIENDLASTSEEN=rs.getString(1);
                    
                   
                    friendlastseenlable.setText(rs.getString("lastseen"));
                   
                    }catch(Exception e)
                    {System.out.println("timer error"+e.getCause()+e.getLocalizedMessage());}
            }
                
                
                
                
       };
        timerUpdate.scheduleAtFixedRate(taskUpdate, 0, 10000);
        
}
    
    void check()
{
        try{
                        
                        // IN FRIEND'S Portion
                        String query="select liked from profile_"+ChangeToValidTableName(FEMAIL)+";";
                        ResultSet rs=stmt.executeQuery(query);
                        rs.next();
                        String liked=rs.getString("liked");
                        
                        // IN USER'S Portion
                         rs=stmt.executeQuery("Select liked from profile_"+ChangeToValidTableName(UEMAIL)+";");
                           rs.last();
                       if(liked.equals("liked"))
                       {
                           likedlable.setText(FNAME+" has liked asapp.");
                           
                           if(rs.getString(1).equals("liked"))
                           {
                                likedlable2.setText("and you too, invite others on asapp.");    
                           }
                           else
                                likedlable2.setText("BE a good friend by liking asapp.");    
                               
                           
                       }
                       else 
                       {        likedlable.setText(FNAME+" has not liked asapp yet.");
                           if(rs.getString(1).equals("liked"))
                           likedlable2.setText("asapp appreciate your LIKE. Send Feedback or Query anytime.");
                           else
                           likedlable2.setText("Be the first one in your friends by liking asapp.");
                       }
        }catch(Exception d)
        {
            JOptionPane.showMessageDialog(null,"Something went wrong.Please Try again.");
        }
        
}
    
    // METHOD UPDATE MY LAST SEEN
   void UpdateMyLastSeen()
{
    
     LastSeentimer = new Timer();
     LastSeen_Task = new TimerTask()
    {
        public void run()
        {
            
            
            
            try{
                
                // IN USER'S Portion
                
                String query="Update profile_"+ChangeToValidTableName(UEMAIL)+" set lastseen=now();";
                stmt.executeUpdate(query);
                System.out.println("chal ra he Fprofile wala");
                
            }catch(Exception e)
            {System.out.println(e.getMessage());}
        }
        
        
        
        
    };
        
        LastSeentimer.scheduleAtFixedRate(LastSeen_Task, 0, 7000);

 
       
   }
    
// Method to convert  user's email to a valid Table name for sql    
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
        Label_imageprint = new javax.swing.JLabel();
        Label_online = new javax.swing.JLabel();
        home = new javax.swing.JLabel();
        username1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        feedback_button = new javax.swing.JButton();
        friendnamelable = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        femalelogo = new javax.swing.JLabel();
        malelogo = new javax.swing.JLabel();
        preview = new javax.swing.JLabel();
        friendlastseenlable = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        frienddoblable = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        l1 = new javax.swing.JLabel();
        likedlable = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        likedlable2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Friend's Profile");
        setMinimumSize(new java.awt.Dimension(1010, 651));
        setResizable(false);
        getContentPane().setLayout(null);

        Label_imageprint.setToolTipText("My Profile");
        Label_imageprint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Label_imageprint.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Label_imageprint.setMaximumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setMinimumSize(new java.awt.Dimension(102, 102));
        Label_imageprint.setPreferredSize(new java.awt.Dimension(102, 102));
        getContentPane().add(Label_imageprint);
        Label_imageprint.setBounds(140, 240, 120, 120);

        Label_online.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        Label_online.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_online.setIcon(new javax.swing.ImageIcon(getClass().getResource("/online.gif"))); // NOI18N
        Label_online.setText("online ");
        getContentPane().add(Label_online);
        Label_online.setBounds(480, 220, 100, 20);

        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home2.png"))); // NOI18N
        home.setToolTipText("Home");
        home.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });
        getContentPane().add(home);
        home.setBounds(0, 120, 45, 45);

        username1.setFont(new java.awt.Font("Trebuchet MS", 1, 28)); // NOI18N
        username1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(username1);
        username1.setBounds(50, 130, 130, 30);
        getContentPane().add(jSeparator4);
        jSeparator4.setBounds(50, 160, 130, 10);

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));

        jLabel5.setFont(new java.awt.Font("Tekton Pro", 0, 75)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/front.gif"))); // NOI18N
        jLabel5.setToolTipText("asapp v1.0.0");

        jLabel16.setBackground(new java.awt.Color(204, 255, 255));
        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("C O N N E C T      W I T H      Y O U R      L O V E D      O N C E ");

        jLabel15.setFont(new java.awt.Font("SimHei", 1, 48)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("sapp");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Profile Viewer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(333, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1030, 120);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("B H A R A T              S H R I V A S T A V A ' S              P R O D U C T I O N              A P P         © 2 0 1 6");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(270, 570, 421, 10);

        feedback_button.setBackground(new java.awt.Color(0, 102, 255));
        feedback_button.setForeground(new java.awt.Color(255, 255, 255));
        feedback_button.setText("Feedback or Query");
        feedback_button.setToolTipText("asapp");
        feedback_button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        feedback_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feedback_buttonActionPerformed(evt);
            }
        });
        getContentPane().add(feedback_button);
        feedback_button.setBounds(760, 580, 140, 23);

        friendnamelable.setFont(new java.awt.Font("Trebuchet MS", 1, 48)); // NOI18N
        friendnamelable.setForeground(new java.awt.Color(0, 0, 102));
        friendnamelable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(friendnamelable);
        friendnamelable.setBounds(310, 120, 450, 60);

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("in your life.");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(20, 190, 280, 18);

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("ASAPP helps you connect with the people ");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(20, 170, 280, 18);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(260, 460, 470, 10);

        femalelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/femalelogo.png"))); // NOI18N
        getContentPane().add(femalelogo);
        femalelogo.setBounds(150, 240, 110, 130);

        malelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/malelogo.png"))); // NOI18N
        getContentPane().add(malelogo);
        malelogo.setBounds(150, 240, 110, 130);

        preview.setForeground(new java.awt.Color(102, 102, 102));
        preview.setText("Preview ");
        getContentPane().add(preview);
        preview.setBounds(150, 210, 60, 14);

        friendlastseenlable.setBackground(new java.awt.Color(102, 102, 102));
        friendlastseenlable.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        friendlastseenlable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(friendlastseenlable);
        friendlastseenlable.setBounds(460, 210, 290, 30);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(260, 390, 470, 10);

        frienddoblable.setBackground(new java.awt.Color(51, 102, 255));
        frienddoblable.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        getContentPane().add(frienddoblable);
        frienddoblable.setBounds(460, 260, 270, 40);

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Birthday :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(340, 260, 130, 29);

        l1.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        l1.setForeground(new java.awt.Color(51, 51, 51));
        l1.setText("was Active at : ");
        getContentPane().add(l1);
        l1.setBounds(350, 210, 140, 30);

        likedlable.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        likedlable.setForeground(new java.awt.Color(0, 51, 255));
        likedlable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(likedlable);
        likedlable.setBounds(270, 320, 470, 50);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/msgSplash.gif"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(770, 120, 60, 70);

        likedlable2.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        likedlable2.setForeground(new java.awt.Color(0, 51, 153));
        likedlable2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(likedlable2);
        likedlable2.setBounds(120, 400, 750, 40);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/silver-light-blue-wave-abstract-backgrounds-powerpoint-1024x725.jpg"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(0, 120, 1010, 510);

        username.setFont(new java.awt.Font("Trebuchet MS", 1, 28)); // NOI18N
        username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(username);
        username.setBounds(140, 30, 130, 40);
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(140, 70, 130, 10);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void feedback_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feedback_buttonActionPerformed
LastSeentimer.cancel();
LastSeen_Task.cancel();
timerUpdate.cancel();
taskUpdate.cancel();
        new feedback(UEMAIL,UPASS,UGENDER,UFNAME).setVisible(true);dispose();               // TODO add your handling code here:
    }//GEN-LAST:event_feedback_buttonActionPerformed

// New First Name TF// First Name TF
// HOME BUTTON 
    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
LastSeentimer.cancel();
LastSeen_Task.cancel();
timerUpdate.cancel();
taskUpdate.cancel();
        new chatpage(UEMAIL,UPASS,UGENDER,UFNAME).setVisible(true);dispose();        // chatpage opens
    }//GEN-LAST:event_homeMouseClicked

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
                new Fprofile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Label_imageprint;
    private javax.swing.JLabel Label_online;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton feedback_button;
    private javax.swing.JLabel femalelogo;
    private javax.swing.JLabel frienddoblable;
    private javax.swing.JLabel friendlastseenlable;
    private javax.swing.JLabel friendnamelable;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel l1;
    private javax.swing.JLabel likedlable;
    private javax.swing.JLabel likedlable2;
    private javax.swing.JLabel malelogo;
    private javax.swing.JLabel preview;
    private javax.swing.JLabel username;
    private javax.swing.JLabel username1;
    // End of variables declaration//GEN-END:variables
}
