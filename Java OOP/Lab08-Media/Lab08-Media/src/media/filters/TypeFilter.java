package media.filters;

import media.Media;
import media.Type;

public class TypeFilter implements Filter {
	private Type typeToFind;

	public TypeFilter(Type typeToFind) {
		this.typeToFind = typeToFind;
	}

	public void setType(Type typeToFind) {
		this.typeToFind = typeToFind;
	}
 // versione prima di java 15
	/*@Override
	 * public boolean filter(Media media) { return media instanceof HasType &&
	 * ((HasType)media).getType().equals(this.typeToFind); }
	 */

	// versione dopo Java 15
	@Override
	public boolean filter(Media media) {
		if  (media instanceof HasType ht) {
				return ht.getType().equals(this.typeToFind);
		}
		return false;
	}

}
