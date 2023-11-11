import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel
{
    public TopPanel()
    {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(100, 50));
        button.setText("Hello");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Duong Quang Hao");
            }
        });
        this.add(button);
    }
}
