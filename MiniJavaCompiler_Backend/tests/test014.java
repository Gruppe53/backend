class A {

   public static void main(String[] args) {
     int[] sieve;
     int idx;
     int mark;

     sieve = new int[100];

     idx = 0;

     while (idx<100) {
       sieve[idx]=0;
       idx=idx+1;
     }

     idx = 2;

     while (idx<100) {

       if (sieve[idx]==0) {
         sieve[idx]=1;

         mark=idx+idx;

         while (mark<100) {
           sieve[mark]=2;
           mark=mark+idx;
         }
       }
       idx=idx+1;
     }

     idx=0;

     while (idx<100) {

       if (sieve[idx]==1) {
         System.out.print(idx);
         System.out.println(" is prime.");
       }
       idx=idx+1;
     }

     return;
   }
}
