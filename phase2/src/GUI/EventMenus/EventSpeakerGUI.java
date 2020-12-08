package GUI.EventMenus;

import Controller.EventManagementSystem;
import Controller.LoginSystem;
import GUI.Main.PanelStack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EventSpeakerGUI {
    private PanelStack panelStack;
    private EventManagementSystem eventSystem;
    private EventPanelBuilder panelBuilder = new EventPanelBuilder();

    private DefaultListModel<String> listModel = new DefaultListModel<>();

    private List<String> eventList;

    private JPanel eventPanel = new JPanel();
    private JPanel jListPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private JLabel eventsJLabel = new JLabel("EVENTS MENU");

    private JList eventsJList = new JList(); //TODO pass in list of events of speakers in parameter of JList

    private JScrollPane eventsJScrollPane = new JScrollPane();
    private JButton backButton = new JButton("Back");
    private JButton broadcastButton = new JButton("✉");


    private int selectedEventIndex;
    private JLabel noEventLabel = new JLabel("You are not speaking at any events.");

    /**
     * Creates an EventSpeakerGUI and initializes its EventManagementSystem and the panelStack
     *
     * @param eventSystem the EventManagementSystem that the AddEventGUI communicates with
     * @param panelStack the stack containing all the panels that have been loaded
     */
    public EventSpeakerGUI(EventManagementSystem eventSystem, PanelStack panelStack) {
        this.eventSystem = eventSystem;
        this.panelStack = panelStack;
    }

    /**
     * Builds and loads the Event Page for Speakers
     *
     * @return The Event panel for Speakers
     */
    public JPanel startEventPage() {
        panelBuilder.buildBorderLayoutPanel(eventPanel, 20, 20, 40, 20);
        eventsJLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 48));
        eventPanel.add(eventsJLabel, BorderLayout.NORTH);
        buildListModel();
        eventsJList = new JList(listModel);
        panelBuilder.buildAttendeeEventsJListPanel(jListPanel, eventsJList, eventsJScrollPane);
        eventPanel.add(jListPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new BorderLayout());
        eventPanel.add(buttonPanel, BorderLayout.SOUTH);
        broadcastButton.setEnabled(false);
        broadcastButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 24));
        buttonPanel.add(broadcastButton, BorderLayout.NORTH);
        backButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 14));
        buttonPanel.add(backButton, BorderLayout.WEST);
        listListener();
        broadcastButtonListen();
        backButtonListen();
        return eventPanel;
    }

    private void buildListModel(){
        this.eventList = eventSystem.getSpeakerEventList();
        if (!eventList.isEmpty()){
            for (String event:eventList) {
                listModel.addElement(event);
            }
        }
    }

    private void backButtonListen(){
        backButton.addActionListener(e -> {
            listModel.clear();
            panelStack.pop();
            JPanel panel = (JPanel) panelStack.pop();
            panelStack.loadPanel(panel);
        });
    }

    private void broadcastButtonListen(){
        broadcastButton.addActionListener(e -> {
            if(!eventsJList.isSelectionEmpty()){
                String message = JOptionPane.showInputDialog("Enter the content of your message: ");
                if (message != null) {
                    String event = eventsJList.getSelectedValue().toString();
                    eventSystem.broadcast(Integer.parseInt(event.substring(0, 1)), message);
                }
            }
        });
    }

    private void listListener(){
        eventsJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                selectedEventIndex = eventsJList.getSelectedIndex();
                broadcastButton.setEnabled(true);
            }
        });
    }

}
