import javax.swing.*;
import java.awt.event.*;

public class register_dialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel rename_label;
    private JTextField rename_textfield;

    public register_dialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        this.pack();
    }

    private void onOK() {
        String name= rename_textfield.getText();
        if(name.isEmpty())
            rename_label.setText("Please input a name!");
        else{
            DB  rc = new DB();
            String write_mode = rc.makeGETRequest(rc.url_main+"writeMode/" + name);
            wait_RFID_diaglog new1=new wait_RFID_diaglog(name);
            new1.setLocationRelativeTo(this);
            new1.setVisible(true);
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

}
