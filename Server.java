package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//import java.io.ObjectOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server extends JFrame implements ActionListener{

    JPanel panel1;
//    JPanel panel2;
    JTextField field1;
    JButton button1;
    static JPanel textArea1;

    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;

    Boolean typing;
    static Box vertical = Box.createVerticalBox();

    Server(){

        //back arrow option
        ImageIcon image1 = new ImageIcon(ClassLoader.getSystemResource("com/company/Icons/3.png"));
        Image image2 = image1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon image3 = new ImageIcon(image2);
        JLabel label1 = new JLabel(image3);
        label1.setBounds(5, 13, 25, 25);
        add(label1);

        label1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        //profile icon
        ImageIcon image4 = new ImageIcon(ClassLoader.getSystemResource("com/company/Icons/1.png"));
        Image image5 = image4.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        ImageIcon image6 = new ImageIcon(image5);
        JLabel label2 = new JLabel(image6);
        label2.setBounds(30, 1, 50, 50);
        add(label2);

        //name of the user
        JLabel label3 = new JLabel("Damon");
        label3.setBounds(85, 0, 200, 40);
        label3.setForeground((Color.WHITE));
        label3.setFont(new Font("SAN_SARIF", Font.BOLD, 15));
        add(label3);

        //Active status of the user
        JLabel label4 = new JLabel("online");
        label4.setBounds(85, 16, 200, 40);
        label4.setForeground((Color.WHITE));
        label4.setFont(new Font("SAN_SARIF", Font.PLAIN, 12));
        add(label4);

        Timer timer = new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!typing) label4.setText("online");
           }
        });
        timer.getInitialDelay();

        //more options menu
        ImageIcon image7 = new ImageIcon(ClassLoader.getSystemResource("com/company/Icons/3icon.png"));
        Image image8 = image7.getImage().getScaledInstance(10, 20, Image.SCALE_DEFAULT);
        ImageIcon image9 = new ImageIcon(image8);
        JLabel label5 = new JLabel(image9);
        label5.setBounds(350, 13, 25, 25);
        add(label5);

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //System.exit(0);
                JFrame f = new JFrame();
                //JOptionPane.showMessageDialog(f,"Successfully Updated.","Alert",JOptionPane.WARNING_MESSAGE);
                int a=JOptionPane.showConfirmDialog(f,"Clear chat history?");
                if(a==JOptionPane.YES_OPTION){
                    //textArea1.setText(null);
                }
            }
        });

        //phone logo
        ImageIcon image10 = new ImageIcon(ClassLoader.getSystemResource("com/company/Icons/phone.png"));
        Image image11 = image10.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon image12 = new ImageIcon(image11);
        JLabel label6 = new JLabel(image12);
        label6.setBounds(310, 13, 25, 25);
        add(label6);

        //video icon
        ImageIcon image13 = new ImageIcon(ClassLoader.getSystemResource("com/company/Icons/video.png"));
        Image image14 = image13.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon image15 = new ImageIcon(image14);
        JLabel label7 = new JLabel(image15);
        label7.setBounds(260, 13, 25, 25);
        add(label7);

        field1 = new JTextField("Type a message");
        field1.setSelectionColor(Color.GRAY);
        field1.setBounds(5, 560, 290, 35);
        field1.setFont(new Font("SAN_SARIF", Font.PLAIN, 17));
        add(field1);

        field1.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                label4.setText("typing...");
                timer.stop();
                typing = true;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                typing = false;
                if (!timer.isRunning()){
                    timer.start();
                }
            }
        });

        field1.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                field1.setText("");
            }

            public void focusLost(FocusEvent e) {
                // nothing
            }
        });

        button1 = new JButton("Send");
        button1.setBounds(300, 560, 90, 35);
        button1.setFont(new Font("SAN_SARIF", Font.BOLD, 15));
        button1.setBackground(new Color(131, 58, 180));
        button1.setForeground((Color.WHITE));
        button1.addActionListener(this);
        add(button1);

        textArea1 = new JPanel();
        textArea1.setBounds(7, 85, 390, 450);
        textArea1.setBackground(Color.WHITE);
        textArea1.setFont(new Font("SAN_SARIF", Font.BOLD, 15));
//        textArea1.setLineWrap(true);
//        textArea1.setWrapStyleWord(true);
//        textArea1.setEditable(false);
        add(textArea1);

        JScrollPane scrollPane = new JScrollPane(textArea1);
        scrollPane.setBounds(7, 85, 385, 450);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(new Color(131, 58, 180));
        panel1.setBounds(0, 0, 400, 55);
        add(panel1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");

        JLabel clock = new JLabel();
        clock.setBounds(170, 55, 200, 20);
        clock.setText(dateFormat.format(Calendar.getInstance().getTime()));
        clock.setForeground(Color.darkGray);
        //new Timer(1000, this).start();
        add(clock);

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setSize(400,600);
        setUndecorated(true);
        setLocation(370, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){

        try {
            String out = "";
            out = field1.getText().trim();

            JPanel panel2 = formatLabel(out);
            panel2.setBackground(Color.WHITE);

            textArea1.setLayout(new BorderLayout());
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBackground(Color.WHITE);
            rightPanel.add(panel2, BorderLayout.LINE_END);
            vertical.add(rightPanel);

            textArea1.add(vertical, BorderLayout.PAGE_START);
            //textArea1.add(panel2);
//            textArea1.setText(textArea1.getText() + "\n Me : " + out);
            dataOutputStream.writeUTF(out);

        }catch(Exception e){
            //System.out.println(e);
        }
        field1.setText("");
    }
    public static JPanel formatLabel(String out){
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

        JLabel newLabel = new JLabel(out);
        newLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        newLabel.setBackground(new Color(252, 183, 241));
        newLabel.setOpaque(true);
        newLabel.setBorder(new EmptyBorder(15, 15, 15, 100));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        JLabel newLabel2 = new JLabel();
        newLabel2.setText(simpleDateFormat.format(calendar.getTime()));

        panel3.add(newLabel);
        panel3.add(newLabel2);
        return panel3;
    }

    public static void main(String[] args) {
        new Server().setVisible(true);

        String msgInput = "";

        try {
            serverSocket = new ServerSocket(6001);

            while (true) {
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                //while (!msgInput.equals("exit")) {
                while (true){
                    msgInput = dataInputStream.readUTF();
                    JPanel panel2 = formatLabel(msgInput);
                    panel2.setBackground(Color.WHITE);

                    JPanel left = new JPanel(new BorderLayout());
                    left.setBackground(Color.WHITE);
                    left.add(panel2, BorderLayout.LINE_START);
                    vertical.add(left);
//                    textArea1.setText(textArea1.getText().trim() + "\n Stefan : " + msgInput);
                }
            }

        } catch (Exception e) {
            //System.out.println(e);
        }
    }
}
