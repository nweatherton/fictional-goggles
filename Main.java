import java.util.*;

public class Main {

  public static void main(String[] args) {
    

    // System.out.println(FDUtil.powerSet(f2.getLeft()));

    System.out.println("HW TEST");
    // One FD Set = A --> A,B and A,B --> C
    FD f1 = new FD(Set.of("A"), Set.of("A", "B")); // A --> A,B
    FD f2 = new FD(Set.of("A", "B"), Set.of("C")); // A,B --> C
    FDSet fdset = new FDSet(f1, f2);

    // Another FD set: A,B --> B and A --> B,C and A,B --> C
    FD g1 = new FD(Set.of("A", "B"), Set.of("B")); // A,B --> B
    FD g2 = new FD(Set.of("A"), Set.of("C", "B")); // A --> B,C
    FD g3 = new FD(Set.of("A", "B"), Set.of("C")); // A,B --> C
    FDSet fdset2 = new FDSet(g1, g2, g3);

    // Test FD set equality: fdset and fdset2 are logically equivalent iff their closures are equivalent
    System.out.println("Logically Equivalent? " + (fdset.isEquivalentTo(fdset2)));

    //MY TESTS
      // Test powerSet()
    Set<String> attributes = Set.of("A", "B", "C");

    System.out.println("Power set:");
    System.out.println(FDUtil.powerSet(attributes));
    System.out.println(FDUtil.powerSet(Set.of()));
  }
}
