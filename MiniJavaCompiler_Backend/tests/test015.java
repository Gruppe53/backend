class A {

   public static void main(String[] args) {
     B x;
     C y;
     D z;

     x = new B();
     y = new C();
     z = new D();

     x.test();
     y.test();
     z.test();

     x = y;
     x.test();
     x = z;
     x.test();

     return;
   }
}

class B {
   void test() {
     System.out.println("test in B");
     return;
   }
}

class C extends B {
   void test() {
     System.out.println("test in C");
     return;
   }
}

class D extends B {
}
