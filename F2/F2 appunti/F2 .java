Jar dopo run, prima opzione
Leggi prima i test 

GRAFICA: 
Una lista osservabile si ottiene incapsulando una normale collezione tramite la factory javafx.collections.FXCollections

Se hai problemi con la grafica controlla di non aver fatto le import di javaawt

Se un campo non c'è nel UML vuol dire che è dentro un metodo perché lo usa solo lui, oppure se due lo usano lo creano due volte.

TILE PANE ha due o tre colonne

- Sorgente evento
if (event.getSource()==b1) 

- Font
txtfield.setFont(Font.font("Arial", FontWeight.BOLD, 14));
output.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
rightLabel.setTextFill(Color.WHITE);
button or txtfield.setStyle("-fx-background-color: white;");

- Popolamento
cb.setItems(FXCollections.observableArrayList("Rosso","Giallo","Verde", "Blu"));

- Margini intorno alla label
VBox.setMargin(leftLabel, new Insets(10, 10, 10, 10));

Per far stare una cosa attaccata al margine anche quando si espande la finestra, devi usare una box e metterla a dx o sx del border pane.

- Allineamento è per le box
vbox.setAlignment(Pos.CENTER_LEFT);
Non serve se usi border pane.

- Box dentro box: 
Box.getChildren().addAll(leftBox,rightBox,button);
mettere 2vbox dentro ad una hbox le affianca in orizzontale, pensare la grafica in colonne non in righe.

- Border pane
BorderPane è sempre riempito da altri box
Gli altri box, alla creazione, puoi specificare lo spacing tra i figli

- Random color
Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r,g,b);

- ListView
listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> myHandler(newValue));

- PieChart
chart.setData(FXCollections.observableArrayList(new PieChart.Data("Accrediti", totaleAccrediti), new PieChart.Data("Addebiti", -totaleAddebiti)));

- Combo box 
comboIscritti.getItems().add(0,"NON ISCRITTO");
comboIscritti.setValue(comboIscritti.getItems().get(0));

aggiungo un elemento al primo posto

- Cose pronte
outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setPrefWidth(250);
		outputArea.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
		outputArea.setText("");
		this.setRight(outputArea);

this.outputArea.appendText

*****
PERSISTENZA: 
- Meglio: split/substring + tokenizer (prima pezzi grossi poi pezzi piccoli)
- Con substring nella linea, usando indexof per trovare i vertici e tagliare

if (rdr == null)
			throw new IllegalArgumentException("Reader nullo");
BufferedReader reader = new BufferedReader(rdr);
String line, items[];
items = line.split("\\s+");
while((line = reader.readLine()) != null)

- Split con delimitatori variabili 
line.split("/|="); ( uno o l'altro, sono in or)
"\\t+" per dire uno o più tabulazioni
String[] items = line.split("\s+");
String[] items = line.split("=");

La split elimina il separatore

- Skipping empty
if (riga.isEmpty())
continue;

- Split check !		
1) if (items.length!=3) throw new BadFileFormatException("Some comma is missing in line - " + line);

Ti permette di controllare che ci sia il giusto numero di separatori e che ci sia il /n alla fine della riga

2) if (items[0]==null || items[0].isEmpty()) throw new BadFileFormatException("Train name must not be void - " + items[0]);
Per ogni indice se non sono troppi, oppure catture l'eccezione IndexOutOfBoundsException

- Enum
Il value of lancia IllegalArgumentException
Il value of va è case sensitive, serve che siano proprio uguali 
Nella mappe, le chiave sono case sensitive

- Parse 
NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ITALY);
per fare il parse double con il locale

try{
Integer.parseInt();
}catch(NumberFormatException e){
throw new BadFileFormatException("Train name must not be void - " + items[0]);
}

- Eccezioni
IllegalArgumentException
IndexOutOfBoundsException
DateTimeParseException
NoSuchElementException
NumberFormatException
ParseException

*******
ALTRO
String.format("%d:%02d", minutes / 60, (minutes % 60));
Formattare con solo due cifre

%-s for left-justified (tutto quello che viene dopo)
%s per qualunque tipo
%4s prints out the string, left-padded by spaces until the total length is 4

%-60s  CFU: %2s  Data:%10s   Voto: %-4s

if (line.isBlank()) continue;
isBlank è funzione della classe string 
continue è un comando dei cicli

- Dimensione lista 
int lastPosIndex  = tracks.size()-1;

****
CELLA in una MATRICE
board[k / DIM][k % DIM]

*********
LAMBDA

* SOLUZIONE 1: SPEZZARE I PASSAGGI*/
		Comparator<Airport> comparator = Comparator.comparing(a -> a.getCity().getName());
		Comparator<Airport> completeComparator = comparator.thenComparing(aa -> aa.getName());
		sortedAirports.sort(completeComparator);
		
		/* SOLUZIONE 2: CAST*/
		//sortedAirports.sort(Comparator.comparing((Airport a) -> a.getCity().getName()).thenComparing(aa -> aa.getName()));

		
		/* SE AVESSI SOLO UNA CONDIZIONE CE LA FAREBBE -------- NON E' IL NOSTRO CASO*/
		//sortedAirports.sort(Comparator.comparing((a) -> a.getCity().getName()));
		
		/* METHOD REFERENCE se avessi solo un metodo da chiamare e non oggetti innestati -------- NON E' IL NOSTRO CASO*/
		//NON PASSA sortedAirports.sort(Comparator.comparing(a -> a.getCode()).thenComparing(aa -> aa.getName()));
		//sortedAirports.sort(Comparator.comparing(Airport::getCode).thenComparing(aa -> aa.getName()));

******
CONDIZIONALE 

- Forma innestata 
int indexLoc = pref.getCitta().equalsIgnoreCase(q.getCitta()) ? 100 : pref.getProvincia().equalsIgnoreCase(q.getProvincia())  ? 90 : pref.getRegione().equalsIgnoreCase(q.getRegione()) ? 60 : 40;

***********
JAVA TIME
FormatStyle.SHORT, Locale.ITALY

DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
			.withLocale(Locale.ITALY);

- Parse
dataContabile = LocalDate.parse(items[0], dateFormatter);
DateTimeFormatter.ISO_LOCAL_DATE (per il formato iso aaa-mm-gg)

- Durata tra offset
public Duration getFlightDuration() {

		OffsetDateTime departure = OffsetDateTime.of(LocalDate.now(), departureLocalTime,
				ZoneOffset.ofHours(getDepartureAirport().getCity().getTimeZone()));

		OffsetDateTime arrival = OffsetDateTime.of(LocalDate.now().plusDays(getDayOffset()), arrivalLocalTime,
				ZoneOffset.ofHours(getArrivalAirport().getCity().getTimeZone()));
		
		if(Duration.between(departure, arrival).toNanos() < 0)
			return Duration.between(departure, arrival.plusDays(1));
		else 
			return Duration.between(departure, arrival); 
	}

- Durata tra zoned
ZonedDateTime basta fare la differenza perché sono assoluti

**********
RANDOM
Random rnd = new Random();
IntStream st = rnd.ints(1,100).limit(500);

Random r = new Random();
double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

- Random int con un max:
int max=list.size();
		Random rndGen = new Random();
		return list.get(rndGen.nextInt(max));

*****
FILE: 
- Aprire file new FileReader("Ghigliottine.txt")
- scrittura su file con printwriter

Salvare su file 
PrintWriter pr;

		try {
			pr = new PrintWriter(new FileWriter("Soluzione.txt"));
			pr.write(sudoku.toString());
			pr.flush();
			pr.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

******************
ESEMPIO DI MAIN
public static void main(String args[]) throws IOException, BadFileFormatException {
VotiReader vReader = new MyVotiReader(new BufferedReader(new FileReader("Voti.txt")));
System.out.println(vReader.getElezioni());
	}

*********
SWITCH:
switch (settore) {
			case "amore":
				amore = strategy.seleziona(super.getRepository().getPrevisioni(settore), segno);
				break;
			case "lavoro":
				lavoro = strategy.seleziona(super.getRepository().getPrevisioni(settore), segno);
				break;
			case "salute":
				salute = strategy.seleziona(super.getRepository().getPrevisioni(settore), segno);
				break;
			} 

**********
CONSIGLI: 
Nei cicli della persistenza metti solo le parti che si ripetono, quelli che ci sono una volta sola le metti fuori, il ciclo esce quando le raggiunge.

Guardare i test della persistenza prima di mettersi a farla.
Estrai quello che ti serve invece di sdoppiare il codice.

Invece di fare tanti if puoi fare usare le forme condizionali innestate

Delle text area gestisci sempre le dimensioni ed il font

// ----- top box -----
@Override