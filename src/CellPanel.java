import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class CellPanel extends JPanel
{
    private final Color blueColor = new Color(99, 190, 255);
    private final Color greenColor = new Color(125, 255, 147);
    private final Color redColor = new Color(255, 125, 125);

    public int x;
    public int y;
    public ChessPiece currentChessPiece;
    public boolean isValidMove;
    private JLabel imageLabel;
    private PieceColor originCellColor;
    public CellPanel(boolean isWhite, int x, int y)
    {
        this.x = x;
        this.y = y;
        isValidMove = false;
        originCellColor = isWhite ? PieceColor.WHITE : PieceColor.BLACK;
        this.setBackground(isWhite ? Color.WHITE : Color.BLACK);
        imageLabel = new JLabel();
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(imageLabel);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GameFrame.Instance.centerPanel.OnClickCellPanel(x, y);
            }
        });
    }

    public void AddImage(ChessPiece chessPiece)
    {
        currentChessPiece = chessPiece;
        BufferedImage pieceImage = GetBufferedImageFromFile(chessPiece);
        Image image = pieceImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setVisible(true);
    }
    public void RemovePiece()
    {
        currentChessPiece = null;
        imageLabel.setVisible(false);
    }
    public void OnSelect()
    {
        this.setBackground(blueColor);
    }

    public void DeSelect()
    {
        this.setBackground(this.originCellColor == PieceColor.WHITE ? Color.WHITE : Color.BLACK);
    }
    public void SetColor(boolean isMove)
    {
        isValidMove = true;
        if(isMove)
        {
            this.setBackground(greenColor);
        }
        else
        {
            this.setBackground(redColor);
        }
    }

    private BufferedImage GetBufferedImageFromFile(ChessPiece chessPiece)
    {
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        String fileStr = path + "/piece/";
        if(chessPiece.color == PieceColor.WHITE) fileStr += "w_";
        else fileStr += "b_";
        fileStr += chessPiece.type.toString().toLowerCase() + "_png_shadow_128px.png";
        File file = new File(fileStr);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            return bufferedImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
