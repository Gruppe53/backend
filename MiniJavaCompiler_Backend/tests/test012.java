class A {
  public static void main(String[] arg) {
    B o;
    o = new B();
    System.out.println(o.getX());
    o.setX(42);
    System.out.println(o.getX());
    System.out.println(o.getFakeX());
    return;
  }
}

class B {
  
  int x;

  int getX() { return x; }
  void setX(int v) { x=v; return; }
  int getFakeX() { int x; x=42; return x; }

}

