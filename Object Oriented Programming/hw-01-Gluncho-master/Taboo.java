
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private Map<T, Set<T> > noFollowMap;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		noFollowMap = new HashMap<T, Set<T> >();
		for(int i = 0; i < rules.size() - 1; i++){
			T curr = rules.get(i);
			if(curr == null || rules.get(i+1)==null) continue;
			if(noFollowMap.containsKey(curr) == false) noFollowMap.put(curr, new HashSet<T>());
			noFollowMap.get(curr).add(rules.get(i + 1));
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		return noFollowMap.containsKey(elem) ? noFollowMap.get(elem) : new HashSet<T>();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		for(int i = 1; i < list.size(); i++){
			T cur = list.get(i - 1);
			T next = list.get(i);
			if(violatesRules(cur, next)) {
				list.remove(i);
				i--;
			}
		}
	}

	private boolean violatesRules(T curr, T followed){
		if(curr == null || followed == null) return false;
		return noFollowMap.containsKey(curr) && noFollowMap.get(curr).contains(followed);
	}
}
