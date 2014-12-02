class A {
  public static void main(String[] arg) {
    B x;
    x = new B();
    x.test();
    x.test(42);
    x.test(40,2);
    x.test(40,1,1);
    return;
  }
}

class B {
    void test() {
      System.out.println("test");
      return;
    }
    void test(int x) {
      System.out.print("test ");
      System.out.println(x);
      return;
    }
    void test(int x, int y) {
      test(x+y);
      return;
    }
    void test(int x, int y, int z) {
      test(x+y+z);
      return;
    }
}
