package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.merge_playlists.MergeController;
import interface_adapter.merge_playlists.MergeViewModel;
import use_case.merge_playlists.MergeInputData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MergeView extends JPanel implements ActionListener, PropertyChangeListener {
    public final static String viewName = "Merge View";
    private final MergeViewModel mergeViewModel;
    private final ViewManagerModel viewManagerModel;

    private final MergeController mergeController;

    private JScrollPane playlistScrollPane;
    private String selectedPlaylistName;

    private DefaultListModel<String> playlistsModel;
    private JList<String> playlistsList;
    private DefaultListModel<String> selectedPlaylistsModel;
    private JList<String> selectedPlaylistsList;


    private final JButton getPlaylistButton;

    private final JButton mergeButton;

    private final JButton homeButton;
    private final JButton refreshButton;
    private final JButton clearPlaylistsButton;
    private final JButton deletePlaylistButton;

    // RadioButtons for the filters, along with the group that they belong in

    private final ButtonGroup instrumentalButtonGroup;
    private final JRadioButton instrumentalRadioButton;
    private final JRadioButton vocalRadioButton;
    private final JRadioButton noneInstrumentalRadioButton;


    private final ButtonGroup valenceButtonGroup;
    private final JRadioButton noneValenceRadioButton;
    private final JRadioButton sadValenceRadioButton;
    private final JRadioButton neutralValenceRadioButton;
    private final JRadioButton happyValenceRadioButton;


    private final ButtonGroup tempoButtonGroup;
    private final JRadioButton noneTempoRadioButton;
//    private final JRadioButton verySlowTempoRadioButton;
    private final JRadioButton slowTempoRadioButton;
    private final JRadioButton normalTempoRadioButton;
    private final JRadioButton fastTempoRadioButton;
//    private final JRadioButton veryFastTempoRadioButton; // uncomment to add very slow and very fast buttons

    public MergeView(MergeViewModel mergeViewModel, MergeController mergeController, ViewManagerModel viewManagerModel) {

        this.mergeViewModel = mergeViewModel;
        this.viewManagerModel = viewManagerModel;
        this.mergeController = mergeController;

        viewManagerModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(mergeViewModel.VIEW_TITLE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel actionsPanel = new JPanel();
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        // Creating Buttons:
        getPlaylistButton = new JButton(mergeViewModel.GET_PLAYLISTS_LABEL);

        mergeButton = new JButton(mergeViewModel.MERGE_BUTTON_LABEL);
        homeButton = new JButton(mergeViewModel.HOME_BUTTON_LABEL);

        refreshButton = new JButton(mergeViewModel.REFRESH_BUTTON_LABEL);

        clearPlaylistsButton = new JButton("Clear Selection");
        deletePlaylistButton = new JButton("Delete Playlist");

        // Creating radio buttons and their groups
        // Instrumental:
        instrumentalButtonGroup = new ButtonGroup();
        instrumentalRadioButton = new JRadioButton(mergeViewModel.INSTRUMENTAL_BUTTON_LABEL);
        vocalRadioButton = new JRadioButton(mergeViewModel.VOCAL_BUTTON_LABEL);
        noneInstrumentalRadioButton = new JRadioButton(mergeViewModel.NONE_LABEL, true);

        instrumentalButtonGroup.add(noneInstrumentalRadioButton);
        instrumentalButtonGroup.add(instrumentalRadioButton);
        instrumentalButtonGroup.add(vocalRadioButton);

        // Valence/Mood
        valenceButtonGroup = new ButtonGroup();
        noneValenceRadioButton = new JRadioButton(mergeViewModel.NONE_LABEL, true);
        sadValenceRadioButton = new JRadioButton(mergeViewModel.SAD_VALENCE_BUTTON_LABEL);
        neutralValenceRadioButton = new JRadioButton(mergeViewModel.NEUTRAL_VALENCE_BUTTON_LABEL);
        happyValenceRadioButton = new JRadioButton(mergeViewModel.HAPPY_VALENCE_BUTTON_LABEL);

        valenceButtonGroup.add(noneValenceRadioButton);
        valenceButtonGroup.add(sadValenceRadioButton);
        valenceButtonGroup.add(neutralValenceRadioButton);
        valenceButtonGroup.add(happyValenceRadioButton);
        
        // Tempo
        tempoButtonGroup = new ButtonGroup();
        noneTempoRadioButton = new JRadioButton(mergeViewModel.NONE_LABEL, true);
//        verySlowTempoRadioButton = new JRadioButton(mergeViewModel.VERY_SLOW_TEMPO_BUTTON_LABEL);
        slowTempoRadioButton = new JRadioButton(mergeViewModel.SLOW_TEMPO_BUTTON_LABEL);
        normalTempoRadioButton = new JRadioButton(mergeViewModel.NORMAL_TEMPO_BUTTON_LABEL);
        fastTempoRadioButton = new JRadioButton(mergeViewModel.FAST_TEMPO_BUTTON_LABEL);
//        veryFastTempoRadioButton = new JRadioButton(mergeViewModel.VERY_FAST_TEMPO_BUTTON_LABEL);
        
        tempoButtonGroup.add(noneTempoRadioButton);
//        tempoButtonGroup.add(verySlowTempoRadioButton);
        tempoButtonGroup.add(slowTempoRadioButton);
        tempoButtonGroup.add(normalTempoRadioButton);
        tempoButtonGroup.add(fastTempoRadioButton);
//        tempoButtonGroup.add(veryFastTempoRadioButton);

        
        // Making the Action Panel
        JPanel buttonsActionPanel = new JPanel();
        buttonsActionPanel.setLayout(new BoxLayout(buttonsActionPanel, BoxLayout.Y_AXIS));
        // The buttons in the ActionPanel
        buttonsActionPanel.add(refreshButton);
        buttonsActionPanel.add(mergeButton);

        actionsPanel.add(buttonsActionPanel);

        // PANELS: ----------------------------------------------------------------
        // Panel storing the filters
        JPanel filtersPanel = new JPanel();
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        filtersPanel.setLayout(new GridLayout(3, 1));

        // Instrumental filter
        JPanel instrumentalPanel = new JPanel();
        instrumentalPanel.add(noneInstrumentalRadioButton);
        instrumentalPanel.add(instrumentalRadioButton);
        instrumentalPanel.add(vocalRadioButton);
        instrumentalPanel.setBorder(BorderFactory.createTitledBorder("Instrumentalness"));
        filtersPanel.add(instrumentalPanel);

        // Valence/Mood filter
        JPanel valencePanel = new JPanel();
        valencePanel.add(noneValenceRadioButton);
        valencePanel.add(sadValenceRadioButton);
        valencePanel.add(neutralValenceRadioButton);
        valencePanel.add(happyValenceRadioButton);
        valencePanel.setBorder(BorderFactory.createTitledBorder("Valence/Mood"));
        filtersPanel.add(valencePanel);

        // Tempo filter
        JPanel tempoPanel = new JPanel();
        tempoPanel.add(noneTempoRadioButton);
//        tempoPanel.add(verySlowTempoRadioButton);
        tempoPanel.add(slowTempoRadioButton);
        tempoPanel.add(normalTempoRadioButton);
        tempoPanel.add(fastTempoRadioButton);
//        tempoPanel.add(veryFastTempoRadioButton);
        tempoPanel.setBorder(BorderFactory.createTitledBorder("Tempo"));
        filtersPanel.add(tempoPanel);

        actionsPanel.add(filtersPanel);

        // SCROLL PANES: ---------------------------
        playlistsModel = new DefaultListModel<>();
        playlistsList = new JList<>(playlistsModel);
        JScrollPane playlistsScrollPane = new JScrollPane(playlistsList);
        playlistsScrollPane.setBorder(BorderFactory.createTitledBorder("Your Playlists"));

        selectedPlaylistsModel = new DefaultListModel<>();
        selectedPlaylistsList = new JList<>(selectedPlaylistsModel);
        JScrollPane selectedScrollPane = new JScrollPane(selectedPlaylistsList);
        selectedScrollPane.setBorder(BorderFactory.createTitledBorder("Selected Playlists"));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(title)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                        GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE) // places this on the right
                                .addComponent(homeButton)
                        )
                        .addComponent(actionsPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(playlistsScrollPane)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(selectedScrollPane)
                                        .addComponent(clearPlaylistsButton)
                                        .addComponent(deletePlaylistButton)
                                )
                        )
        );


        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(title)
                                .addComponent(homeButton)
                        )
                        .addComponent(actionsPanel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(playlistsScrollPane)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(selectedScrollPane)
                                        .addComponent(clearPlaylistsButton)
                                        .addComponent(deletePlaylistButton)
                                )
                        )
        );


        mergeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(mergeButton)) {
                            mergePlaylists();
                            refresh();
                        }
                    }
                }
        );

        homeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(homeButton)) {
                            mergeController.returnHome();
                        }
                    }
                }
        );

        refreshButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(refreshButton)) {
                            refresh();
                        }
                    }
                }
        );

        playlistsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPlaylist = playlistsList.getSelectedValue();
                if (selectedPlaylist != null && !selectedPlaylistsModel.contains(selectedPlaylist)) {
                    selectedPlaylistsModel.addElement(selectedPlaylist);
                }
            }
        });

        clearPlaylistsButton.addActionListener(e -> {
            if (e.getSource().equals(clearPlaylistsButton)) {
                selectedPlaylistsModel.clear();
            }
        });

        deletePlaylistButton.addActionListener(e -> {
            if (e.getSource().equals(deletePlaylistButton)) {
                deleteSelectedPlaylist();
            }
        });
//

    }

    private void refresh() {
        selectedPlaylistsModel.clear();
        List<String> playlistNames = mergeController.getPlaylists();
        displayPlaylists(playlistNames);
    }

    private void displayPlaylists(List<String> playlistNames) {
        playlistsModel.clear();
        for (String playlistName : playlistNames) {
            playlistsModel.addElement(playlistName);
        }
    }

    private void mergePlaylists() {
        List<String> selectedPlaylists = new ArrayList<>();
        for (int i = 0; i < selectedPlaylistsModel.getSize(); i++) {
            selectedPlaylists.add(selectedPlaylistsModel.getElementAt(i));
        }

        if (selectedPlaylists.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one playlist to merge.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String givenName = JOptionPane.showInputDialog(this, "Enter the name of the new playlist: ");

        if (givenName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a non-empty name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            selectedPlaylistsModel.clear();
        }

        // This function gets the options for filters and puts it into mergeInputData
        MergeInputData mergeInputData = createMergeInputData(selectedPlaylists, givenName);

        mergeController.mergePlaylists(mergeInputData);

    }

    private MergeInputData createMergeInputData(List<String> selectedPlaylists, String givenName) {
        // get the options selected from the filters
        int instrumentalChoice = MergeInputData.ANY;
        if (instrumentalRadioButton.isSelected()) instrumentalChoice = MergeInputData.INSTRUMENTAL_CHOICE;
        if (vocalRadioButton.isSelected()) instrumentalChoice = MergeInputData.VOCAL_CHOICE;

        int valenceChoice = MergeInputData.ANY;
        if (sadValenceRadioButton.isSelected()) valenceChoice = MergeInputData.SAD_CHOICE;
        if (neutralValenceRadioButton.isSelected()) valenceChoice = MergeInputData.NEUTRAL_CHOICE;
        if (happyValenceRadioButton.isSelected()) valenceChoice = MergeInputData.HAPPY_CHOICE;

        int tempoChoice = MergeInputData.ANY;
        if (slowTempoRadioButton.isSelected()) tempoChoice = MergeInputData.SLOW_CHOICE;
        if (normalTempoRadioButton.isSelected()) tempoChoice = MergeInputData.NORMAL_CHOICE;
        if (fastTempoRadioButton.isSelected()) tempoChoice = MergeInputData.FAST_CHOICE;

        MergeInputData mergeInputData = new MergeInputData(selectedPlaylists, givenName, false);
        mergeInputData.setInstrumentalChoice(instrumentalChoice);
        mergeInputData.setValenceChoice(valenceChoice);
        mergeInputData.setTempoChoice(tempoChoice);
        return mergeInputData;
    }

    private void deleteSelectedPlaylist() {
        int selectedIndex = selectedPlaylistsList.getSelectedIndex();
        if (selectedIndex != -1) {
            selectedPlaylistsModel.remove(selectedIndex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update displayed list for the user if this event is state (i.e. playlists changed)
        if (evt.getNewValue().equals("Merge View")) {
            // This will only be reached when being called when HomeInteractor calls the presenter to switch to this page
            // Call this to initially gather the user's playlists (same as the refresh button)
            refresh();
        }

    }
}
