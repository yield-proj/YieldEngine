package com.xebisco.yield.input;

import com.xebisco.yield.View;
import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.utils.YldAction;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class YldInput implements KeyListener, MouseListener, org.newdawn.slick.KeyListener, org.newdawn.slick.MouseListener
{

    private HashMap<Keys, YldAction> shortcuts = new HashMap<>();
    private ArrayList<Integer> pressing = new ArrayList<>();
    private boolean touching, clicking;
    private final YldWindow window;
    private final GameContainer slickApp;

    public YldInput(YldWindow window, GameContainer slickApp)
    {
        this.window = window;
        this.slickApp = slickApp;
        if (window != null)
        {
            window.getFrame().addKeyListener(this);
            window.getFrame().addMouseListener(this);
        }
        else if (slickApp != null)
        {
            slickApp.getInput().addKeyListener(this);
            slickApp.getInput().addMouseListener(this);
        }
    }

    private void kp(int key)
    {
        pressing.add(key);
        Keys keys = new Keys(pressing.toArray(new Integer[0]));
        if (shortcuts.containsKey(keys))
        {
            shortcuts.get(keys).onAction();
        }
    }

    private void kr(int key)
    {
        pressing.removeIf(s -> (s == key));
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        kp(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        kr(e.getKeyCode());
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        clicking = true;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        touching = true;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        touching = false;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    public boolean isPressing(int... keyCode)
    {
        int contains = 0;
        int i = 0;
        while (i < keyCode.length)
        {
            if (pressing.contains(keyCode[i]))
            {
                contains++;
                break;
            }
            i++;
        }
        return contains == keyCode.length;
    }

    public boolean justPressed(int... keyCode)
    {
        int contains = 0;
        int i = 0;
        while (i < keyCode.length)
        {
            if (pressing.contains(keyCode[i]))
            {
                pressing.remove(new Integer(keyCode[i]));
                contains++;
                break;
            }
            i++;
        }
        return contains == keyCode.length;
    }

    public void addShortcut(Keys keys, YldAction action)
    {
        shortcuts.put(keys, action);
    }

    public HashMap<Keys, YldAction> getShortcuts()
    {
        return shortcuts;
    }

    public void setShortcuts(HashMap<Keys, YldAction> shortcuts)
    {
        this.shortcuts = shortcuts;
    }

    public ArrayList<Integer> getPressing()
    {
        return pressing;
    }

    public void setPressing(ArrayList<Integer> pressing)
    {
        this.pressing = pressing;
    }

    public int getX()
    {
        int p = MouseInfo.getPointerInfo().getLocation().x;
        if (window != null)
        {
            p -= window.getFrame().getX() + window.getFrame().getInsets().left;
            if (View.getActView() == null)
            {
                if (p > window.getWindowG().getWidth())
                    p = window.getWindowG().getWidth();
            }
            else
            {
                p = (int) ((p / (float) window.getWindowG().getWidth()) * View.getActView().getWidth());
                if (p > View.getActView().getWidth())
                    p = View.getActView().getWidth();
            }
        }
        else
        {
            p -= Display.getX();
            if (View.getActView() == null)
            {
                if (p > Display.getWidth())
                    p = Display.getWidth();
            }
            else
            {
                p = (int) ((p / (float) Display.getWidth()) * View.getActView().getWidth());
                if (p > View.getActView().getWidth())
                    p = View.getActView().getWidth();
            }
        }
        if (p < 0)
            p = 0;
        return p;
    }

    public int getY()
    {
        int p = MouseInfo.getPointerInfo().getLocation().y;
        if (window != null)
        {
            p -= window.getFrame().getY() + window.getFrame().getInsets().top;
            if (View.getActView() == null)
            {
                if (p > window.getWindowG().getHeight())
                    p = window.getWindowG().getHeight();
            }
            else
            {
                p = (int) ((p / (float) window.getWindowG().getHeight()) * View.getActView().getHeight());
                if (p > View.getActView().getHeight())
                    p = View.getActView().getHeight();
            }
        }
        else
        {
            p -= Display.getX();
            if (View.getActView() == null)
            {
                if (p > Display.getHeight())
                    p = Display.getHeight();
            }
            else
            {
                p = (int) ((p / (float) Display.getHeight()) * View.getActView().getHeight());
                if (p > View.getActView().getHeight())
                    p = View.getActView().getHeight();
            }
        }
        if (p < 0)
            p = 0;

        return p;
    }

    public boolean isTouching()
    {
        return touching;
    }

    public void setTouching(boolean touching)
    {
        this.touching = touching;
    }

    public boolean isClicking()
    {
        return clicking;
    }

    public void setClicking(boolean clicking)
    {
        this.clicking = clicking;
    }

    public YldWindow getWindow()
    {
        return window;
    }

    public GameContainer getSlickApp()
    {
        return slickApp;
    }

    @Override
    public void keyPressed(int i, char c)
    {
        kp(KeyEvent.getExtendedKeyCodeForChar(c));
    }

    @Override
    public void keyReleased(int i, char c)
    {
        kr(KeyEvent.getExtendedKeyCodeForChar(c));
    }

    @Override
    public void mouseWheelMoved(int i)
    {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3)
    {
        clicking = true;
    }

    @Override
    public void mousePressed(int i, int i1, int i2)
    {
        touching = true;
    }

    @Override
    public void mouseReleased(int i, int i1, int i2)
    {
        touching = false;
    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3)
    {

    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3)
    {

    }

    @Override
    public void setInput(Input input)
    {

    }

    @Override
    public boolean isAcceptingInput()
    {
        return true;
    }

    @Override
    public void inputEnded()
    {

    }

    @Override
    public void inputStarted()
    {

    }
}
