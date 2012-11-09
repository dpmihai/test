package misc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class MapTest {
	
	public static void main(String[] args) {
		
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("3", new IdName(new Double(4), "exit"));
		map1.put("2", "Str");
		map1.put("1", new Integer[] {1, 2, 3} );
		
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("1", new Short[] {1, 3, 2} );
		map2.put("2", "Str");
		map2.put("3", new IdName(new Double(4), "exitModified"));
				
		//System.out.println(equals(map1, map2));

		String s1 = toString(map1);
		String s2 = toString(map2);
		System.out.println("s1="+s1 + "  hash="+s1.hashCode());
		System.out.println("s2="+s2 + "  hash="+s2.hashCode());
		System.out.println("eq="+s1.equals(s2));
	}
	
	public static boolean equals(HashMap<String, Object> map1, HashMap<String, Object> map2) {
		if (!map1.keySet().equals(map2.keySet())) {
			return false;
		}
		
		for (String key : map1.keySet()) {
			Object o1 = map1.get(key);
			Object o2 = map2.get(key);
			
			if (o1 == null) {
				if (o2 == null) {
					continue;
				} else {
					return false;
				}
			} else if (o2 == null) {
				return false;
			}
			
			if (o1.getClass().isArray()) {
				if (o2.getClass().isArray()) {
					Object[] array1 = (Object[])o1;
					Object[] array2 = (Object[])o2;					
					if (array1.length != array2.length) {
						return false;
					}
					String[] s1 = new String[array1.length];
					String[] s2 = new String[array2.length];
					for (int i=0; i<array1.length; i++) {
						if (array1[i] == null) {
							s1[i] = "";
						} else {
							s1[i] = array1[i].toString();
						}												
					}
					Arrays.sort(s1);
					for (int i=0; i<array2.length; i++) {
						if (array2[i] == null) {
							s2[i] = "";
						} else {
							s2[i] = array2[i].toString();
						}												
					}
					Arrays.sort(s2);
										
					System.out.println("eq="+Arrays.equals(s1, s2));
					if (Arrays.equals(s1, s2)){
						continue;
					} else {
						return false;
					}
					
				} else {
					return false;
				}
			} else if (o2.getClass().isArray()) {
				return false;
			}
			
			if (o1 instanceof IdName) {
				if (o2 instanceof IdName)  {
					if ( ((IdName)o1).getId().equals(((IdName)o2).getId())) {
						continue;
					} else {
						return false;
					}
				}
			} else if (o2 instanceof IdName) {
				return false;
			}
			
			if (!o1.equals(o2)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String toString(HashMap<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		if (map != null) {			
			TreeSet<String> keys = new TreeSet<String>(map.keySet());
			for (String key : keys) {
				Object o = map.get(key);
				if (o != null) {
					if (o.getClass().isArray()) {
						Object[] array = (Object[])o;
						if (array.length > 0) {
							int size = array.length;
							String[] s = new String[size];
							for (int i=0; i<size; i++) {
								if (array[i] == null) {
									s[i] = "";
								} else {
									s[i] = array[i].toString();
								}												
							}
							Arrays.sort(s);
							for (int i=0; i<size; i++) {
								sb.append(s[i]);
							}
						}
					} else if (o instanceof IdName) {
						sb.append(((IdName) o).getId());
					} else {
						sb.append(o.toString());
					}					
				}
			}

		}
		
		return sb.toString();
	}
	
	public static class IdName implements Serializable {

	    private static final long serialVersionUID = -5066657215056829886L;

	    private Serializable id;
	    private Serializable name;	   	    

	    public IdName(Serializable id, Serializable name) {
			super();
			this.id = id;
			this.name = name;
		}

		/** Get Id
	     *
	     * @return Id
	     */
	    public Serializable getId() {
	        return id;
	    }

	    /** Set Id
	     *
	     * @param id id
	     */
	    public void setId(Serializable id) {
	        this.id = id;
	    }

	    /** Get Name
	     *
	     * @return name
	     */
	    public Serializable getName() {
	        return name;
	    }

	    /** Set name
	     *
	     * @param name name
	     */
	    public void setName(Serializable name) {
	        this.name = name;
	    }

	    /** Equals
	     *
	     * @param o idname object
	     * @return true if current idname object equals parameter idname object, false otherwise
	     */
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof IdName)) return false;

	        IdName idName = (IdName) o;

	        if (id != null ? !id.equals(idName.id) : idName.id != null) return false;
	        if (name != null ? !name.equals(idName.name) : idName.name != null) return false;

	        return true;
	    }

	    /** Hash code value for this idname object
	     *
	     * @return a hash code value for this idname object
	     */
	    public int hashCode() {
	        int result;
	        result = (id != null ? id.hashCode() : 0);
	        result = 31 * result + (name != null ? name.hashCode() : 0);
	        return result;
	    }

	    /** Tostring method
	     *
	     * @return current object as a string
	     */
	    public String toString() {
	        if (name !=  null) {
	            return name.toString();
	        } else {
	            return id.toString();
	        }
	    }
	}

}
