package com.xebisco.yield.editor.app.props;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropPanel extends JPanel {

    private final Prop[] props;
    public PropPanel(Prop[] props) {
        this.props = props;
        setLayout(new BorderLayout());
        setOpaque(false);

        List<JPanel> containers = new ArrayList<>();
        List<Border> containerBorders = new ArrayList<>();
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        containers.add(panel);

        for (Prop prop : props) {
            if(prop instanceof ContainerProp cp) {
                JPanel b;
                containers.add(b = cp.render());
                b.setOpaque(false);
                containerBorders.add(b.getBorder());
                b.setBorder(null);
            } else if(prop instanceof ExitContainerProp) {
                JPanel pnewC = new JPanel();
                pnewC.setOpaque(false);
                pnewC.setLayout(new BorderLayout());
                pnewC.add(containers.get(containers.size() - 2), BorderLayout.NORTH);
                JPanel p2newC = new JPanel();
                p2newC.setOpaque(false);
                p2newC.setLayout(new BorderLayout());
                p2newC.add(containers.getLast(), BorderLayout.NORTH);
                p2newC.setBorder(containerBorders.getLast());
                containerBorders.removeLast();
                JPanel newC = new JPanel();
                newC.setOpaque(false);
                newC.setLayout(new BorderLayout());
                newC.add(pnewC, BorderLayout.NORTH);
                newC.add(p2newC);
                containers.removeLast();
                JPanel newCP = new JPanel();
                newCP.setOpaque(false);
                newCP.setLayout(new BorderLayout());
                newCP.add(newC, BorderLayout.NORTH);

                containers.set(containers.size() - 1, newCP);
            } else {
                containers.getLast().add(prop.render());
                containers.getLast().setOpaque(false);
                JPanel newC = new JPanel();
                newC.setOpaque(false);
                newC.setLayout(new BorderLayout());
                newC.add(containers.getLast(), BorderLayout.NORTH);
                containers.set(containers.size() - 1, newC);

                JPanel space = new JPanel();
                space.setOpaque(false);
                space.setPreferredSize(new Dimension(6, 6));
                containers.getLast().add(space);
                newC = new JPanel();
                newC.setOpaque(false);
                newC.setLayout(new BorderLayout());
                newC.add(containers.getLast(), BorderLayout.NORTH);
                containers.set(containers.size() - 1, newC);
            }
        }
        while (containers.size() > 1) {
            JPanel pnewC = new JPanel();
            pnewC.setLayout(new BorderLayout());
            pnewC.add(containers.get(containers.size() - 2), BorderLayout.NORTH);
            JPanel p2newC = new JPanel();
            p2newC.setLayout(new BorderLayout());
            p2newC.add(containers.getLast(), BorderLayout.NORTH);
            p2newC.setBorder(containerBorders.getLast());
            containerBorders.removeLast();
            JPanel newC = new JPanel();
            newC.setLayout(new BorderLayout());
            newC.add(pnewC, BorderLayout.NORTH);
            newC.add(p2newC);
            containers.removeLast();
            JPanel newCP = new JPanel();
            newCP.setLayout(new BorderLayout());
            newCP.add(newC, BorderLayout.NORTH);

            containers.set(containers.size() - 1, newCP);
        }

        add(containers.get(0));
    }

    public static HashMap<String, Serializable> values(Prop[] props) {
        HashMap<String, Serializable> values = new HashMap<>();
        for(Prop prop : props) {
            if(prop.name() != null) values.put(prop.name(), prop.value());
        }
        return values;
    }

    public static void insert(Prop[] props, Map<String, Serializable> values) {
        for(Prop prop : props) {
            if(prop.name() != null) prop.setValue(values.get(prop.name()));
        }
    }

    public Prop[] props() {
        return props;
    }
}
