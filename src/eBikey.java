import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.border.Border;
import javax.swing.*;

public class eBikey extends JFrame{
    private JPanel main_panel;
    private JPanel alarm_panel;
    private JLabel alarm_label;
    private JLabel lock_label;
    private JLabel RFID_label;
    private JPanel RFID_panel;
    private JLabel scan_label;
    private JPanel scan_panel;
    private JButton lock_button;
    private JPanel lock_panel;
    private JButton scan_erase_button;
    private JList scan_list;
    private JButton RFID_delete_button;
    private JButton RFID_register_button;
    private JButton alarm_button;
    private JButton alarm_erase_button;
    private JList RFID_list;
    private JButton alarm_history_button;
    private JScrollPane RFID_list_scrollpane;
    private JScrollPane scan_scroll;
    private JButton scan_history_button;

    private DB rc;
    private boolean is_locked,is_alarming;
    private int cardNumber;

    public eBikey(String title) {
        //set UI for the main page
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 950);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        this.setContentPane(main_panel);

        //set variables
        cardNumber=0;
        //set DB
        rc=new DB();

        //set UI for alarm and lock area
        alarm_button.setContentAreaFilled(false);

        alarm_button.addActionListener(e -> {
            if(is_alarming){
                rc.makeGETRequest(rc.url_main+ "setAlarmManual/"+"0");
                rc.makeGETRequest(rc.url_main+ "setAlarming/"+"0");
            }
            else {
                //record the time that the alarm is triggered by computer
                rc.makeGETRequest(rc.url_main+ "setAlarmManual/"+"1");
                rc.makeGETRequest(rc.url_main+ "setAlarming/"+"1");
                rc.makeGETRequest(rc.url_main+ "recordAlarm/"+new Date().getTime()/1000);
            }
        });
        alarm_history_button.addActionListener(e -> {
            alarm_history_dialog new_dialog= new alarm_history_dialog();
            new_dialog.setLocationRelativeTo(alarm_panel);
            new_dialog.setVisible(true);
        });
        lock_button.setContentAreaFilled(false);
        lock_button.addActionListener(e -> {
            if(is_locked){

               // rc.makeGETRequest(rc.url_main+ "scanHistory/"+"404/"+new Date().getTime()/1000+"/1");
             //    rc.makeGETRequest(rc.url_main+ "unlock");
                rc.makeGETRequest(rc.url_main+ "setManual");
              //  System.out.println("I am unlocking");

            }
            else {

             //   rc.makeGETRequest(rc.url_main+ "scanHistory/"+"404/"+new Date().getTime()/1000+"/2");
              //  rc.makeGETRequest(rc.url_main+ "lock");
                rc.makeGETRequest(rc.url_main+ "setManual");
              //  System.out.println("I'm locking");
            }
        });

        //set UI for RFID area
        RFID_register_button.addActionListener(e -> {
            register_dialog new_dialog= new register_dialog();
            new_dialog.setLocationRelativeTo(RFID_panel);
            new_dialog.setVisible(true);
        });
        RFID_delete_button.addActionListener(e -> {
            System.out.println("cardNumber:"+cardNumber);
            //Only users can be deleted
            if(cardNumber!=404&&cardNumber!=0){
                delete_RFID_dialog new1= new delete_RFID_dialog(cardNumber);
                new1.setLocationRelativeTo(RFID_panel);
                new1.setVisible(true);
                set_RFID_list();
            }
        });
        RFID_list.setBackground(new Color(0,0,0,0));
        RFID_list_scrollpane.getViewport().setOpaque(false);
        RFID_list_scrollpane.setBorder(null);
        RFID_list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting()){
                    //get the selected card number
                    JList source = (JList)event.getSource();
                    Object[] selectedItem = source.getSelectedValues();
                    int j=source.getSelectedIndex();
                    if(j!=-1){
                        String s= selectedItem[0].toString();
                        System.out.println("tostring:"+s);
                        if(s.equals("ALL")){//When selecting all the cards, curdNumber will be set to 0
                            cardNumber=0;
                        }
                        else{
                            s=s.substring(0,s.indexOf("N"));
                            int i= s.indexOf(":");
                            String result=s.substring(i+1);
                            result=result.trim();
                            cardNumber= Integer.parseInt(result);
                        }
                        set_scan();
                    }
                }
            }
        });


        //set UI for scan area
        scan_list.setBackground(new Color(0,0,0,0));
        //scan_scroll.setC
        scan_scroll.getViewport().setOpaque(false);
        scan_scroll.setBorder(null);
       // scan_scroll.set
        scan_erase_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scan_erase_dialog new_dialog= new scan_erase_dialog();
                new_dialog.setLocationRelativeTo(scan_panel);
                new_dialog.setVisible(true);
            }
        });

        //Refresh content periodically
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                set_RFID_list();
                set_scan();
                set_lock_state();
            }
        },0,500);

        //if the alarm is triggered, the background of the alarm zone will blink, attract users attention.
        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(is_alarming){
                    if(alarm_panel.getBackground().equals( new Color(0,200,15))){
                        alarm_panel.setBackground(new Color(0,240,15));
                    }
                    else{
                        alarm_panel.setBackground(new Color(0,200,15));
                    }
                }
                else
                    alarm_panel.setBackground(new Color(0,185,15));
            }
        },0,400);

        alarm_history_button.setOpaque(false);
        RFID_register_button.setOpaque(false);
        RFID_delete_button.setOpaque(false);
        scan_erase_button.setOpaque(false);

        alarm_history_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                alarm_history_button.setOpaque(true);
                alarm_history_button.setForeground(Color.green);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                alarm_history_button.setOpaque(false);
                alarm_history_button.setForeground(Color.white);
            }
        });
        RFID_register_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                RFID_register_button.setOpaque(true);
                RFID_register_button.setForeground(Color.orange);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                RFID_register_button.setOpaque(false);
                RFID_register_button.setForeground(Color.white);
            }
        });
        RFID_delete_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                RFID_delete_button.setOpaque(true);
                RFID_delete_button.setForeground(Color.orange);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                RFID_delete_button.setOpaque(false);
                RFID_delete_button.setForeground(Color.white);
            }
        });
        scan_erase_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                scan_erase_button.setOpaque(true);
                scan_erase_button.setForeground(new Color(150,70,215));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                scan_erase_button.setOpaque(false);
                scan_erase_button.setForeground(Color.white);
            }
        });
    }

    private void set_lock_state(){
        is_locked= rc.get_lock_state()[0] == 1;
        is_alarming= rc.get_lock_state()[1] == 1;
        if(is_alarming){
            alarm_button.setIcon(new ImageIcon("src/icon/speaker.png"));
        }
        else {
            alarm_button.setIcon(new ImageIcon("src/icon/mute.png"));
        }
        if(is_locked){
            lock_button.setIcon(new ImageIcon("src/icon/lock.png"));
        }
        else {
            lock_button.setIcon(new ImageIcon("src/icon/unlock.png"));
        }
    }
    private void set_scan(){
            ArrayList<String> scan_history_list=rc.get_scan_history(cardNumber);
            scan_list.setListData(scan_history_list.toArray());
    }
    private void set_RFID_list(){
        ArrayList<String> rfid_info_list=rc.get_RFID_info();
        rfid_info_list.add(0,"ALL");
        RFID_list.setListData(rfid_info_list.toArray());
    }
    public static void main(String[] args) {
        //generate my UI
        JFrame eBikey = new eBikey("eBikey");
        //the frame needs to become visible
        eBikey.setVisible(true);
    }
    }
