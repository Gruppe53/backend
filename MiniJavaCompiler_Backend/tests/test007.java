class A {
  public static void main(String[] arg) {
    boolean x;
    x = true;
    if (x) {
      System.out.println("This should be printed.");
    }
    x = false;
    if (x) {
      System.out.println("This should not be printed.");
    }
    return;
  }
}
