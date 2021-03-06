package de.wolfi.minopoly.components;

import java.util.ArrayList;
import java.util.HashMap;

import de.wolfi.minopoly.components.fields.Field;
import de.wolfi.minopoly.components.fields.FieldColor;
import de.wolfi.minopoly.components.fields.JailField;
import de.wolfi.minopoly.components.fields.StartField;

public class FieldManager extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6097015591168469852L;

	private final ArrayList<Field> fields = new ArrayList<>();
	private final HashMap<FieldColor, ArrayList<Field>> mappedList = new HashMap<>();

	protected FieldManager() {
	}

	public Field addField(Field f) {
		f.spawn();
		this.fields.add(f);
		this.addMapped(f.getColor(), f);
		return f;
	}

	private void addMapped(FieldColor color, Field f) {
		ArrayList<Field> list = this.mappedList.get(color);
		if (list == null)
			list = new ArrayList<>();
		if (!list.contains(f))
			list.add(f);
	}

	public Field getNextField(Field from) {
		boolean next = false;
		for (final Field f : this.fields) {
			if (f.equals(from)) {
				next = true;
				continue;
			}
			if (next)
				return f;
		}

		return this.fields.get(0);
	}

	@Override
	protected void load() {
		for (final Field f : this.fields)
			f.load();
	}

	public int countProperties(Player player, FieldColor color) {
		int count = 0;
		for (Field f : mappedList.get(color)) {
			if (f.isOwnedBy(player))
				count++;
		}
		return count;
	}

	@Override
	protected void unload() {
		for (final Field f : this.fields)
			f.unload();

	}

	public Field getFieldByType(Field start, Class<? extends Field> type) {
		boolean found = false;
		if (start == null)
			found = true;
		Field next = null;
		for (final Field f : this.fields) {
			if (f.getClass() == type) {
				next = f;
				found = true;
				break;
			}
		}
		while(found == false){
			next = getNextField(next);
			if(next.getClass() == type) found = true;
		}
		
		return next;
	}

	public Field getStartField() {
		return this.getFieldByType(null, StartField.class);
	}
	
	public Field getJailField(){
		return this.getFieldByType(null, JailField.class);
	}

	public boolean hasAll(Player player, FieldColor color) {
		return this.countProperties(player, color) >= this.mappedList.get(color).size();
	}
}
