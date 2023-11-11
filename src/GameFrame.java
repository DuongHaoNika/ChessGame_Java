import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    public static GameFrame Instance;
    public TopPanel topPanel;
    public CenterPanel centerPanel;
    public GameFrame()
    {
        Instance = this;
        this.setVisible(true);
        this.setSize(640, 680);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Chess");

        topPanel = new TopPanel();
        centerPanel = new CenterPanel();

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
