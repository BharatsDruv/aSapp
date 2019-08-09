/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template asappapplication@gmail.com
 * shrivastava file, choose Tools | Templates
 * and open the template in the editor.
 */

//


import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author sony
 */
public class MyProfile extends javax.swing.JFrame {
String UEMAIL,UPASS,GENDER,UFNAME;
Timer LastSeentimer ;Statement stmt=null;
TimerTask LastSeen_Task;
final JDialog d=new JDialog();            //Global Variables
    /**
     * Creates new form LogIn
     */
     public MyProfile()
     {
         initComponents();
        
     }
    public MyProfile(String UEMAIL1,String UPASS1,String GENDER1,String UFNAME1)      // Takes variable from other jFrames
{  
    initComponents();
        
    Print_loading();
        
    UEMAIL=UEMAIL1;
    UPASS=UPASS1;
    GENDER=GENDER1;
    UFNAME=UFNAME1;
    UserFirstName_Label.setText(UFNAME);
    UserEmail_Label.setText(UEMAIL);
    home.setFocusable(true);
    Label_imageprint.setVisible(false);
    malelogo.setVisible(false);
    fnamelable.setVisible(false);
    fnametf.setVisible(false);
    femalelogo.setVisible(false);
    
    Thread t = new Thread()
    { 
        public  void run()
        {
        // TASK BEGIN 

            e1.setVisible(false); e2.setVisible(false);                         // e1 and e2 are '*' to ensure the errors in pass field
            try{
            
            //BACK END STARTs   
                Class.forName(Class_Setting.DRIVER_MANAGER);
                Connection con=DriverManager.getConnection(Class_Setting.URL+Class_Setting.DATABASE_NAME,Class_Setting.USERNAME,Class_Setting.PASSWORD);
                stmt=con.createStatement();
                        
                ResultSet rs=stmt.executeQuery("Select dob from profile_"+ChangeToValidTableName(UEMAIL)+";");
                rs.last();
                UserBirthDate_Label.setText(rs.getString(1));
                rs = stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(UEMAIL)+";");          // to take image from user's profile table 

              // IMAGE SECTION BEGIN
                String filename = "";                                           // filename is a PROFILE PIC name
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
                Label_imageprint.setIcon(imageIcon);                            // imageprint is a jLable

            // Back end off        
                }
            catch(Exception e)
                { 
                    Label_imageprint.setVisible(false);                                                                            
               
                    if(GENDER.equals("M"))                                      // if user is a male
                    {   
                        malelogo.setVisible(true);
                        System.out.print("here1");                              // malelogo is a jLabel LOGO for Male
                    }
                    else 
                    { 
                        femalelogo.setVisible(true);
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
        
        
    // TIMER FOR UPDATING MY LAST SEEN
        
     LastSeentimer = new Timer();
     LastSeen_Task = new TimerTask()
    {
        public void run()
        {
            
            
            
            try{
                        
                        // In user's TABLE
                    String query="Update profile_"+ChangeToValidTableName(UEMAIL)+" set lastseen=now();";
                    stmt.executeUpdate(query);
                    System.out.println("chal ra he setting wala");
                
                }catch(Exception e)
                {JOptionPane.showConfirmDialog(null,"timer error"+e.getMessage());}
        }
        
        
        
        
    };
        LastSeentimer.scheduleAtFixedRate(LastSeen_Task, 0, 3000);

 
} // CONSTRUCTOR CLOSED
    
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
        home = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        feedback_button = new javax.swing.JButton();
        fnamelable = new javax.swing.JLabel();
        newpasslable = new javax.swing.JLabel();
        labelchange = new javax.swing.JLabel();
        e1 = new javax.swing.JLabel();
        fnametf = new javax.swing.JTextField();
        e2 = new javax.swing.JLabel();
        newpasspf = new javax.swing.JPasswordField();
        prevpasslable = new javax.swing.JLabel();
        prevpasspf = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        browse = new javax.swing.JButton();
        femalelogo = new javax.swing.JLabel();
        malelogo = new javax.swing.JLabel();
        preview = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        Label_passerror = new javax.swing.JLabel();
        UserBirthDate_Label = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        UserFirstName_Label = new javax.swing.JLabel();
        UserEmail_Label = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        change_Label = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("My Profile");
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
        Label_imageprint.setBounds(90, 170, 120, 120);

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

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hi.gif"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(jLabel9);
        jLabel9.setBounds(840, 130, 140, 90);

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

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setText("     My profile.");

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(291, Short.MAX_VALUE))
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        jLabel7.setText("B H A R A T           S H R I V A S T A V A ' S             P R O D U C T I O N             A S A P P         © 2 0 1 6");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(310, 570, 421, 10);

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
        feedback_button.setBounds(850, 240, 140, 23);

        fnamelable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        fnamelable.setForeground(new java.awt.Color(102, 102, 102));
        fnamelable.setText("New First name");
        getContentPane().add(fnamelable);
        fnamelable.setBounds(330, 350, 120, 30);

        newpasslable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        newpasslable.setForeground(new java.awt.Color(102, 102, 102));
        newpasslable.setText("New password");
        getContentPane().add(newpasslable);
        newpasslable.setBounds(330, 420, 150, 30);

        labelchange.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        labelchange.setForeground(new java.awt.Color(102, 102, 102));
        labelchange.setText("want to change password ?");
        getContentPane().add(labelchange);
        labelchange.setBounds(320, 390, 190, 30);

        e1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        e1.setForeground(new java.awt.Color(255, 51, 51));
        e1.setText("*");
        getContentPane().add(e1);
        e1.setBounds(310, 490, 10, 15);

        fnametf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fnametfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fnametfFocusLost(evt);
            }
        });
        fnametf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fnametfKeyTyped(evt);
            }
        });
        getContentPane().add(fnametf);
        fnametf.setBounds(320, 350, 350, 30);

        e2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        e2.setForeground(new java.awt.Color(255, 51, 51));
        e2.setText("*");
        getContentPane().add(e2);
        e2.setBounds(310, 430, 10, 15);

        newpasspf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                newpasspfCaretUpdate(evt);
            }
        });
        newpasspf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                newpasspfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                newpasspfFocusLost(evt);
            }
        });
        getContentPane().add(newpasspf);
        newpasspf.setBounds(320, 420, 350, 30);

        prevpasslable.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        prevpasslable.setForeground(new java.awt.Color(102, 102, 102));
        prevpasslable.setText("current password");
        getContentPane().add(prevpasslable);
        prevpasslable.setBounds(330, 480, 150, 30);

        prevpasspf.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                prevpasspfCaretUpdate(evt);
            }
        });
        prevpasspf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prevpasspfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                prevpasspfFocusLost(evt);
            }
        });
        getContentPane().add(prevpasspf);
        prevpasspf.setBounds(320, 480, 350, 30);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(260, 460, 470, 10);

        browse.setText("Browse...");
        browse.setToolTipText("Select image for your profile picture.");
        browse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });
        getContentPane().add(browse);
        browse.setBounds(80, 300, 140, 20);

        femalelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/femalelogo.png"))); // NOI18N
        getContentPane().add(femalelogo);
        femalelogo.setBounds(100, 170, 110, 130);

        malelogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/malelogo.png"))); // NOI18N
        getContentPane().add(malelogo);
        malelogo.setBounds(100, 170, 110, 130);

        preview.setForeground(new java.awt.Color(102, 102, 102));
        preview.setText("Preview ");
        getContentPane().add(preview);
        preview.setBounds(100, 130, 60, 14);

        save.setBackground(new java.awt.Color(0, 102, 255));
        save.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        save.setForeground(new java.awt.Color(255, 255, 255));
        save.setText("Save profile");
        save.setToolTipText("Save changes");
        save.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        getContentPane().add(save);
        save.setBounds(680, 480, 130, 30);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(260, 390, 470, 10);

        Label_passerror.setForeground(new java.awt.Color(255, 0, 51));
        getContentPane().add(Label_passerror);
        Label_passerror.setBounds(680, 420, 240, 30);

        UserBirthDate_Label.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        UserBirthDate_Label.setForeground(new java.awt.Color(51, 51, 51));
        UserBirthDate_Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(UserBirthDate_Label);
        UserBirthDate_Label.setBounds(390, 270, 250, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText("Birth Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(280, 270, 120, 30);

        UserFirstName_Label.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        UserFirstName_Label.setForeground(new java.awt.Color(51, 51, 51));
        UserFirstName_Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(UserFirstName_Label);
        UserFirstName_Label.setBounds(380, 160, 310, 40);

        UserEmail_Label.setFont(new java.awt.Font("Trebuchet MS", 0, 24)); // NOI18N
        UserEmail_Label.setForeground(new java.awt.Color(102, 102, 102));
        UserEmail_Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(UserEmail_Label);
        UserEmail_Label.setBounds(380, 220, 310, 30);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(820, 140, 10, 380);

        change_Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.jpg"))); // NOI18N
        change_Label.setToolTipText("Edit My Name");
        change_Label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        change_Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                change_LabelMouseClicked(evt);
            }
        });
        getContentPane().add(change_Label);
        change_Label.setBounds(690, 160, 40, 40);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setText("Email :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(280, 220, 70, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Name :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(280, 160, 70, 30);
        getContentPane().add(jSeparator6);
        jSeparator6.setBounds(240, 130, 570, 30);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator5);
        jSeparator5.setBounds(232, 140, 10, 380);

        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Max. size ( 50 KB )");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(80, 320, 140, 14);

        jLabel11.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("410");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(120, 420, 50, 26);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/silver-light-blue-wave-abstract-backgrounds-powerpoint-1024x725.jpg"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(0, 120, 1010, 510);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void feedback_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feedback_buttonActionPerformed
LastSeentimer.cancel();
LastSeen_Task.cancel();
        new feedback(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);dispose();              
    }//GEN-LAST:event_feedback_buttonActionPerformed
// New First Name TF
    private void fnametfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fnametfFocusGained
fnamelable.setVisible(false);        
    }//GEN-LAST:event_fnametfFocusGained
// New First Name TF
    private void fnametfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fnametfFocusLost
fnametf.setText(fnametf.getText().trim());                                      // To remove extra space from New First Name
if(fnametf.getText().isEmpty())
    fnamelable.setVisible(true);                                                
    }//GEN-LAST:event_fnametfFocusLost
// New Pass PF
    private void newpasspfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newpasspfFocusGained
      newpasslable.setVisible(false);        
    }//GEN-LAST:event_newpasspfFocusGained

    private void newpasspfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newpasspfFocusLost
    newpasspf.setText(newpasspf.getText().trim());                              // To remove extra space from New Pass  
    prevpasslable.setText("Previous password");
if(newpasspf.getText().isEmpty())
    newpasslable.setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_newpasspfFocusLost
// First Name TF
    private void fnametfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fnametfKeyTyped
                                                                                // To ensure that First Name is correct and Legal
char k=evt.getKeyChar();
if(k>='0' && k<='9')
    evt.consume();

    }//GEN-LAST:event_fnametfKeyTyped
// Prev Pass PF
    private void prevpasspfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prevpasspfFocusGained
prevpasslable.setVisible(false);       
    }//GEN-LAST:event_prevpasspfFocusGained

    private void prevpasspfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prevpasspfFocusLost
prevpasspf.setText(prevpasspf.getText().trim());                                // To remove extra space from prev pass
if(prevpasspf.getText().equals(UPASS));
else 
    e1.setVisible(true);
if(prevpasspf.getText().isEmpty())
{ prevpasslable.setVisible(true);
if(newpasspf.getText().isEmpty())
    save.setEnabled(true);
}
    }//GEN-LAST:event_prevpasspfFocusLost
// SAVE PROFILE BUTTON (save user's profile)
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
Thread t = new Thread()
{
    public  void run()
    {
    // TASK BEGIN 

        save.setText("Please wait");       
          
        try{
                String DNAME=ChangeToValidTableName(UEMAIL);                 // Conversion of UEMAIL to valid Table name
            // BACK END STARTs
                String NFNAME=fnametf.getText();                                // NFNAME = New First Name of user
                String PREVPASS=prevpasspf.getText();
                String NEWPASS=newpasspf.getText();

                if(NEWPASS.length()<8  )                                        // if current pass != new pass
                    {
                            Label_passerror.setText("Password must contains at least 8 character.");
                            e2.setVisible(true);                                // e2 is '*' error representator
                    }
                else            // if user wants to change password
                    {
                  
                            String query="Update profile_"+DNAME+" set pass='"+NEWPASS+"';";         // change new password
                            if((stmt.executeUpdate(query))==0);
                                JOptionPane.showMessageDialog(null,"password change");
                            newpasspf.setText("");
                            prevpasspf.setText("");
                            UPASS=NEWPASS;
                            Label_passerror.setText("");
                    }
                
                if(!NFNAME.isEmpty())                                           // if user's want to change its NAME
                    {
                            String query="Update profile_"+DNAME+" set name='"+NFNAME+"';";
                            if(JOptionPane.showInputDialog(null,"Enter Your Password.").equals(UPASS)) 
                            stmt.executeUpdate(query);                          // Confirmation prev. Password
                            UFNAME=NFNAME; 
                            fnamelable.setVisible(false);
                            change_Label.setVisible(true);
                            fnametf.setVisible(false);
                            UserFirstName_Label.setText(UFNAME);                // UFNAME (Global variable) update its value
                    }
            // Back end off                     
                        
                        
            }
        catch(Exception e)
            {                                                                   // if internet connect is not there.
                JOptionPane.showMessageDialog(null,"Check your details.");
            }
        save.setText("Save profile");
        
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
    
    }//GEN-LAST:event_saveActionPerformed
// BROWSE ... BUTTON (to take PRO PIC from System)
    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
Thread t = new Thread()
{ 
    public  void run()
    {
    // TASK BEGIN 

        try{
        // BACK END STARTs        
                Class.forName(Class_Setting.DRIVER_MANAGER);
                Connection connection=DriverManager.getConnection(Class_Setting.URL+Class_Setting.DATABASE_NAME,Class_Setting.USERNAME,Class_Setting.PASSWORD);
                stmt=connection.createStatement();
                        
            // show Dialog box to enter user's PROFILE PIC from system
                JFileChooser chooser=new JFileChooser();        
                chooser.showOpenDialog(null);                                              
                File f=chooser.getSelectedFile();

                String filename1 = f.getAbsolutePath();                         // Path of Pic
    
                File image = new File(filename1);     
                PreparedStatement psmnt = connection.prepareStatement("update profile_"+ChangeToValidTableName(UEMAIL)+" set image=?"); 
                FileInputStream fis = new FileInputStream(image);    
                psmnt.setBinaryStream(1, (InputStream) fis, (int) (image.length()));  
                psmnt.executeUpdate();
    
    
                Statement stmt=connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT image FROM profile_"+ChangeToValidTableName(UEMAIL)+";");                    // Extract pic from profile table of user      
                String filename = "";             
                rs.last(); 
                {                                                               // conversion of image from bytes to JPEG format
                      
                    Blob test = rs.getBlob("image");         
                    InputStream x = test.getBinaryStream();                 
                    int size = x.available();                 

                    byte b[] = new byte[size];                 
                    x.read(b);                 

                } 
    
    
                byte imag[] = rs.getBytes("image");
                Image img = Toolkit.getDefaultToolkit().createImage(imag);
                                     // To RESIZE PRO PIC
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        
                Label_imageprint.setVisible(true);                              // imageprint is a jLable
                Label_imageprint.setIcon(imageIcon);  
                femalelogo.setVisible(false);
                malelogo.setVisible(false);
        

        // Back end off    
            }
        catch(Exception e)
            {System.out.print(e.getMessage());}
        
  
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
  

    }//GEN-LAST:event_browseActionPerformed
// HOME BUTTON 
    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
LastSeentimer.cancel();
LastSeen_Task.cancel();
        new chatpage(UEMAIL,UPASS,GENDER,UFNAME).setVisible(true);dispose();    // chatpage opens
    }//GEN-LAST:event_homeMouseClicked

    private void newpasspfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_newpasspfCaretUpdate
if(newpasspf.getText().length()>0)
{
    save.setEnabled(false);
}
else
    save.setEnabled(true);
    }//GEN-LAST:event_newpasspfCaretUpdate

    private void prevpasspfCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_prevpasspfCaretUpdate
if(prevpasspf.getText().equals(UPASS))
{
    save.setEnabled(true);
}
else
    save.setEnabled(false);
    }//GEN-LAST:event_prevpasspfCaretUpdate

    private void change_LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_change_LabelMouseClicked
fnamelable.setVisible(true);
    change_Label.setVisible(false);
    fnametf.setVisible(true);       
    }//GEN-LAST:event_change_LabelMouseClicked

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
                new MyProfile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Label_imageprint;
    private javax.swing.JLabel Label_passerror;
    private javax.swing.JLabel UserBirthDate_Label;
    private javax.swing.JLabel UserEmail_Label;
    private javax.swing.JLabel UserFirstName_Label;
    private javax.swing.JButton browse;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel change_Label;
    private javax.swing.JLabel e1;
    private javax.swing.JLabel e2;
    private javax.swing.JButton feedback_button;
    private javax.swing.JLabel femalelogo;
    private javax.swing.JLabel fnamelable;
    private javax.swing.JTextField fnametf;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel labelchange;
    private javax.swing.JLabel malelogo;
    private javax.swing.JLabel newpasslable;
    private javax.swing.JPasswordField newpasspf;
    private javax.swing.JLabel preview;
    private javax.swing.JLabel prevpasslable;
    private javax.swing.JPasswordField prevpasspf;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}
