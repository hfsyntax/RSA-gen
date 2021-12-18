import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Noah Kaiser
 * @date 9/25/21
 */

public class KaiserNoahRSA
{
    public int gcd(int inE, int inZ) {
      int divisor = 0;
      while (inZ != 0) {
        divisor = inZ;
        inZ = inE % inZ;
        inE = divisor;
      }
      return inE;
    }
    
    public void testGcd () {
      int result1 = gcd (29, 288);
      int result2 = gcd (30, 288);
      System.out.println ("GCD (29, 288) = 0x" + Integer.toString(result1, 16));
      System.out.println ("GCD (30, 288) = 0x" + Integer.toString(result2, 16));
    }
    
    public int xgcd(int inE, int inZ) {
      
      if (gcd(inE, inZ) != 1) {
        return -1;
      }
      
      int base = inZ; // if negative answer exists
      int prev = 0; // previous b
      int curr = 1; // current b
      int tempE = 0; // hold inE
      int tempB = 0; // hold current b
      int divisor = 0; // compute divisor
      
      while (inE != 0) {
        divisor = inZ / inE;
        tempE = inE;
        inE = inZ % inE;
        inZ = tempE;
        
        // if a remainder exists
        if (inE != 0) {
          tempB = curr;
          curr = prev - curr * divisor;
          prev = tempB;
        }
      }
      // convert negative answer
      while (curr < 0) 
        curr += base;
      return curr;
    }
    
    public void testXgcd () {
      int result1 = xgcd (29, 288);
      int result2 = xgcd (149, 288);

      System.out.println ("29^-1 mod 288 = 0x" + Integer.toString(result1, 16));
      System.out.println ("149^-1 mod 288 = 0x" + Integer.toString(result2, 16));
    }

    
    public int[] keygen (int inP, int inQ, int inE) {
      int n = inP * inQ;
      int z = (inP - 1) * (inQ - 1);
      int d = 0;
      ArrayList<Integer> composites = new ArrayList<Integer>(); 
      
      // generate new e
      if (inE <= 1) {
        for (int i = 0; i < z; i++) {
          if (gcd(i, z) == 1) {
            // add all composites in interval to list
            composites.add(i);
          }
        }
        // get random number from list
        int index = new Random().nextInt(composites.size());
        int number = composites.get(index);
        inE = number;
      }
      
      // check if e is co-prime with z
      if (inE > 1 && inE < z) {
        if (gcd(inE, z) != 1) {
          System.out.println("inE and z are not coprime");
          return null;
        } 
      }
      
      d = xgcd(inE, z);
      
      return new int[] {inE,n,d};
    } 
    
    public void testKeygen () {
      int[] keypair = keygen (17, 19, 29);

      System.out.println ("e = 0x" + Integer.toString(keypair[0], 16));
      System.out.println ("N = 0x" + Integer.toString(keypair[1], 16));
      System.out.println ("d = 0x" + Integer.toString(keypair[2], 16));
    }
    
    //c = a^b mod n
    public int modExp (int a, int b, int n) {
      int x = 1;
      int w = a; 
      int y = b;
      
      while (y > 0) {
        int t = y%2; 
        y = y/2;
        if (t == 1) { 
          long xLong = x * w; 
          x = (int) (xLong % n);
        }
        
        long wLong = w * w;
        w = (int) (wLong % n);
      }

      return x;
    }

    public int encrypt (int message, int inE, int inN) {
      return modExp(message, inE, inN);
    }

    public int decrypt (int ciphertext, int inD, int inN) {
      return modExp(ciphertext, inD, inN);
    }
    
    public void testRSA () {
      int[] keypair = keygen (17, 19, 29);

      int m1 = 4;
      int c1 = encrypt (m1, keypair[0], keypair[1]);
      System.out.println ("The encryption of (m1=0x" + Integer.toString(m1, 16) + ") is 0x" + Integer.toString(c1, 16));
      int cleartext1 = decrypt (c1, keypair[2], keypair[1]);
      System.out.println ("The decryption of (c=0x" + Integer.toString(c1, 16) + ") is 0x" + Integer.toString(cleartext1, 16));

      int m2 = 5;
      int c2 = encrypt (m2, keypair[0], keypair[1]);
      System.out.println ("The encryption of (m2=0x" + Integer.toString(m2, 16) + ") is 0x" + Integer.toString(c2, 16));
      int cleartext2 = decrypt (c2, keypair[2], keypair[1]);
      System.out.println ("The decryption of (c2=0x" + Integer.toString(c2, 16) + ") is 0x" + Integer.toString(cleartext2, 16));
    }
 
    public static void main(String[] args) {
      KaiserNoahRSA output = new KaiserNoahRSA ();

      System.out.println ("********** Project 1 output begins ********** ");

      output.testGcd ();
      output.testXgcd ();
      output.testKeygen ();
      output.testRSA ();
    }
    
}
