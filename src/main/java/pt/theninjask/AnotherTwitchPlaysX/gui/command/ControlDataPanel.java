package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandVarType;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.data.MouseCoordsType;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords.MouseCoordsListener;
import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class ControlDataPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ControlData data;

    //private List<ControlDataPanel> in;

    private JComboBox<Integer> index;

    private CommandPanel parent;

    private AtomicBoolean isRefreshActive;

    private MouseCoordsListener listener = null;

    private MouseCoordsListener listenerFinal = null;

    public enum Type {
        NORMAL, VAR
    }

    private List<Component> normal = new ArrayList<Component>();

    private List<Component> var = new ArrayList<Component>();

    private JButton key;

    private JComboBox<JComboItem<Integer>> opt;

    private JComboBoxVar inputVar;

    private JTextField duration;

    private JComboBoxVar durationVar;

    private JTextField aftermath;

    private JComboBoxVar aftermathVar;

    private JFormattedTextField x;

    private JButton xClear;

    private JComboBoxVar xVar;

    private JFormattedTextField y;

    private JButton yClear;

    private JComboBoxVar yVar;

    private JFormattedTextField finalX;

    private JButton finalXClear;

    private JComboBoxVar finalXVar;

    private JFormattedTextField finalY;

    private JButton finalYClear;

    private JComboBoxVar finalYVar;

    private JButton remove;

    private JButton xType;

    private JButton yType;

    private JButton finalXType;

    private JButton finalYType;

    private JLabel inputLabel;

    private JLabel na;

    private JLabel durationLabel;

    private JLabel aftermathLabel;

    private JLabel xLabel;

    private JLabel yLabel;

    private JLabel cycleLabel;

    private JLabel finalXLabel;

    private JLabel finalYLabel;

    private JLabel indexLabel;

    private JButton cycle;

    private static String jComboBoxVarNone = DataManager.getLanguage().getControlData().getVarNone();

    private class JComboBoxVar extends JComboBox<JComboItem<Pair<String, CommandVarType>>> {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private CommandVarType type;

        private Map<String, JComboItem<Pair<String, CommandVarType>>> vars;

        private AtomicBoolean disable = new AtomicBoolean(false);

        private String varOf;

        public JComboBoxVar(CommandVarType type, String varOf) {
            super();
            this.vars = new HashMap<String, JComboItem<Pair<String, CommandVarType>>>();
            this.addItem(new JComboItem<Pair<String, CommandVarType>>(null, jComboBoxVarNone));
            this.setSelectedIndex(0);
            this.type = type;
            this.varOf = varOf;
            this.addActionListener(l -> {
                if (disable.get())
                    return;
                if (getSelectedIndex() < 0)
                    return;
                if (getSelectedIndex() == 0) {
                    data.getMap().remove(this.varOf);
                    return;
                }
                data.getMap().put(this.varOf, getItemAt(getSelectedIndex()).get().getLeft());
                /*
                 * switch (getItemAt(getSelectedIndex()).toString()) { case None:
                 * data.getMap().remove(this.varOf); break; default:
                 * data.getMap().put(this.varOf, getItemAt(getSelectedIndex()).get().getLeft());
                 * break; }
                 */
            });
        }

        public void addItem(Pair<String, CommandVarType> var) {
            if (var == null || var.getRight() != type || jComboBoxVarNone.equals(var.getLeft())
                    || vars.containsKey(var.getLeft()))
                return;
            JComboItem<Pair<String, CommandVarType>> tmp = new JComboItem<Pair<String, CommandVarType>>(var,
                    var.getLeft());
            vars.put(var.getLeft(), tmp);
            addItem(tmp);
        }

        public void removeItem(Pair<String, CommandVarType> var) {
            if (var == null || var.getRight() != type || jComboBoxVarNone.equals(var.getLeft())
                    || !vars.containsKey(var.getLeft()))
                return;
            if (getItemAt(getSelectedIndex()).get() != null && getItemAt(getSelectedIndex()).get().equals(var))
                setSelectedDefault();
            removeItem(vars.remove(var.getLeft()));
        }

        public void setSelectedDefault() {
            this.setSelectedIndex(0);
        }

        public void refresh() {
            disable.set(true);
            vars.clear();
            super.removeAllItems();
            addItem(new JComboItem<Pair<String, CommandVarType>>(null, jComboBoxVarNone));
            parent.getCurrentCommandData().getVars().forEach(e -> {
                addVar(e);
            });
            String tmp = data.getMap().get(varOf);
            if (tmp != null)
                setSelectedItem(vars.get(tmp));
            else
                setSelectedDefault();
            disable.set(false);
        }
    }

    public ControlDataPanel(ControlData newData, CommandPanel parent) {
        Constants.printVerboseMessage(Level.INFO,
                String.format("%s(%s)", ControlDataPanel.class.getSimpleName(), this.hashCode()));
        this.data = newData;
        this.parent = parent;
        //this.in = in;
        this.isRefreshActive = new AtomicBoolean(false);
        //this.in.add(this);
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel center;
        switch (data.getType()) {
            case KEY:
                center = new JPanel(new GridLayout(3, 1));
                break;
            default:
                center = new JPanel(new GridLayout(5, 1));
                break;
        }
        center.setOpaque(false);
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEADING));
        content.setOpaque(false);
        inputPanel.setOpaque(false);
        inputLabel = new JLabel(
                data.getType() == ControlType.KEY ? DataManager.getLanguage().getControlData().getTypeKey()
                        : DataManager.getLanguage().getControlData().getTypeButton());
        inputLabel.setForeground(DataManager.getTheme().font());
        inputPanel.add(inputLabel, BorderLayout.WEST);
        switch (data.getType()) {
            case KEY:
                key = new JButton(DataManager.getLanguage().getControlData().getKeyNone());
                if (data.getKey() != null)
                    key.setText(KeyEvent.getKeyText(data.getKey()));
                normal.add(key);
                key.setFocusable(false);
                key.addActionListener(l -> {
                    JPanel pressPanel = new JPanel(new GridLayout(2, 1));
                    pressPanel.setBackground(DataManager.getTheme().background());
                    JLabel press = new JLabel(String.format(DataManager.getLanguage().getControlData().getKeyCurrent(),
                            data.getKey() == null ? DataManager.getLanguage().getControlData().getKeyNone()
                                    : KeyEvent.getKeyText(data.getKey())));
                    press.setForeground(DataManager.getTheme().font());
                    press.setHorizontalAlignment(JLabel.CENTER);
                    pressPanel.add(press);
                    pressPanel.setFocusable(true);
                    pressPanel.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == Constants.stopKey) {
                                press.setText(String.format(DataManager.getLanguage().getControlData().getKeyCurrent(),
                                        DataManager.getLanguage().getControlData().getKeyNone()));
                                data.setKey(null);
                                key.setText(DataManager.getLanguage().getControlData().getKeyNone());
                            } else {
                                press.setText(String.format(DataManager.getLanguage().getControlData().getKeyCurrent(),
                                        KeyEvent.getKeyText(e.getKeyCode())));
                                data.setKey(e.getKeyCode());
                                key.setText(KeyEvent.getKeyText(e.getKeyCode()));
                            }
                        }
                    });
                    // JButton ok = new JButton("Confirm");
                    // pressPanel.add(ok);
                    new PopOutFrame(pressPanel, MainFrame.getInstance());
                });
                content.add(key);
                break;
            // case MOUSE_CLICK:
            case MOUSE:
            case MOUSE_DRAG:
                opt = new JComboBox<JComboItem<Integer>>();
                normal.add(opt);
                opt.setFocusable(false);
                opt.addItem(new JComboItem<Integer>(null, DataManager.getLanguage().getControlData().getButtonNone()));
                opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON1_DOWN_MASK,
                        DataManager.getLanguage().getControlData().getButtonLeft()));
                opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON3_DOWN_MASK,
                        DataManager.getLanguage().getControlData().getButtonRight()));
                opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON2_DOWN_MASK,
                        DataManager.getLanguage().getControlData().getButtonMiddle()));
                opt.addActionListener(l -> {
                    this.data.setKey(opt.getItemAt(opt.getSelectedIndex()).get());
                });
                if (data.getKey() != null) {
                    for (int i = 0; i < opt.getItemCount(); i++) {
                        if (data.getKey().equals(opt.getItemAt(i).get())) {
                            opt.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                content.add(opt);
                break;
            // case MOUSE_MOV:
            case MOUSE_WHEEL:
            default:
                na = new JLabel(DataManager.getLanguage().getNA());
                normal.add(na);
                na.setForeground(DataManager.getTheme().font());
                content.add(na);
                break;
        }
        inputPanel.add(content);
        inputVar = new JComboBoxVar(CommandVarType.STRING, ControlData.KEY_VAR);
        inputVar.setVisible(false);
        // inputVar.setEnabled(false);
        var.add(inputVar);
        content.add(inputVar);

        inputPanel.add(content);

        center.add(inputPanel);

        /*
         * DecimalFormatSymbols symb = new DecimalFormatSymbols();
         * symb.setGroupingSeparator(Character.MIN_VALUE); DecimalFormat format = new
         * DecimalFormat(); format.setDecimalFormatSymbols(symb);
         */
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);

        if (data.getType() != ControlType.MOUSE_DRAG) {

            NumberFormatter durationFormatter = new NumberFormatter(format);
            durationFormatter.setValueClass(Integer.class);
            durationFormatter.setMinimum(0);
            durationFormatter.setMaximum(60 * 1000);
            durationFormatter.setAllowsInvalid(false);
            JPanel durationPanel = new JPanel(new BorderLayout());
            durationPanel.setOpaque(false);
            content = new JPanel(new FlowLayout(FlowLayout.LEADING));
            content.setOpaque(false);
            durationLabel = new JLabel(DataManager.getLanguage().getControlData().getDuration());
            durationLabel.setForeground(DataManager.getTheme().font());
            durationPanel.add(durationLabel, BorderLayout.WEST);
            duration = new JFormattedTextField(durationFormatter);
            normal.add(duration);
            duration.setBorder(null);
            if (data.getDuration() != null)
                duration.setText(Integer.toString(data.getDuration()));
            duration.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    try {
                        data.setDuration(Integer.parseInt(duration.getText()));
                    } catch (NumberFormatException e) {
                    }
                }
            });
            /*
             * duration.addActionListener(l->{
             * data.setDuration(Integer.parseInt(duration.getText())); });
             */
            duration.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        default:
                            int updated;
                            Integer durationVal;
                            break;
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_KP_UP:
                            durationVal = data.getDuration();
                            if (durationVal == null)
                                break;
                            if (durationVal == 60 * 1000)
                                break;
                            updated = durationVal + 1;
                            data.setDuration(updated);
                            duration.setText(Integer.toString(updated));
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_KP_DOWN:
                            durationVal = data.getDuration();
                            if (durationVal == null)
                                break;
                            updated = durationVal - 1;
                            if (updated < 0)
                                break;
                            data.setDuration(updated);
                            duration.setText(Integer.toString(updated));
                            break;
                    }
                }
            });
            duration.setPreferredSize(new Dimension(50, 20));
            content.add(duration);

            durationVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.DURATION_VAR);
            durationVar.setVisible(false);
            var.add(durationVar);
            content.add(durationVar);

            durationPanel.add(content);

            center.add(durationPanel);
        }

        JPanel aftermathPanel = new JPanel(new BorderLayout());
        aftermathPanel.setOpaque(false);
        content = new JPanel(new FlowLayout(FlowLayout.LEADING));
        content.setOpaque(false);
        aftermathLabel = new JLabel(DataManager.getLanguage().getControlData().getAftermath());
        aftermathLabel.setForeground(DataManager.getTheme().font());
        aftermathPanel.add(aftermathLabel, BorderLayout.WEST);
        NumberFormatter aftermathFormatter = new NumberFormatter(format);
        aftermathFormatter.setValueClass(Integer.class);
        aftermathFormatter.setMinimum(0);
        aftermathFormatter.setMaximum(60 * 1000);
        aftermathFormatter.setAllowsInvalid(false);
        aftermath = new JFormattedTextField(aftermathFormatter);
        normal.add(aftermath);
        aftermath.setBorder(null);
        if (data.getAftermathDelay() != null)
            aftermath.setText(Integer.toString(data.getAftermathDelay()));
        aftermath.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    data.setAftermathDelay(Integer.parseInt(aftermath.getText()));
                } catch (NumberFormatException e) {
                }
            }
        });
        aftermath.setPreferredSize(new Dimension(50, 20));
        aftermath.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    default:
                        int updated;
                        Integer aftermathVal;
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_KP_UP:
                        aftermathVal = data.getAftermathDelay();
                        if (aftermathVal == null)
                            break;
                        if (aftermathVal == 60 * 1000)
                            break;
                        updated = aftermathVal + 1;
                        data.setAftermathDelay(updated);
                        aftermath.setText(Integer.toString(updated));
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_KP_DOWN:
                        aftermathVal = data.getAftermathDelay();
                        if (aftermathVal == null)
                            break;
                        updated = aftermathVal - 1;
                        if (updated < 0)
                            break;
                        data.setAftermathDelay(updated);
                        aftermath.setText(Integer.toString(updated));
                        break;
                }
            }
        });
        content.add(aftermath);

        aftermathVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.AFTERMATH_VAR);
        aftermathVar.setVisible(false);
        var.add(aftermathVar);
        content.add(aftermathVar);

        aftermathPanel.add(content);

        center.add(aftermathPanel);
        if ((data.getType() == ControlType.MOUSE || data.getType() == ControlType.MOUSE_DRAG)
                && data.getInDepthCursor() != null) {
            NumberFormatter xFormatter = new NumberFormatter(format);
            xFormatter.setValueClass(Integer.class);
            xFormatter.setMinimum(Integer.MIN_VALUE);
            xFormatter.setMaximum(Integer.MAX_VALUE);
            xFormatter.setAllowsInvalid(false);
            JPanel xPanel = new JPanel(new BorderLayout());
            xPanel.setOpaque(false);
            content = new JPanel(new FlowLayout(FlowLayout.LEADING));
            content.setOpaque(false);
            xLabel = new JLabel(String.format(DataManager.getLanguage().getControlData().getX(),
                    MouseCoords.getInstance().getX().get()));
            xLabel.setForeground(DataManager.getTheme().font());
            xPanel.add(xLabel, BorderLayout.WEST);
            x = new JFormattedTextField(xFormatter);
            normal.add(x);
            x.setBorder(null);
            if (data.getInDepthCursor().getX().getLeft() != null)
                x.setText(data.getInDepthCursor().getX().getLeft().toString());
            x.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    try {
                        data.getInDepthCursor().getX().setLeft(Integer.parseInt(x.getText()));
                    } catch (NumberFormatException e) {
                    }
                }
            });
            x.setPreferredSize(new Dimension(40, 20));
            x.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        default:
                            int updated;
                            Integer xVal;
                            break;
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_KP_UP:
                            xVal = data.getInDepthCursor().getX().getLeft();
                            if (xVal == null)
                                break;
                            if (xVal == Integer.MAX_VALUE)
                                break;
                            updated = xVal + 1;
                            data.getInDepthCursor().getX().setLeft(updated);
                            x.setValue(updated);
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_KP_DOWN:
                            xVal = data.getInDepthCursor().getX().getLeft();
                            if (xVal == null)
                                break;
                            if (xVal == Integer.MIN_VALUE)
                                break;
                            updated = xVal - 1;
                            data.getInDepthCursor().getX().setLeft(updated);
                            x.setValue(updated);
                            break;
                    }
                }
            });
            xClear = new JButton(DataManager.getLanguage().getControlData().getXClear());
            normal.add(xClear);
            xClear.setFocusable(false);
            xClear.setMargin(new Insets(0, 0, 0, 0));
            xClear.setPreferredSize(Constants.X_BUTTON);
            xClear.addActionListener(l -> {
                x.setValue(null);
                data.getInDepthCursor().getX().setLeft(null);
            });
            content.add(xClear);
            content.add(x);

            xVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.X_VAR);
            xVar.setVisible(false);
            var.add(xVar);
            content.add(xVar);

            xType = new JButton(DataManager.getLanguage().getControlData().getAbs());
            if (data.getInDepthCursor().getX().getRight() == MouseCoordsType.REL)
                xType.setText(DataManager.getLanguage().getControlData().getRel());
            xType.setFocusable(false);
            xType.setMargin(new Insets(0, 0, 0, 0));
            xType.addActionListener(l -> {
                switch (data.getInDepthCursor().getX().getRight()) {
                    case ABS:
                        data.getInDepthCursor().getX().setRight(MouseCoordsType.REL);
                        xType.setText(DataManager.getLanguage().getControlData().getRel());
                        break;
                    case REL:
                    default:
                        data.getInDepthCursor().getX().setRight(MouseCoordsType.ABS);
                        xType.setText(DataManager.getLanguage().getControlData().getAbs());
                        break;
                }
            });
            xPanel.add(xType, BorderLayout.EAST);
            xPanel.add(content);

            center.add(xPanel);

            JPanel yPanel = new JPanel(new BorderLayout());
            yPanel.setOpaque(false);
            content = new JPanel(new FlowLayout(FlowLayout.LEADING));
            content.setOpaque(false);
            yLabel = new JLabel(String.format(DataManager.getLanguage().getControlData().getY(),
                    MouseCoords.getInstance().getY().get()));
            yLabel.setForeground(DataManager.getTheme().font());
            yPanel.add(yLabel, BorderLayout.WEST);
            NumberFormatter yFormatter = new NumberFormatter(format);
            yFormatter.setValueClass(Integer.class);
            yFormatter.setMinimum(Integer.MIN_VALUE);
            yFormatter.setMaximum(Integer.MAX_VALUE);
            yFormatter.setAllowsInvalid(false);
            y = new JFormattedTextField(yFormatter);
            normal.add(y);
            y.setBorder(null);
            if (data.getInDepthCursor().getY().getLeft() != null)
                y.setText(data.getInDepthCursor().getY().getLeft().toString());
            y.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    try {
                        data.getInDepthCursor().getY().setLeft(Integer.parseInt(y.getText()));
                    } catch (NumberFormatException e) {
                    }
                }
            });
            y.setPreferredSize(new Dimension(40, 20));
            y.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        default:
                            int updated;
                            Integer yVal;
                            break;
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_KP_UP:
                            yVal = data.getInDepthCursor().getY().getLeft();
                            if (yVal == null)
                                break;
                            if (yVal == Integer.MAX_VALUE)
                                break;
                            updated = yVal + 1;
                            data.getInDepthCursor().getY().setLeft(updated);
                            y.setValue(updated);
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_KP_DOWN:
                            yVal = data.getInDepthCursor().getY().getLeft();
                            if (yVal == null)
                                break;
                            if (yVal == Integer.MIN_VALUE)
                                break;
                            updated = yVal - 1;
                            data.getInDepthCursor().getY().setLeft(updated);
                            y.setValue(updated);
                            break;
                    }
                }
            });
            yClear = new JButton(DataManager.getLanguage().getControlData().getYClear());
            normal.add(yClear);
            yClear.setFocusable(false);
            yClear.setMargin(new Insets(0, 0, 0, 0));
            yClear.setPreferredSize(Constants.X_BUTTON);
            yClear.addActionListener(l -> {
                y.setValue(null);
                data.getInDepthCursor().getY().setLeft(null);
            });
            content.add(yClear);
            content.add(y);

            yVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.Y_VAR);
            yVar.setVisible(false);
            var.add(yVar);
            content.add(yVar);

            yType = new JButton(DataManager.getLanguage().getControlData().getAbs());
            if (data.getInDepthCursor().getY().getRight() == MouseCoordsType.REL)
                yType.setText(DataManager.getLanguage().getControlData().getRel());
            yType.setFocusable(false);
            yType.setMargin(new Insets(0, 0, 0, 0));
            yType.addActionListener(l -> {
                switch (data.getInDepthCursor().getY().getRight()) {
                    case ABS:
                        data.getInDepthCursor().getY().setRight(MouseCoordsType.REL);
                        yType.setText(DataManager.getLanguage().getControlData().getRel());
                        break;
                    case REL:
                    default:
                        data.getInDepthCursor().getY().setRight(MouseCoordsType.ABS);
                        yType.setText(DataManager.getLanguage().getControlData().getAbs());
                        break;
                }
            });

            yPanel.add(yType, BorderLayout.EAST);
            yPanel.add(content);

            center.add(yPanel);
            listener = (eX, eY) -> {
                xLabel.setText(String.format(DataManager.getLanguage().getControlData().getX(),
                        MouseCoords.getInstance().getX().get()));
                yLabel.setText(String.format(DataManager.getLanguage().getControlData().getY(),
                        MouseCoords.getInstance().getY().get()));
            };
            MouseCoords.getInstance().registerListener(listener);
            if (data.getType() == ControlType.MOUSE_DRAG) {
                JPanel cyclePanel = new JPanel(new BorderLayout());
                cyclePanel.setOpaque(false);
                cycleLabel = new JLabel(DataManager.getLanguage().getControlData().getInitialCoords());
                cycleLabel.setForeground(DataManager.getTheme().font());
                cycleLabel.setHorizontalAlignment(JLabel.CENTER);
                cyclePanel.add(cycleLabel, BorderLayout.CENTER);
                /*
                 *
                 */
                NumberFormatter finalXFormatter = new NumberFormatter(format);
                finalXFormatter.setValueClass(Integer.class);
                finalXFormatter.setMinimum(Integer.MIN_VALUE);
                finalXFormatter.setMaximum(Integer.MAX_VALUE);
                finalXFormatter.setAllowsInvalid(false);
                JPanel finalXPanel = new JPanel(new BorderLayout());
                finalXPanel.setOpaque(false);
                content = new JPanel(new FlowLayout(FlowLayout.LEADING));
                content.setOpaque(false);
                finalXLabel = new JLabel(String.format(DataManager.getLanguage().getControlData().getFinalX(),
                        MouseCoords.getInstance().getX().get()));
                finalXLabel.setForeground(DataManager.getTheme().font());
                finalXPanel.add(finalXLabel, BorderLayout.WEST);
                finalX = new JFormattedTextField(finalXFormatter);
                normal.add(finalX);
                finalX.setBorder(null);
                if (data.getInDepthCursor().getFinalX().getLeft() != null)
                    finalX.setText(data.getInDepthCursor().getFinalX().getLeft().toString());
                finalX.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }

                    private void update() {
                        try {
                            data.getInDepthCursor().getFinalX().setLeft(Integer.parseInt(finalX.getText()));
                        } catch (NumberFormatException e) {
                        }
                    }
                });
                finalX.setPreferredSize(new Dimension(40, 20));
                finalX.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            default:
                                int updated;
                                Integer xVal;
                                break;
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_KP_UP:
                                xVal = data.getInDepthCursor().getFinalX().getLeft();
                                if (xVal == null)
                                    break;
                                if (xVal == Integer.MAX_VALUE)
                                    break;
                                updated = xVal + 1;
                                data.getInDepthCursor().getFinalX().setLeft(updated);
                                finalX.setValue(updated);
                                break;
                            case KeyEvent.VK_DOWN:
                            case KeyEvent.VK_KP_DOWN:
                                xVal = data.getInDepthCursor().getFinalX().getLeft();
                                if (xVal == null)
                                    break;
                                if (xVal == Integer.MIN_VALUE)
                                    break;
                                updated = xVal - 1;
                                data.getInDepthCursor().getFinalX().setLeft(updated);
                                finalX.setValue(updated);
                                break;
                        }
                    }
                });
                finalXClear = new JButton(DataManager.getLanguage().getControlData().getFinalXClear());
                normal.add(finalXClear);
                finalXClear.setFocusable(false);
                finalXClear.setMargin(new Insets(0, 0, 0, 0));
                finalXClear.setPreferredSize(Constants.X_BUTTON);
                finalXClear.addActionListener(l -> {
                    finalX.setValue(null);
                    data.getInDepthCursor().getFinalX().setLeft(null);
                });
                content.add(finalXClear);
                content.add(finalX);

                finalXVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.FINAL_X_VAR);
                finalXVar.setVisible(false);
                var.add(finalXVar);
                content.add(finalXVar);

                finalXType = new JButton(DataManager.getLanguage().getControlData().getAbs());
                if (data.getInDepthCursor().getFinalX().getRight() == MouseCoordsType.REL)
                    finalXType.setText(DataManager.getLanguage().getControlData().getRel());
                finalXType.setFocusable(false);
                finalXType.setMargin(new Insets(0, 0, 0, 0));
                finalXType.addActionListener(l -> {
                    switch (data.getInDepthCursor().getFinalX().getRight()) {
                        case ABS:
                            data.getInDepthCursor().getFinalX().setRight(MouseCoordsType.REL);
                            finalXType.setText(DataManager.getLanguage().getControlData().getRel());
                            break;
                        case REL:
                        default:
                            data.getInDepthCursor().getFinalX().setRight(MouseCoordsType.ABS);
                            finalXType.setText(DataManager.getLanguage().getControlData().getAbs());
                            break;
                    }
                });

                finalXPanel.add(finalXType, BorderLayout.EAST);
                finalXPanel.add(content);

                // center.add(finalXPanel);

                JPanel finalYPanel = new JPanel(new BorderLayout());
                finalYPanel.setOpaque(false);
                content = new JPanel(new FlowLayout(FlowLayout.LEADING));
                content.setOpaque(false);
                finalYLabel = new JLabel(String.format(DataManager.getLanguage().getControlData().getFinalY(),
                        MouseCoords.getInstance().getY().get()));
                finalYLabel.setForeground(DataManager.getTheme().font());
                finalYPanel.add(finalYLabel, BorderLayout.WEST);
                NumberFormatter finalYFormatter = new NumberFormatter(format);
                finalYFormatter.setValueClass(Integer.class);
                finalYFormatter.setMinimum(Integer.MIN_VALUE);
                finalYFormatter.setMaximum(Integer.MAX_VALUE);
                finalYFormatter.setAllowsInvalid(false);
                finalY = new JFormattedTextField(finalYFormatter);
                normal.add(finalY);
                finalY.setBorder(null);
                if (data.getInDepthCursor().getFinalY().getLeft() != null)
                    finalY.setText(data.getInDepthCursor().getFinalY().getLeft().toString());
                finalY.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }

                    private void update() {
                        try {
                            data.getInDepthCursor().getFinalY().setLeft(Integer.parseInt(finalY.getText()));
                        } catch (NumberFormatException e) {
                        }
                    }
                });
                finalY.setPreferredSize(new Dimension(40, 20));
                finalY.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            default:
                                int updated;
                                Integer yVal;
                                break;
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_KP_UP:
                                yVal = data.getInDepthCursor().getFinalY().getLeft();
                                if (yVal == null)
                                    break;
                                if (yVal == Integer.MAX_VALUE)
                                    break;
                                updated = yVal + 1;
                                data.getInDepthCursor().getFinalY().setLeft(updated);
                                finalY.setValue(updated);
                                break;
                            case KeyEvent.VK_DOWN:
                            case KeyEvent.VK_KP_DOWN:
                                yVal = data.getInDepthCursor().getFinalY().getLeft();
                                if (yVal == null)
                                    break;
                                if (yVal == Integer.MIN_VALUE)
                                    break;
                                updated = yVal - 1;
                                data.getInDepthCursor().getFinalY().setLeft(updated);
                                finalY.setValue(updated);
                                break;
                        }
                    }
                });
                finalYClear = new JButton(DataManager.getLanguage().getControlData().getFinalYClear());
                normal.add(finalYClear);
                finalYClear.setFocusable(false);
                finalYClear.setMargin(new Insets(0, 0, 0, 0));
                finalYClear.setPreferredSize(Constants.X_BUTTON);
                finalYClear.addActionListener(l -> {
                    finalY.setValue(null);
                    data.getInDepthCursor().getFinalY().setLeft(null);
                });
                content.add(finalYClear);
                content.add(finalY);

                finalYVar = new JComboBoxVar(CommandVarType.DIGIT, ControlData.FINAL_Y_VAR);
                finalYVar.setVisible(false);
                var.add(finalYVar);
                content.add(finalYVar);

                finalYType = new JButton(DataManager.getLanguage().getControlData().getAbs());
                if (data.getInDepthCursor().getFinalY().getRight() == MouseCoordsType.REL)
                    finalYType.setText(DataManager.getLanguage().getControlData().getRel());
                finalYType.setFocusable(false);
                finalYType.setMargin(new Insets(0, 0, 0, 0));
                finalYType.addActionListener(l -> {
                    switch (data.getInDepthCursor().getFinalY().getRight()) {
                        case ABS:
                            data.getInDepthCursor().getFinalY().setRight(MouseCoordsType.REL);
                            finalYType.setText(DataManager.getLanguage().getControlData().getRel());
                            break;
                        case REL:
                        default:
                            data.getInDepthCursor().getFinalY().setRight(MouseCoordsType.ABS);
                            finalYType.setText(DataManager.getLanguage().getControlData().getAbs());
                            break;
                    }
                });

                finalYPanel.add(finalYType, BorderLayout.EAST);

                finalYPanel.add(content);

                // center.add(yPanel);
                listenerFinal = (eX, eY) -> {
                    finalXLabel.setText(String.format(DataManager.getLanguage().getControlData().getFinalX(),
                            MouseCoords.getInstance().getX().get()));
                    finalYLabel.setText(String.format(DataManager.getLanguage().getControlData().getFinalY(),
                            MouseCoords.getInstance().getY().get()));
                };
                MouseCoords.getInstance().registerListener(listenerFinal);
                /*
                 *
                 */
                cycle = new JButton(">");
                cycle.setFocusable(false);
                cycle.addActionListener(l -> {
                    switch (cycle.getText()) {
                        case ">":
                            cycleLabel.setText(DataManager.getLanguage().getControlData().getFinalCoords());
                            center.remove(xPanel);
                            center.remove(yPanel);
                            center.add(finalXPanel);
                            center.add(finalYPanel);
                            cycle.setText("<");
                            break;
                        case "<":
                            cycleLabel.setText(DataManager.getLanguage().getControlData().getInitialCoords());
                            center.remove(finalXPanel);
                            center.remove(finalYPanel);
                            center.add(xPanel);
                            center.add(yPanel);
                            cycle.setText(">");
                            break;
                        default:
                            // Never
                            cycleLabel.setText(DataManager.getLanguage().getNA());
                            break;
                    }
                    center.revalidate();
                    center.repaint();
                });
                cyclePanel.add(cycle, BorderLayout.EAST);
                center.add(cyclePanel, center.getComponentCount() - 2);
            }
        }
        this.add(center, BorderLayout.CENTER);
        JPanel left = new JPanel(new GridLayout(5, 1));
        left.setOpaque(false);

        // JPanel indexPanel = new JPanel();
        // indexPanel.setOpaque(false);
        indexLabel = new JLabel(DataManager.getLanguage().getControlData().getIndex());
        indexLabel.setForeground(DataManager.getTheme().font());
        left.add(indexLabel);
        index = new JComboBox<Integer>();
        index.setFocusable(false);
        index.addActionListener(l -> {
            if (isRefreshActive.get())
                return;
            parent.moveListControlDataPanel(index.getSelectedIndex(), this);
        });
        left.add(index);
        // left.add(indexPanel);
        // JButton help = new JButton("Help");
        remove = new JButton(DataManager.getLanguage().getControlData().getRemove());
        remove.setFocusable(false);
        remove.addActionListener(l -> {
            this.parent.leaveListControlDataPanel(this);
            if (listener != null)
                MouseCoords.getInstance().unregisterListener(listener);
            if (listenerFinal != null)
                MouseCoords.getInstance().unregisterListener(listenerFinal);
        });
        left.add(remove);
        // left.setPreferredSize(new Dimension(50, 151));
        this.add(left, BorderLayout.EAST);
        EventManager.registerEventListener(this);

        this.parent.joinListControlDataPanel(this);
    }

    public void refreshIndex() {
        // index.setSelectedIndex(-1);
        // System.out.println(index.getItemCount());
        isRefreshActive.set(true);
        index.removeAllItems();
        for (int i = 0; i < parent.sizeOfListControlDataPanel(); i++) {
            index.addItem(i);
        }
        index.setSelectedIndex(parent.getIndexListControlDataPanel(this));
        isRefreshActive.set(false);
    }

    public void setMode(Type type) {
        switch (type) {
            case NORMAL:
                for (Component elem : var) {
                    elem.setVisible(false);
                }
                for (Component elem : normal) {
                    elem.setVisible(true);
                }
                break;
            case VAR:
                for (Component elem : normal) {
                    elem.setVisible(false);
                }
                for (Component elem : var) {
                    if (elem instanceof JComboBoxVar) {
                        JComboBoxVar cast = (JComboBoxVar) elem;
                        cast.refresh();
                    }
                    elem.setVisible(true);
                }
                break;
            default:
                break;
        }

    }

    public void addVar(Pair<String, CommandVarType> var) {
        if (var == null)
            return;
        for (Component elem : this.var) {
            if (elem instanceof JComboBoxVar) {
                JComboBoxVar selec = ((JComboBoxVar) elem);
                selec.addItem(var);
            }
        }
    }

    public void removeVar(Pair<String, CommandVarType> var) {
        if (var == null)
            return;
        for (Component elem : this.var) {
            if (elem instanceof JComboBoxVar) {
                JComboBoxVar selec = ((JComboBoxVar) elem);
                selec.removeItem(var);
            }
        }
        data.getMap().forEach((k, v) -> {
            if (var.getLeft().equals(v))
                data.getMap().remove(k);
        });
    }

    public ControlData getControlData() {
        return data;
    }

    // @Handler
    public void updateLang(LanguageUpdateEvent event) {
        Lang session = event.getLanguage();
        if (session == null)
            return;

        jComboBoxVarNone = session.getControlData().getVarNone();
        inputLabel.setText(data.getType() == ControlType.KEY ? session.getControlData().getTypeKey()
                : session.getControlData().getTypeButton());
        if (key != null && data.getKey() == null)
            key.setText(session.getControlData().getKeyNone());
        if (opt != null) {
            // this approach seems meh
            int ind = opt.getSelectedIndex();
            opt.removeAllItems();
            opt.addItem(new JComboItem<Integer>(null, session.getControlData().getButtonNone()));
            opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON1_DOWN_MASK,
                    DataManager.getLanguage().getControlData().getButtonLeft()));
            opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON3_DOWN_MASK,
                    DataManager.getLanguage().getControlData().getButtonRight()));
            opt.addItem(new JComboItem<Integer>(MouseEvent.BUTTON2_DOWN_MASK,
                    DataManager.getLanguage().getControlData().getButtonMiddle()));
            opt.setSelectedIndex(ind);
        }
        if (na != null)
            na.setText(session.getNA());
        if (durationLabel != null)
            durationLabel.setText(session.getControlData().getDuration());
        aftermathLabel.setText(session.getControlData().getAftermath());
        if (xLabel != null)
            xLabel.setText(session.getControlData().getX());
        if (xClear != null)
            xClear.setText(session.getControlData().getXClear());
        if (xType != null) {
            if (data.getInDepthCursor().getX().getRight() == MouseCoordsType.ABS)
                xType.setText(session.getControlData().getAbs());
            else
                xType.setText(session.getControlData().getRel());
        }
        if (yLabel != null)
            yLabel.setText(session.getControlData().getY());
        if (yClear != null)
            yClear.setText(session.getControlData().getYClear());
        if (yType != null) {
            if (data.getInDepthCursor().getY().getRight() == MouseCoordsType.ABS)
                yType.setText(session.getControlData().getAbs());
            else
                yType.setText(session.getControlData().getRel());
        }
        if (finalXLabel != null)
            finalXLabel.setText(session.getControlData().getFinalX());
        if (finalXClear != null)
            finalXClear.setText(session.getControlData().getFinalXClear());
        if (finalXType != null) {
            if (data.getInDepthCursor().getFinalX().getRight() == MouseCoordsType.ABS)
                finalXType.setText(session.getControlData().getAbs());
            else
                finalXType.setText(session.getControlData().getRel());
        }
        if (finalYLabel != null)
            finalYLabel.setText(session.getControlData().getFinalY());
        if (finalYClear != null)
            finalYClear.setText(session.getControlData().getFinalYClear());
        if (finalYType != null) {
            if (data.getInDepthCursor().getFinalY().getRight() == MouseCoordsType.ABS)
                finalYType.setText(session.getControlData().getAbs());
            else
                finalYType.setText(session.getControlData().getRel());
        }
        if (cycle != null && cycleLabel != null)
            switch (cycle.getText()) {
                case ">":
                    cycleLabel.setText(session.getControlData().getInitialCoords());
                    break;
                case "<":
                    cycleLabel.setText(session.getControlData().getFinalCoords());
                    break;
                default:
                    cycleLabel.setText(session.getNA());
                    break;
            }
        indexLabel.setText(session.getControlData().getIndex());
        remove.setText(session.getControlData().getRemove());
    }

    @Handler
    public void updateTheme(ColorThemeUpdateEvent event) {
        if (event.getTheme() != null) {
            inputLabel.setForeground(event.getTheme().font());
            if (na != null)
                na.setForeground(event.getTheme().font());
            if (durationLabel != null)
                durationLabel.setForeground(event.getTheme().font());
            aftermathLabel.setForeground(event.getTheme().font());
            if (xLabel != null)
                xLabel.setForeground(event.getTheme().font());
            if (yLabel != null)
                yLabel.setForeground(event.getTheme().font());
            if (cycleLabel != null)
                cycleLabel.setForeground(event.getTheme().font());
            if (finalXLabel != null)
                finalXLabel.setForeground(event.getTheme().font());
            if (finalYLabel != null)
                finalYLabel.setForeground(event.getTheme().font());
            indexLabel.setForeground(event.getTheme().font());
        }
    }
}
