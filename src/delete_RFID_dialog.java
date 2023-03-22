import javax.swing.*;
import java.awt.event.*;

public class delete_RFID_dialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel delete_label;
    private JLabel to_delete_label;

    public delete_RFID_dialog(int id) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        this.pack();

        to_delete_label.setText("Do you want to delete "+ id+" ?" );
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DB rc=new DB();
                String response= rc.makeGETRequest(rc.url_main+"removeCard/"+id );
                dispose();
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

    private void onCancel() {
        dispose();
    }
}
