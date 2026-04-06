import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PixelCanvas extends JPanel {
    public Dimension canvasSize;
    public Dimension defaultRes = new Dimension(32,32);
    public Color[][] pixels = new Color[defaultRes.height][defaultRes.width];
    public int scaleMultiplier;
    public Backend backend;

    public void init(){
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = new Color(0,0,0);
            }
        }
    }

    private void mouseListen(){

        MouseAdapter drawListener = new MouseAdapter() {

            private void draw(MouseEvent e){
                int x = e.getX() / scaleMultiplier;
                int y = e.getY() / scaleMultiplier;

                if(x >= 0 && x < canvasSize.width && y >= 0 && y < canvasSize.height){
                    pixels[y][x] = Color.WHITE;
                    repaint();
                }

                backend.update(PixelCanvas.this);
            }

            @Override
            public void mousePressed(MouseEvent e){
                draw(e);
            }

            @Override
            public void mouseDragged(MouseEvent e){
                draw(e);
            }
        };

        addMouseListener(drawListener);
        addMouseMotionListener(drawListener);
    }

    public PixelCanvas(int width, int height){
        canvasSize = new Dimension(width,height);
        mouseListen();
        init();
    }
    public PixelCanvas(Dimension size){
        this.canvasSize = size;
        mouseListen();
        init();
    }
    public PixelCanvas(){
        this.canvasSize = defaultRes;
        mouseListen();
        init();
    }

    public float[] getPixels(){
        float[] pixelsFloat = new float[canvasSize.width * canvasSize.height];

        for (int y = 0; y < canvasSize.height; y++) {
            for (int x = 0; x < canvasSize.width; x++) {
                pixelsFloat[(y*canvasSize.width)+x] = pixels[y][x].getRed() / 255f;
            }
        }

        return pixelsFloat;
    }

    @Override
    protected void paintComponent(Graphics g){
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                g.setColor(pixels[y][x]);

                int startPosX = x*scaleMultiplier;
                int startPosY = y*scaleMultiplier;
                // i dont know what im doiiing
                g.fillRect(startPosX,startPosY,scaleMultiplier,scaleMultiplier);
            }
        }
    }
}
