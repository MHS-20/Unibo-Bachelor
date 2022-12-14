package flightTracker.ui;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import flightTracker.model.Flight;
import flightTracker.persistence.BadFileFormatException;
import flightTracker.ui.controller.Controller;
import flightTracker.ui.controller.MyController;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class MainPane extends BorderPane {

	private Controller controller;
	private ComboBox<String> flightCombo;
	private CheckBox checkbox;
	private GeoCanvas canvas;

	public MainPane(GeoMap map, Controller controller) {
		this.controller = controller;
		// --- load background map
		canvas = new GeoCanvas(map);
		this.setCenter(canvas);
		// --- load combo flights
		flightCombo = new ComboBox<>();
		flightCombo.setItems(this.controller.getFlights());
		flightCombo.setTooltip(new Tooltip("Scegliere il volo da graficare"));
		//
		HBox rigaSuperiore = new HBox();
		rigaSuperiore.setSpacing(10);
		rigaSuperiore.getChildren().add(new Label("Voli disponibili"));
		rigaSuperiore.getChildren().add(flightCombo);
		checkbox = new CheckBox("Voli multipli");
		rigaSuperiore.getChildren().add(checkbox);
		//
		this.setTop(rigaSuperiore);
		flightCombo.setOnAction(this::myHandler);
		// --- plot reference parallels
		canvas.drawParallels(List.of(45));
	}
	
	private void plotFlight(Flight flight) {
		if (checkbox.isSelected()) {
			double r,g,b;
			Random rnd = new Random();
			r = rnd.nextDouble(); g = rnd.nextDouble(); b = rnd.nextDouble();
			canvas.setDrawingColor(Color.color(r, g, b));
		}
		else {
			canvas.redrawMap();
		}
		canvas.drawPoints(this.controller.getPoints(flight));
	}
	
	public void myHandler(ActionEvent event) {
		String flightName = flightCombo.getValue();
		try {
			Flight flight = controller.load(flightName,new FileReader(flightName));
			plotFlight(flight);		}
		catch (IOException e) {
			MyController.alert("Errore", "Errore di I/O nella lettura del file " + flightName, "Addio");
			//System.exit(1);
		} catch (BadFileFormatException e) {
			MyController.alert("Errore", "Errore nel formato del file " + flightName, "Addio");
			//System.exit(1);
		}
	}
}
