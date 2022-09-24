// TabooTest.java
// Taboo class tests -- nothing provided.

import junit.framework.TestCase;

import java.util.*;

public class TabooTest extends TestCase {

    private <T> Set<T> asSet(T... elems){
        Set<T> ans = new HashSet<>();
        for(T elem : elems){
            ans.add(elem);
        }
        return ans;
    }
    public void testNoFollow1(){

        Taboo<String> t = new Taboo<>(Arrays.asList("a","c","a","b"));
        assertEquals(asSet("c","b"), t.noFollow("a"));
        assertEquals(asSet("a"), t.noFollow("c"));
        assertEquals(Collections.emptySet(), t.noFollow("b"));
        assertEquals(Collections.emptySet(), t.noFollow("x"));
    }

    public void testNoFollow2(){
        Taboo<Integer> t = new Taboo<>(Arrays.asList(1,1,2,2,3,2,3,2,4,2,4));
        assertEquals(asSet(1,2), t.noFollow(1));
        assertEquals(asSet(4,3,2), t.noFollow(2));
        assertEquals(asSet(2), t.noFollow(4));
        assertEquals(Collections.emptySet(), t.noFollow(5));
    }

    public void testNoFollow3(){
        Taboo<Character> t = new Taboo<>(new ArrayList<Character>());
        assertEquals(Collections.emptySet(), t.noFollow('a'));

    }

    public void testReduce1(){
        Taboo<String> t = new Taboo<>(Arrays.asList("a","c","a","b"));
        List<String> list = new LinkedList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        t.reduce(list);
        assertEquals(Arrays.asList("a","x","c"),list);
    }

    public void testReduce2(){
        Taboo<String> t = new Taboo<>(Arrays.asList("a", "a", "b", "b"));
        List<String> list = new LinkedList<>(Arrays.asList("a", "a", "b", "x", "b", "b", "a", "b","c"));
        t.reduce(list);
        assertEquals(Arrays.asList("a", "x", "b", "a", "c"), list);
    }

    public void testReduce3(){
        Taboo<String> t = new Taboo<>(Arrays.asList("a", "b", null, "c", "d"));
        List<String> list = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "c", "c"));
        List<String> list2 = new ArrayList<>(Arrays.asList(null, null, "a", "b"));
        t.reduce(list);
        t.reduce(list2);
        assertEquals(Arrays.asList("a", "c", "c", "c"),list);
        assertEquals(Arrays.asList(null, null, "a"), list2);
    }

    public void testReduce4(){
        Taboo<Integer> t = new Taboo<>(new ArrayList<>());
        List<Integer> l = new ArrayList<>(Arrays.asList(2, 5, 4, 2, 4));
        List<Integer> l2 = new ArrayList<>();
        t.reduce(l);
        t.reduce(l2);
        assertEquals(Arrays.asList(2, 5, 4, 2, 4), l);
        assertEquals(l2, new ArrayList<>());
    }
}
