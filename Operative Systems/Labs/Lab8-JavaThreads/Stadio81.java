package stadiocalcio;
	
	public class Stadio {
		private int occupati, tifosi1, tifosi2, tifosi3;
		private int ragazzi, adulti, anziani;
		private String squadra1, squadra2, squadra3;
		private int MAX;
		
		public Stadio(String[] squadre, int MAX){
			this.squadra1= squadre[0];
			this.squadra2= squadre[1];
			this.squadra3= squadre[2];

			this.MAX = MAX;
			this.occupati = 0;
			occupati = 0; 
			tifosi1 = 0; 
			tifosi2 = 0; 
			tifosi3 = 0;
			
			ragazzi = 0; 
			adulti = 0; 
			anziani = 0;
			}
		
		public synchronized boolean entra(String squadra, int eta){
			if(occupati == MAX)
				return false;
			else{
				occupati++;
				
				if(squadra.equals(squadra1))
					tifosi1++;
				
				if(squadra.equals(squadra2))
					tifosi2++;
				
				if(squadra.equals(squadra3))
					tifosi3++;
				
				if(eta <= 13)
					ragazzi++;
				
				if(eta > 13 && eta <= 65)
					adulti++;
				
				if(eta > 65)
					anziani++;
				
				return true; 
			}
		}
		
		public synchronized void esci(){
			occupati--;
		}
		
		public synchronized void stampa (){
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("Numero tifosi per squadra : ");
			sb.append(squadra1);
			sb.append(" è: ");
			sb.append(tifosi1);
			sb.append("\n");
			
			sb.append("Numero tifosi per squadra : ");
			sb.append(squadra2);
			sb.append(" è: ");
			sb.append(tifosi2);
			sb.append("\n");
			
			sb.append("Numero tifosi per squadra : ");
			sb.append(squadra3);
			sb.append(" è: ");
			sb.append(tifosi3);
			sb.append("\n");
			
			sb.append("Ragazzi, adulti, anziani:");
			sb.append(" ");
			sb.append(ragazzi); 		
			sb.append(" ");
			sb.append(adulti);
			sb.append(" ");
			sb.append(anziani);
			
			System.out.println(sb.toString());
		}
}
