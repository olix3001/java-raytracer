package me.olix3001.gui;

import me.olix3001.math.Vector3;
import me.olix3001.render.Camera;
import me.olix3001.render.Renderer;
import me.olix3001.render.Scene;
import me.olix3001.solids.Solid;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Viewport extends JPanel {

    private JFrame frame;
    private BufferedImage frameBuffer;
    private Scene scene;

    private Vector3 cameraMovement;
    private float resolution;
    private float deltaTime;
    private Vector3 cameraPosition;
    private Camera camera;
    private boolean autoResolution = false;
    private boolean captureMovement;
    private int prevX, prevY;

    private RenderRunnable beforeRender = null;
    private RenderRunnable afterRender = null;

    public Viewport(JFrame container, float resolution) {
        setFocusable(true);

        scene = new Scene();

        camera = scene.getCamera();
        cameraPosition = camera.getPosition();
        cameraMovement = new Vector3(0, 0, 0);
        // camera movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // x
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    cameraMovement.setX(.2f);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    cameraMovement.setX(-.2f);
                }
                // z
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    cameraMovement.setZ(.2f);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    cameraMovement.setZ(-.2f);
                }
                // y
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    cameraMovement.setY(.2f);
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraMovement.setY(-.2f);
                }

                // RESOLUTION
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    setResolution(.05f);
                }
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    setResolution(.1f);
                }
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    setResolution(.25f);
                }
                if (e.getKeyCode() == KeyEvent.VK_3) {
                    setResolution(.5f);
                }
                if (e.getKeyCode() == KeyEvent.VK_4) {
                    setResolution(.75f);
                }
                if (e.getKeyCode() == KeyEvent.VK_5) {
                    setResolution(1f);
                }
                if (e.getKeyCode() == KeyEvent.VK_0) {
                    autoResolution = !autoResolution;
                }

                // other
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    askAndSaveScreenshot();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // x
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                    cameraMovement.setX(0);
                }
                // z
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
                    cameraMovement.setZ(0);
                }
                // y
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraMovement.setY(0);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (captureMovement) {
                    autoResolutionOn();
                    int mouseXOffset = e.getXOnScreen() - prevX;
                    int mouseYOffset = e.getYOnScreen() - prevY;
                    prevX = e.getXOnScreen();
                    prevY = e.getYOnScreen();
                    camera.setYaw(camera.getYaw() + mouseXOffset * -.1f);
                    camera.setPitch(Math.min(90, Math.max(-90, camera.getPitch() + mouseYOffset * -.1f)));
                } else {
                    autoResolutionOff();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                captureMovement = !captureMovement;
                prevX = e.getXOnScreen();
                prevY = e.getYOnScreen();
            }
        });


        this.frame = container;
        this.resolution = resolution;
        this.deltaTime = 1;
    }

    public void MainLoop() {
        while(true) {
            long startTime = System.currentTimeMillis();

            // camera movement
            if (cameraMovement.length() != 0) {
                cameraPosition.translate(cameraMovement.rotateYP(camera.getYaw(), camera.getPitch()).multiply(deltaTime/50f*1.5f));
            }

            if (beforeRender != null) beforeRender.run(deltaTime);

            BufferedImage tempBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Renderer.renderScene(scene, tempBuffer.getGraphics(), getWidth(), getHeight(), resolution);
            frameBuffer = tempBuffer;

            repaint();

            if (afterRender != null) afterRender.run(deltaTime);

            deltaTime = System.currentTimeMillis() - startTime;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (frameBuffer != null) g.drawImage(frameBuffer, 0, 0, this);

        g.setColor(Color.white);
        FontMetrics fm = g.getFontMetrics();
        // draw fps count
        String fps = String.valueOf(1000f/deltaTime);
        if (fps.length() > 4) fps = fps.substring(0,4);
        String fpsString = fps+" fps";
        Rectangle2D fpsBounds = fm.getStringBounds(fpsString, g);
        String resolutionString = (resolution*100f)+"%";
        Rectangle2D resBounds = fm.getStringBounds(resolutionString, g);
        String countString =  scene.countSolids() +" objects";
        Rectangle2D countBounds = fm.getStringBounds(fpsString, g);

        g.drawString(fpsString, 10, (int) (fpsBounds.getHeight()));
        g.drawString(resolutionString, 10, (int) (resBounds.getHeight()*2));
        g.drawString(countString, 10, (int) (countBounds.getHeight()*3));

        // draw controls
        String controls = "Resolution: [L] 5%, [1] 10%, [2] 25%, [3] 50%, [4] 75%, [5] 100%, [0] Toggle auto (" + (autoResolution ? "ON" : "OFF") + ")";
        String controls2 = "Controls: [W] Forward, [S] Backward, [A] Left, [D] Right, [SPACE] Up, [SHIFT] Down, [F1] Save image";

        Rectangle2D controlsBounds = fm.getStringBounds(controls, g);
        Rectangle2D controls2Bounds = fm.getStringBounds(controls2, g);

        g.drawString(controls, 10, (int) (getHeight() - controls2Bounds.getHeight()*2));
        g.drawString(controls2, 10, (int) (getHeight() - controls2Bounds.getHeight()));
    }

    public void askAndSaveScreenshot() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG IMAGE", "png"));
        fileChooser.setSelectedFile(new File("raytracing.png"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // render
            BufferedImage saveBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Renderer.renderScene(scene, saveBuffer.getGraphics(), getWidth(), getHeight(), 1f);
            // save
            try {
                ImageIO.write(saveBuffer, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    private void autoResolutionOn() {
        if (autoResolution)
            this.resolution = .25f;
    }

    private void autoResolutionOff() {
        if (autoResolution)
            this.resolution = 1f;
    }

    public void beforeRender(RenderRunnable beforeRender) {
        this.beforeRender = beforeRender;
    }

    public void afterRender(RenderRunnable afterRender) {
        this.afterRender = afterRender;
    }

    public static abstract class RenderRunnable {
        public abstract void run(float deltaTime);
    }
}


