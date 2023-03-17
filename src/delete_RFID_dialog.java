import javax.swing.*;
import java.awt.event.*;

public class delete_RFID_dialog extends JDialog {
    private String url_main="https://studev.groept.be/api/a22ib2d02/";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel delete_label;
    private int cardNumber;
    public delete_RFID_dialog(int id) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        this.pack();

        cardNumber=id;
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
    }

    private void onOK() {
        // add your code here
        DB rc=new DB();
        String response;
        if(cardNumber!=0)
            response= rc.makeGETRequest(url_main+"removeCard/"+cardNumber );
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
