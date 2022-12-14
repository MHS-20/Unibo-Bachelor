package favoliere.ui;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;

import favoliere.ui.controller.Controller;
import favoliere.model.FasciaEta;
import favoliere.model.Favola;
import favoliere.model.Impressionabilita;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainPane extends BorderPane {
	
	private Controller controller = null;

	private TextArea output;
	private ComboBox<FasciaEta> fasceEta;
	private ComboBox<Impressionabilita> impressionabilita;
	private Button genera, stampa;
	
	public MainPane(Controller controller) {
		this.controller = controller;
		initPane();
	}

	private void initPane() {

		output = new TextArea();
		output.setEditable(false);
		output.setWrapText(true);

		VBox panel = new VBox(2);
		{
			panel.setSpacing(10);
			panel.setPadding(new Insets(0, 20, 10, 20));
			// ---------------------
			panel.getChildren().add(new Label("Fascia d'età del bambino: "));
			fasceEta = new ComboBox<>(FXCollections.observableArrayList(controller.getFasceEta()));
			fasceEta.setEditable(false);
			fasceEta.getSelectionModel().selectLast();
			panel.getChildren().add(fasceEta);
			// ---------------------
			panel.getChildren().add(new Label("Grado di impressionabilità: "));
			impressionabilita = new ComboBox<>(FXCollections.observableArrayList(controller.getLivelliImpressionabilita()));
			impressionabilita.setEditable(false);
			impressionabilita.getSelectionModel().selectFirst();
			panel.getChildren().add(impressionabilita);
			// ---------------------
			panel.getChildren().add(new Label("Favola generata: "));
			panel.getChildren().add(output);
			// ---------------------
			genera = new Button("Genera favola");
			genera.setAlignment(Pos.BASELINE_RIGHT);
			genera.setOnAction(this::genTale);
			panel.getChildren().add(genera);
			// ---------------------
			stampa = new Button("Stampa su file");
			stampa.setAlignment(Pos.BASELINE_RIGHT);
			stampa.setDisable(true);
			stampa.setOnAction(this::printToFile);
			panel.getChildren().add(stampa);
		}
		this.setCenter(panel);

	}

	private void printToFile(ActionEvent e) {
		try (PrintWriter pw = new PrintWriter(controller.getOutputFileName())) {
			if (output.getText().trim().isEmpty()) {
				Controller.alert("Attenzione", "Non c'è alcun testo da salvare", "Generare prima una favola");
				return;
			}
			pw.println(output.getText().trim());
			stampa.setDisable(true);
		} catch (FileNotFoundException e1) {
			Controller.alert("Errore", "Impossibile aprire il file " + controller.getOutputFileName() + " in scrittura", "Verificare nome e posizione");
		}
	}
	
	private void genTale(ActionEvent e) {
		Impressionabilita livelloImpressionabilita = impressionabilita.getValue();
		FasciaEta eta = fasceEta.getValue();
		Optional<Favola> favola = controller.generaFavola(eta, livelloImpressionabilita);
		output.setText(favola.isPresent() ? favola.get().toString() : "impossibile generare una favola coi vincoli richiesti");
		stampa.setDisable(false);
	}

}
