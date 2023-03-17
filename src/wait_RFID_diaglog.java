import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class wait_RFID_diaglog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel wait_RFID_label;

    public wait_RFID_diaglog(String name) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);



        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        wait_RFID_label.setText("wait for RFID "+name);

        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int mode = 1;
                DB rc=new DB();
                String reponse= rc.makeGETRequest(  " https://studev.groept.be/api/a22ib2d02/getMode");
                mode= rc.get_mode(reponse);
                if (mode == 0){
                    dispose();
                }
            }
        },1000,200);


        this.pack();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

/*    public static void main(String[] args) {
        wait_RFID_diaglog dialog = new wait_RFID_diaglog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}
