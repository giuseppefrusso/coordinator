package it.unisa.diem.coordinator.dummy;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

public class TwoKeyMap<K1, K2, V> {
	
	private Map<K1, V> map1;
	private Map<K2, V> map2;
	
	public TwoKeyMap() {
		map1 = new HashMap<>();
		map2 = new TreeMap<>();
	}
	
	/**
	 * If both two maps contains already the value, returns the value itself.
	 * If neither of them contains already the value, returns null.
	 * If one of them contains already the value, returns the value itself. 
	 * @param key1
	 * @param key2
	 * @param value
	 * @return value or null
	 */
	public V put(K1 key1, K2 key2, V value) {
		map1.put(key1, value);
		return map2.put(key2, value);		
	}
	
	public V getByKey1(K1 key1) {
		return map1.get(key1);
	}
	
	public V getByKey2(K2 key2) {
		return map2.get(key2);
	}
	
	/**
	 * The natural order is based on the second key.
	 * @return
	 */
	public TwoKeyEntry pollLastEntry() {
		return null;
	}
	
	public class TwoKeyEntry {
		private K1 key1;
		private K2 key2;
		private V value;
		
		public TwoKeyEntry(K1 key1, K2 key2, V value) {
			this.key1 = key1;
			this.key2 = key2;
			this.value = value;
		}
		
		public K1 getKey1() {
			return key1;
		}
		
		public K2 getKey2() {
			return key2;
		}
		
		public V getValue() {
			return value;
		}
	}

}
