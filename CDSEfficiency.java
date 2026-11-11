import java.util.*;
import java.io.*;
public class CDSEfficiency
{
   public static void main(String[] args){
      int cooldown_level = 0; //1-15: R= 0.85^(0.40+0.85*level), 1: R=1
      double cooldown = 25;
      double cds = 3;
      double roll_cooldown = 1.8;
      ArrayList<Double> values = new ArrayList<Double>();
      double time_since_last_satchel = 0.0;//internal tracking of the time, this will always climb to cooldown
      double time_since_last_satchel_a = 0.0;//actual user experience !!WHAT WE NEED TO LOG
      double time_total = 0; 
      int arrow_count = 1; 
      double offset = 0.01;
      ArrayList<Double> valuescollection = new ArrayList<Double>();
      //setup:
      if(cooldown_level > 0)
         cooldown *= Math.pow(0.85, 0.4+0.85*cooldown_level);
      System.out.println(cooldown+ " seconds");
      
      cds /= 2;
      cds *= arrow_count;
      int roll_count = 0;
      int interation = 0;
      double total_offset = 0.0;
      
      Scanner input = new Scanner(System.in);
      System.out.println("How long is the cooldown of the artifact?");
      System.out.println("How many levels of Acrobat do you have?");
      System.out.println("Do you have +100% roll cooldown?");
      System.out.println("Do you have +50% faster roll?");
      System.out.println("How many levels of cooldown do you have? (cooldown armor perk counts as 3)");
      System.out.println("How many levels of cooldown shot do you have?");
      System.out.println("How many arrows do you fire when you roll?"); 
      while(total_offset < roll_cooldown){
         values.clear();
         time_total = total_offset;
         time_since_last_satchel_a = total_offset;
         time_since_last_satchel = total_offset;
         roll_count = 0;
         while(roll_count < 1000000){
            if(time_since_last_satchel + roll_cooldown > cooldown){           //what if we don't get the chance to roll?
            //reset system (t_a -> values, and t_a, t = 0), process the full check
               double remains = roll_cooldown - (cooldown - time_since_last_satchel);// store remaining for later
               time_since_last_satchel_a += cooldown - time_since_last_satchel;//make sure to catch t_a up
            //time_total += cooldown - time_since_last_satchel;               //and be sure to to do it for t_t too
               values.add(time_since_last_satchel_a);//stored
               time_since_last_satchel_a = 0;//reset
               time_since_last_satchel   = 0;//reset
            
            //time to update base on remaining time 
               time_since_last_satchel_a += remains;
               time_since_last_satchel += remains + cds;
               time_total += roll_cooldown;
            //add remaining roll time to both t_a and t, we'll be sure to check again to see if this was an insta cooldown
            }else{                                                            //then we can roll, so add it
               time_since_last_satchel += roll_cooldown + cds;
               time_total += roll_cooldown;
               time_since_last_satchel_a += roll_cooldown;
            }                                                              //check to see if needs reseting and processing
            if(time_since_last_satchel >= cooldown){
            //store and reset t_a, reset t, 
               //System.out.println(time_since_last_satchel_a);
               values.add(time_since_last_satchel_a);//stored
               time_since_last_satchel_a = 0;//reset
               time_since_last_satchel   = 0;
            
            }
            roll_count++;
         }
         //System.out.println(values);
         double a = 0;
         for(Double d:values){
            a+=d;
         }
         //System.out.println(total_offset+": "+a/values.size());
         valuescollection.add(a/values.size());
         total_offset += offset;
      }
      System.out.println(valuescollection);
      int min = 0;
      for(int i = 0; i < valuescollection.size(); i++){
         if(valuescollection.get(i) < valuescollection.get(min)) min = i;
      }
      System.out.println("Looping value: "+valuescollection.get(min)); 
      System.out.println("Cooldown Shot Efficiency: "+(1-valuescollection.get(min)/cooldown)+"%");
   }
}