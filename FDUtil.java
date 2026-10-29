import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;

/**
 * This utility class is not meant to be instantitated, and just provides some
 * useful methods on FD sets.
 * 
 * @author Noah Weatherton
 * @version 10/26/2026
 */
public final class FDUtil {
  /**
   * Generates all trivial FDs over the given attribute set.
   * FDs with empty right-hand sides are omitted.
   *
   * @param attrs the relation's attributes; must not be modified
   * @return a new set containing all trivial FDs over the given attributes
   */
  public static FDSet trivial(final Set<String> attrs) {
    FDSet result = new FDSet();
    Set<Set<String>> subsets = powerSet(attrs);
    for (Set<String> left : subsets) {
      for (Set<String> right : subsets) { // Get all possible left and right hand sides from the powerset
        if (!right.isEmpty() && left.containsAll(right)) {
          result.add(new FD(new TreeSet<>(left), new TreeSet<>(right)));
        }
      }
    }
    return result;
  }
  

  /**
   * Augments every FD in the given set of FDs with the given attributes
   * 
   * @param fdset FD Set (Immutable)
   * @param attrs a set of attributes with which to augment FDs (Immutable)
   * @return a set of augmented FDs
   */
  public static FDSet augment(final FDSet fdset, final Set<String> attrs) {
    FDSet result = new FDSet();
    for (FD fd : fdset) {
      Set<String> left = new TreeSet<>(fd.getLeft());
      Set<String> right = new TreeSet<>(fd.getRight());
      left.addAll(attrs);
      right.addAll(attrs);
      result.add(new FD(left,right));
    }
    return result;
  }

  /**
   * Exhaustively resolves transitive FDs with respect to the given set of FDs
   * 
   * @param fdset (Immutable) FD Set
   * @return all transitive FDs with respect to the input FD set
   */
  public static FDSet transitive(final FDSet fdset) {
    FDSet copy = new FDSet(fdset);
    boolean hasChanged = true;
    while (hasChanged) {
      hasChanged = false;
      int saishosize = copy.size();
      FDSet copy2 = new FDSet(copy);
      for (FD fd1 : copy2) {
        for (FD fd2 : copy2) {
          if (fd1.getRight().containsAll(fd2.getLeft())) {
              FD newFD = new FD(new TreeSet<>(fd1.getLeft()), new TreeSet<>(fd2.getRight()));
              copy.add(newFD);
            
          }
        }
      }
      hasChanged = copy.size() != saishosize;
    }
    // Have to do some shenanigans because FDSet does not have contains 
    Set<FD> original = fdset.getSet();
    FDSet result = new FDSet();
    for (FD fd : copy) {
      if (!original.contains(fd)) {
        result.add(fd);
      }
    }
    return result;
  }

  /**
   * Generates the closure of the input FD set, omitting FDs with empty
   * right-hand sides. The relation's attributes are assumed to be exactly
   * those appearing on either side of the input FDs.
   *
   * @param fdset input FD set; must not be modified
   * @return a new FD set containing the closure
   */
  public static FDSet fdSetClosure(final FDSet fdset) {
    FDSet current = new FDSet(fdset); // make a copy of F
    Set<String> attrs = new TreeSet<>();
    for (FD fd: fdset) {
      attrs.addAll(fd.getLeft());
      attrs.addAll(fd.getRight());
    }
    // Add all trivial attributes!
    current.addAll(trivial(attrs));
    Set<Set<String>> subsets = powerSet(attrs);
    boolean hasChanged = true;
    while (hasChanged) {
      // Saisho is japanese for at first
      int saishosize = current.size();
      for (Set<String> z : subsets) {
        current.addAll(augment(current,z));
      }
      current.addAll(transitive(current));
      hasChanged = current.size() != saishosize;
    }
    return current;
  }

  /**
   * Generates all subsets of the input set, including the empty subset.
   *
   * @param set input set; must not be modified
   * @return the power set of the input set
   */
  public static <E> Set<Set<E>> powerSet(final Set<E> set) {
    Set<Set<E>> result = new HashSet<>();
    if (set.isEmpty()) {
      result.add(new HashSet<>());
      return result;
    } else {
      Set<E> copy = new HashSet<>(set);
      E randomItem = copy.iterator().next();
      copy.remove(randomItem);
      
      Set<Set<E>> recursedSet = powerSet(copy);
      result.addAll(recursedSet); // Add all subsets that dont contain the recursedSet
      for (Set<E> subset : recursedSet) {
        Set<E> setsWithRandomItem = new HashSet<>(subset);
        setsWithRandomItem.add(randomItem);
        result.add(setsWithRandomItem);
      }
    return result;
    }
  }
}