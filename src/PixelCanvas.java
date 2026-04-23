import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PixelCanvas extends JPanel {
    public Dimension canvasSize;
    public Dimension defaultRes = new Dimension(32,32);
    public Color[][] pixels = new Color[defaultRes.height][defaultRes.width];
    public int scaleMultiplier;
    public Backend backend;

    public int brushSize = 2;
    public float brushStrength = 0.5f;

    public void saveImage (String path){
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, pixels[y][x].getRGB());
            }
        }
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        );
        File file = new File(path + "/" + timestamp + ".png");
        try {
            ImageIO.write(img, "png", file);
            System.out.println("Saved image to "+file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
                int cx = e.getX() / scaleMultiplier;
                int cy = e.getY() / scaleMultiplier;

                for (int dy = -brushSize; dy <= brushSize; dy++) {
                    for (int dx = -brushSize; dx <= brushSize; dx++) {

                        int x = cx + dx;
                        int y = cy + dy;

                        if (x < 0 || y < 0 || x >= canvasSize.width || y >= canvasSize.height)
                            continue;

                        double dist = Math.sqrt(dx*dx + dy*dy);

                        if (dist <= brushSize) {
                            float falloff = 1.0f - (float)(dist / brushSize);
                            float current = pixels[y][x].getRed() / 255f;
                            float newValue = current + brushStrength * falloff;
                            newValue = Math.min(1.0f, newValue);

                            int gray = (int)(newValue * 255);
                            pixels[y][x] = new Color(gray, gray, gray);
                        }
                    }
                }

                repaint();
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
