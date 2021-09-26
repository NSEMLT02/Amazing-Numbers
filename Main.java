package numbers;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String state = "c";

    public static void main(String[] args) {
        
        System.out.print("Welcome to Amazing Numbers!\n");
        printInstructions();
        
        // b Meaning to break
        while (state != "b") {
            requestProc(makeReq());   
        }
        
        scanner.close();
        System.out.println("Goodbye!");
    }
    
    
    /////// Handle Request Methods ///////
    public static String makeReq() {
        
        // just handle input, no more 
        System.out.println("\nEnter a request: ");
        return scanner.nextLine();
        
    }
    
    public static void requestProc(String req) {
        // For empty requests
        if (req.isEmpty()) {
            printInstructions();
            return;
        }
        
        String[] reqs = req.split(" "); 
        long num1 = parse(reqs[0]);
        long num2 = 0;
        
        // Check requests numbers
        if (num1 < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            return;
        } else if (num1 == 0) {
            // To end the program
            state = "b";
            return;
        } else if (reqs.length == 1) {
            
            Number n = new Number(num1);
            printProps(n);
        } else if (reqs.length == 2) {
            // In case there is a second number ensure is a natural nubmer
            num2 = parse(reqs[1]);
            if (num2 < 1) {
                System.out.println("The second parameter should be a natural number.");
                return;
            }
            
            printProps(num1,num2);
                
        } else if (reqs.length >= 3) {
            // Where there are 1 or more properties to check
            
            num2 = parse(reqs[1]);
            if (num2 < 1) {
                System.out.println("The second parameter should be a natural number.");
                return;
            } 
            
            // Check all inputted properties, see if they are valid ones
            List<String> props = Arrays.asList(Number.propertiesStrings);
            ArrayList<String> notFound = new ArrayList<String>();
            
            // Check which properties are valid
            for (int i = 2; i < reqs.length; i++) {
                
                if (!props.contains(reqs[i].toLowerCase()) && !props.contains(reqs[i].substring(1).toLowerCase())) {
                    notFound.add(reqs[i].toLowerCase());
                }
            }
            
            
            if (notFound.size() > 0) {
                if (notFound.size() == 1) {
                     System.out.printf("The property [%S] is wrong.\nAvailable properties: %S", notFound.get(0).toString(), Arrays.toString(Number.propertiesStrings));
                     return;
                } else {
                    System.out.printf("The properties %S are wrong.\nAvailable properties: %S", Arrays.toString(notFound.toArray()), Arrays.toString(Number.propertiesStrings));
                    return;
                }
            }
            
            List<String> reqsList = Arrays.asList(reqs);
            
            if (reqs.length >= 4) {

                // Search for mutually exclusive properties
                String[] exclusive1 = {"even", "duck", "sunny", "sad"};
                String[] exclusive2 = {"odd", "spy", "square", "happy"}; 
                
                
                for (int i = 0; i < exclusive1.length; i++) {
                    if ((reqsList.contains(exclusive1[i]) && reqsList.contains(exclusive2[i]))) {
                        // Deal with normal exclusive properties
                        System.out.printf("The request contains mutually exclusive properties: [%S, %S]\nThere are no numbers with these properties.\n", exclusive1[i], exclusive2[i]);
                        return;
                    } else if ((reqsList.contains('-' + exclusive1[i]) && reqsList.contains('-' + exclusive2[i]))) {
                      // Dealing with - exclusive properties
                      System.out.printf("The request contains mutually exclusive properties: [-%S, -%S]\nThere are no numbers with these properties.\n", exclusive1[i], exclusive2[i]);
                      return;
                    } 
                }
                // Deal with mixed exlusive properties
                for (int i = 0; i < Number.propertiesStrings.length; i++) {
                  if (reqsList.contains(Number.propertiesStrings[i]) && reqsList.contains("-" + Number.propertiesStrings[i])) {
                      
                      System.out.printf("The request contains mutually exclusive properties: [%S, -%S]\nThere are no numbers with these properties.\n", Number.propertiesStrings[i],Number.propertiesStrings[i]);
                      return;
                    }
                }
            } 
            
            printProps(num1, num2, reqsList);
            
            
        }
        
    
        
    }
    
    
    /////// Prtinting Methods ///////
    public static void printProps(Number num) {
            System.out.printf("Properties of %d\n", num.value);
            
            // Loop though the 2 arrays of object number, to save lines and being more readable
            for(int i = 0; i < num.properties.length; i++) {
                System.out.println(Number.propertiesStrings[i] + ": " + num.properties[i]);
            }
    }
    
    public static void printProps(long num1, long num2) {
        // Printing when there is more than one number
        // Loop to print number
        for (long i = num1; i < num1 + num2; i++) {
            System.out.println();
            Number n = new Number(i);
            printNumProperties(n);
        }
    }
    
    public static void printProps(long num1, long num2, List<String> reqs) {
        // Printing when there is more than one number and a property to search 
        // Loop to print number
        List<String> props = Arrays.asList(Number.propertiesStrings);
        ArrayList<Integer> temp = new ArrayList<Integer>(); // Properties that should be true
        ArrayList<Integer> temp2 = new ArrayList<Integer>(); // Properties that should be false
        String property;

        // Get index of properties that have to be true and false for search
        for (int i = 0; i < reqs.size() - 2; i++) {
            property = reqs.get(i + 2).toLowerCase();

            if (property.startsWith("-")) {
            temp2.add(props.indexOf(property.substring(1)));
            } else {
            temp.add(props.indexOf(property));
            }
        } 

        // Transform arraylist Integers to int array
        int[] truePropsIndex = new int[temp.size()];
        int in = 0;
        for (Integer value: temp) {
          truePropsIndex[in++] = value;
        }

        // Second Transform
        int[] falsePropsIndex = new int[temp2.size()];
        in = 0;
        for (Integer value: temp2) {
          falsePropsIndex[in++] = value;
        }

        // Merge 2 Arralist to
        temp.addAll(temp2);
        int[] allPropsIndex = new int[temp.size()];
        in = 0;
        for (Integer value: temp) {
          allPropsIndex[in++] = value;
        }

        // Counters
        int c = 0;
        int c2;
        int c3;

        for (long i = num1; c < num2 ; i++) {
             // First get requested properties of the current numer then check if they are true
            Number n = new Number(i, allPropsIndex);
            c2 = 0;
            c3 = 0;
            
            for (int index : truePropsIndex) {
                if (n.properties[index]) {
                    // if properties is true add 1 to a counter
                    c2++;
                } else {
                    // With one false property break
                    break;
                }
            }

            for (int index : falsePropsIndex) {
              if (!n.properties[index]) {
                // If property is false add 1
                c3++;
              } else {
                // Break if one is true
                break;
              }
            }
            
            if (c2 == truePropsIndex.length && c3 == falsePropsIndex.length) {
                // If number had all indicated properties add 1 to main counter.
                c++;
                System.out.println();
                n = new Number(i);
                printNumProperties(n);
            }   
        }
    }
    
    
    // -_-
    public static void printInstructions() {
        System.out.println("Supported requests:"
        + "\n- enter a natural number to know its properties;"
        + "\n- enter two natural numbers to obtain the properties of the list:"
        + "\n  * the first parameter represents a starting number;"
        + "\n  * the second parameter shows how many consecutive numbers are to be printed;"
        + "\n- two natural numbers and properties to search for;"
        + "\n- a property preceded by minus must not be present in numbers;"
        + "\n- separate the parameters with one space;"
        + "\n- enter 0 to exit.");
    }
    
    public static void printNumProperties(Number n) {
        
        System.out.printf("%d is ", n.value);
        
        // Loop to print true properties of number
        for (int j = 0; j < n.properties.length; j++) {
                    
            if (n.properties[j]) {
                // if property is true print property  name
                System.out.print(Number.propertiesStrings[j]);
                            
                if (n.properties.length - 1 != j) {
                    System.out.print(", ");
                }
            }
            
        }
    }
    
    public static long parse(String num) {
        // Ensure the input was a number not a String
        try { 
            return Long.parseLong(num);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}


class Number {
    
    final static String[] propertiesStrings = {"buzz", "duck", "palindromic", "gapful", "spy", "square", "sunny", "jumping", "even", "odd", "happy", "sad"};
    boolean[] properties;
    long value;
    
    public Number(long num, int... toCheck) {
        // If no parameter is pass to check, check all 
        if (toCheck.length == 0) {
          properties = new boolean[]{isBuzz(num), isDuck(num), isPalindromic(num), isGapful(num), isSpy(num),  
          isSquare(num), isSunny(num), isJumping(num), !isOdd(num), isOdd(num), isHappy(num), isSad(num)}; 
        } else if (toCheck.length > 0) {
          // Loop thorugh properties to check and only check the requested ones, to save processing time
          properties = new boolean[propertiesStrings.length];

          for (int j = 0; j < toCheck.length; j++) {
            int i = toCheck[j];
            switch(i) {
              case 0:
                properties[i] = isBuzz(num);
                break;
              case 1:
                properties[i] = isDuck(num);
                break;
              case 2:
                properties[i] = isPalindromic(num);
                break;
              case 3:
                properties[i] = isGapful(num);
                break;
              case 4:
                properties[i] = isSpy(num);
                break;
              case 5:
                properties[i] = isSquare(num);
                break;
              case 6:
                properties[i] = isSunny(num);
                break;
              case 7:
                properties[i] = isJumping(num);
                break;
              case 8:
                properties[i] = !isOdd(num);
                break;
              case 9:
                properties[i] = isOdd(num);
                break;
              case 10:
                properties[i] = isHappy(num);
                break;
              case 11:
                properties[i] = isSad(num);
                break;
              default:
            }
          }
        }
         
        value = num;
    }
    
    ///// get
    public boolean[] getProperties() {
        return properties;
    }
    
    ///// Methods to get properties
    public static boolean isOdd(long num) {
        return (num == 1 || num % 2 != 0);
    }
    
    public static boolean isBuzz(long num) {
        return (num % 7 == 0 || (num - 7) % 10 == 0);
    }
    
    public static boolean isDuck(Long num) {
        String numberString = num.toString();
        return numberString.contains("0");
    }
    
    public static boolean isPalindromic(long num){
        long reversed = 0;
        long copyNumber = num;
        // Reverse the number thorugh a loop
        for (;num != 0; num /= 10) {
            long digit = num % 10;
            reversed = reversed * 10 + digit;
        }
        
        return reversed == copyNumber;
    }
    
    public static boolean isGapful (long num) {
        
        // If length less than 3 no gapful
        int length = (int) (Math.log10(num) + 1);
        if (length < 3) {
            return false;
        }
        
        int len = (int) Math.log10(num);
        
        // Get first and last digit of num and concat it.
        long firstDigit = (long)(num / Math.pow(10, len));
        long lastDigit = num % 10;
    
        long divisor = firstDigit * 10 + lastDigit;
        
        return num % divisor == 0;        
    }
    
    public static boolean isSpy(long num) {
        // Make a sum and mul of number digits and compare
        long mul = 1;
        long sum = 0;
        long digit;
        
        while(num > 0) {
            digit = num % 10;
            sum +=  digit;
            mul *= digit;
            num /= 10;
        }
        
        return sum == mul;    
        
    }
    
    public static boolean isSquare(long num) {
        double sq = Math.sqrt(num);
        return ((sq - Math.floor(sq) == 0));
    }
    
    public static boolean isSunny(long num) {
        return isSquare(num + 1);
    }
    
    public static boolean isJumping(long num) {
        // Take digits with logs.
        
        while (num != 0) {
            long digit1 = num % 10;
            num /= 10;
            
            if (num != 0) {
                long digit2 = num % 10;
                
                if (Math.abs(digit2-digit1) != 1) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public static boolean isHappy(long num) {
      long sum;
      long result = num;
      long rem = 0;

      // Repeat Square sum until one part of the sum is 4 or 1
      while (result != 1 && result != 4) {
        sum = 0;

        // Square sum
        while(result > 0) {
          rem = result % 10;
          sum += rem * rem;
          result /= 10;
        }

        result = sum;
      }
      

      if (result == 1) {
        return true;
      } else {
        return false;
    }

  }

  public static boolean isSad(long num) {
    return !isHappy(num);
  }
}