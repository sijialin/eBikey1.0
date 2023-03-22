import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class alarm_history_dialog extends JDialog {
    private JPanel contentPane;

    private JButton alarm_history_erase_button;
    private JList alarm_history_list;
    private String url_main="https://studev.groept.be/api/a22ib2d02/";
    public alarm_history_dialog() {
        setContentPane(contentPane);
        setModal(true);
        this.setIconImage(new ImageIcon("src/icon/bicycle.png").getImage());
        this.setSize(400,240);

        set_list();
        alarm_history_erase_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onErase();
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


    private void onCancel(){
        dispose();
    }
    private void onErase() {
        // add your code here if necessary
        alarm_erase_dialog new1= new alarm_erase_dialog();
        new1.setLocationRelativeTo(this);
        new1.setVisible(true);
        set_list();
       // dispose();
    }
    private void set_list(){
        DB rc =new DB();
        String response = rc.makeGETRequest(url_main+"get_alarm_history" );
        ArrayList<String> alarm_info_list=rc.get_alarm_history(response);
        alarm_history_list.setListData(alarm_info_list.toArray());

    }
}
