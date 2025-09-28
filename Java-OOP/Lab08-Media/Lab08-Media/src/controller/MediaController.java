package controller;

import media.Media;
import media.collection.MediaCollection;
import media.filters.Filter;

public class MediaController {
	private MediaCollection allMedias = null;

	public MediaController() {
		allMedias = new MediaCollection(100);
	}

	public boolean add(Media m) {
		if (allMedias.indexOf(m) >= 0)
			return false;

		allMedias.add(m);
		return true;
	}

	public boolean remove(Media media) {
		int position = allMedias.indexOf(media);
		if (position >= 0) {
			allMedias.remove(position);
			return true;
		}
		return false;
	}

	public MediaCollection getAll() {
		MediaCollection me = new MediaCollection(allMedias.size());
		for (int i = 0; i < allMedias.size(); i++) {
			me.add(allMedias.get(i));
		}

		return me;
	}

	public MediaCollection find(Filter f) {
		MediaCollection result = new MediaCollection();
		for (int i = 0; i < allMedias.size(); i++) {
			Media media = allMedias.get(i);
			if (f.filter(media)) {
				result.add(media);
			}
		}
		return result;
	}

}
