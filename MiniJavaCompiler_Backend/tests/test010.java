class A {
  public static void main(String[] arg) {
    boolean x;
    x = true;
    if (x && false ) {
      System.out.println("This should not be printed.");
    } else {
      System.out.println("This should be printed.");
    }
    if (x && true ) {
      System.out.println("This should be printed.");
    } else {
      System.out.println("This should not be printed.");
    }
    return;
  }
}
