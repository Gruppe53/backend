class A {
  public static void main(String[] args) {
    Factorial f;
    int x;

    f = new Factorial();
    x=f.compute(6);

    System.out.println(x);
    return;
  }
}

class Factorial {
  
  int compute(int arg) {

    if (arg==0) { return 1; }
    if (arg==1) { return 1; }

    return arg*compute(arg-1);

  }
}
