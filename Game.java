package cs2365_project2;

import java.util.ArrayList;
import java.util.Collections;

/**
        Game class has 3 methods, StartGame, FormatCardForWorkout, and GetMaxReps(For mathematical purpose in Stat collection)
        Requires two other classes to function correctly, Deck and Card
       
        StartGame asks the user how many decks they want,
        asks the user whether or not to shuffle them all together
        asks the user whether or not they wish to remove Special cards
       
        Proceeds to draw 7 cards, sends them to the FormatCardForWorkout method
       
        In the FormatCardForWorkout method, it counts the amount of reps of each workout
        then takes into account the special cards and proceeds to do the functions
        listed in the project 2 document on blackboard
   
        Then the output is displayed per exercise in the console
   
        Then we jump back to the StartGame method and it proceeds to repeat this process
        until all the Decks have been used
        
        * @author Demetrios Mihaltses
   
    **/
class Game
{
    //FOR STAT COLLECTION--> 
        
    int[] StatsWorkout = new int[3];
    int[] WorkoutReps = new int[5];
    int[] skippedR = new int[4];


   htmlOutput html = new htmlOutput();
       
   /**
        Main function that Starts and runs through the game.
        * Goes through all the decks and changes decks when no more cards can be drawn from it
   
    */
    public void StartGame(int Pick1, int Pick2, int Pick3)
    {      
    	
        if(Pick1 > 3)
        {
            Pick1 = 3;
        }
        else if(Pick1 <= 0)
        {
            Pick1 = 1;
        }
        
        //filling the Decks
        Deck[] Decks = new Deck[Pick1];
        
       for(int a = 0; a < Decks.length; a++)
       {
           Decks[a] = new Deck();
           Decks[a].CreateDeck();
       }
        
        //shuffling all the decks together
        if(Pick2 == 1 && Decks.length > 1)
        {
            ArrayList<Card> All = new ArrayList<Card>();
            for(int a = 0; a < Decks.length; a++)
            {
                ArrayList<Card> DeckInd = Decks[a].GetDeck();
                for(int b = 0; b < DeckInd.size(); b++)
                {
                    All.add(DeckInd.get(b));
                }
            }
            
            Collections.shuffle(All);
            
            //redistrbuting the cards into the decks
            int index = 0;
            for(int a = 0; a < Decks.length; a++)
            {
                ArrayList<Card> NewD = new ArrayList<Card>();
                for(int b = 0; b < 108; b++)
                {
                    if(All.get(index) != null)
                    {
                        NewD.add(All.get(index));
                        index++;
                    }
                }
                
                Decks[a].RefillDeck(NewD);
            }
        }
        
        //The Player can then choose to remove special cards!!!
        if(Pick3 == 1)
        {
            for(int a = 0; a < Decks.length; a++)
            {
                Decks[a].RemoveSpecialCards();
            }
        }
        
        //Let the game begin!
        int count = 1;
        ArrayList<Card> Hand = new ArrayList<Card>();
        for(int a = 0; a  < Decks.length; a++)
        {
            Hand.clear();
            while(!Decks[a].isEmpty())
            {
                if(Hand.size() == 0)
                {
                    ArrayList<Card> Temp = Decks[a].DrawCards(7);
                    
                    //function to sort the hand and display the data
                    html.setHandHeading(count);
                    count++;
                    SortingHand(Temp);

                    for(int b = 0; b < Temp.size(); b++)
                    {
                        Hand.add(Temp.get(b));
                    }
                    
                    //Hand has been filled
                    FormatCardForWorkout(Hand, Decks[a]);
                    
                    //Will draw a new hand
                    Temp.clear();
                    Hand.clear();
                    
                }
                
                //Number of Cards Left3
                
            int RemainingCards = Decks[a].CardsLeftOnDeck() + (108 * ( Decks.length - (a+1)));
            html.setCardsLeft(RemainingCards);
            System.out.println("Cards left to draw from - " + RemainingCards + "\n");
                
            }
        }
        
        //Display Stat Data
        DisplayStatData(StatsWorkout, WorkoutReps, skippedR);
        html.writeToFile();
        System.out.println("Game over!!! Hopefully you had a good workout!");
    }
    
    /**
     * Function takes in each hand and the Deck that you are currently on and formats the series
     * of cards into a workout routine via card color, value, and special type.
     * @param H
     * @param D
     */
    void FormatCardForWorkout(ArrayList<Card> H, Deck D)
    {
        int RepsSitUps = 0;
        int RepsPushUps = 0;
        int RepsSquat = 0;
        int RepsLounges = 0;
        int Burpees = 0;
        
        
        for(int a = 0; a < H.size(); a++)
        {
            boolean isS = H.get(a).Special;
            int V = H.get(a).getValue();
            String Col = H.get(a).getString(true);
            String Spe = H.get(a).getString(false);
            
            if(!isS)
            {
                if(Col.equals(("Red")))
                {
                    RepsSitUps += V;
                }
                else if(Col.equals(("Blue")))
                {
                    RepsPushUps += V;
                }
                else if(Col.equals(("Yellow")))
                {
                    RepsSquat += V;
                }
                else
                {
                    //Green
                    RepsLounges += V;
                }
            }
        }
        
        
        for(int a = 0; a < H.size(); a++)
        {
            boolean isS = H.get(a).Special;
            int V = H.get(a).getValue();
            String Col = H.get(a).getString(true);
            String Spe = H.get(a).getString(false);
            
            if(isS)
            {
                if(Spe.equals("Skip"))
                {
                    if(Col.equals(("Red")))
                    {
                        skippedR[0] += RepsSitUps;
                        RepsSitUps = 0;
                    }
                    else if(Col.equals(("Blue")))
                    {
                        skippedR[1] += RepsPushUps;
                        RepsPushUps =0;
                    }
                    else if(Col.equals(("Yellow")))
                    {
                        skippedR[2] += RepsSquat;
                        RepsSquat =0;
                    }
                    else
                    {
                        //Green
                        skippedR[3] += RepsLounges;
                        RepsLounges = 0;
                    }  
                }
                else if(Spe.equals("Draw 2"))
                {
                    if(Col.equals(("Red")))
                    {
                        RepsSitUps *= 2;
                    }
                    else if(Col.equals(("Blue")))
                    {
                        RepsPushUps *= 2;
                    }
                    else if(Col.equals(("Yellow")))
                    {
                        RepsSquat *= 2;
                    }
                    else
                    {
                        //Green
                        RepsLounges *= 2;
                    }                     
                }
                else if(Spe.equals(("Wild")) || Spe.equals(("Wild Draw 4")))
                {
                    Burpees += 10;
                    RepsSitUps *= 4;
                    RepsPushUps *= 4;
                    RepsSquat *= 4;
                    RepsLounges *= 4;
                }
            }
        }
        
        
        if(RepsSitUps > 0)
        {
            System.out.println("Do " + RepsSitUps + " reps of Sit Ups!");
        }
        
        if(RepsPushUps > 0)
        {
            System.out.println("Do " + RepsPushUps + " reps of Push Ups!");
        }
        
        if(RepsSquat > 0)
        {
            System.out.println("Do " + RepsSquat + " reps of Squats!");
        }
        
        if(RepsLounges > 0)
        {
            System.out.println("Do " + RepsLounges + " reps of Lunges!");
        }
        
        if(Burpees > 0)
        {
            System.out.println("Do " + Burpees + " reps of Burpees!");
        }
        
        System.out.println();
        
        //Stat collection function gets called
        WorkoutReps[0] += RepsPushUps;
        WorkoutReps[1] += RepsSitUps;
        WorkoutReps[2] += RepsSquat;
        WorkoutReps[3] += RepsLounges;
        WorkoutReps[4] += Burpees;
        int TotalR = RepsPushUps + RepsSitUps + RepsSquat + RepsLounges + Burpees;
        int MaxR = GetMaxReps(RepsPushUps, RepsSitUps, RepsSquat, RepsLounges, Burpees);
        
        D.StatCollection(TotalR, 0, MaxR);
        
        UpdateStatsTable(D.UDateStatData());
        
    }
    
     /**
     * Function globally keeps track of every Rep performed, every skipped rep, and the max reps
     * performed in a single set throughout the entire game
     * @param Stats
     */
    void UpdateStatsTable(int[] Stats)
    {
        StatsWorkout[0] += Stats[0];
        StatsWorkout[1] += Stats[1];
        
        if(StatsWorkout[2] < Stats[2])
        {
            StatsWorkout[2] = Stats[2];
        }
        
    }
    
     /**
     * Function displays the stats of game, called at the end of the game. Takes in two
     * Arrays that both have data pertaining to total stats
     * @param TotalStats
     * @param IndStats
     */
    void DisplayStatData(int[] TotalStats, int[] IndStats, int[] skipped)
    {
    	html.setExerciseHeading();
        System.out.println("---Workout Stats---");
        
        System.out.println("Total Reps - " + TotalStats[0]);
        System.out.println("Total Skipped Reps - " + TotalStats[1]);
        System.out.println("Max Reps performed - " + TotalStats[2] + "\n");
        
        System.out.println("Total Pushups - " + IndStats[0]);
        html.setExerciseTotal(IndStats[0], skipped[0], "Pushups");
        System.out.println("Total Situps - " + IndStats[1]);
        html.setExerciseTotal(IndStats[1], skipped[1], "Situps");
        System.out.println("Total Squats - " + IndStats[2]);
        html.setExerciseTotal(IndStats[2], skipped[2], "Squats");
        System.out.println("Total Lunges - " + IndStats[3]);
        html.setExerciseTotal(IndStats[3], skipped[3], "Lunges");
        System.out.println("Total Burpees - " + IndStats[4] + "\n");
        html.setExerciseTotal(IndStats[4], 0 , "Burpees");
    }
            
      /**
     * Sorting method that groups a set of cards from hand H, into separate arrayList via color
     * @param H
     */
    void SortingHand(ArrayList<Card> H)
    {
        ArrayList<Card> Wild = new ArrayList<Card>();
        ArrayList<Card> Red = new ArrayList<Card>();
        ArrayList<Card> Blue = new ArrayList<Card>();
        ArrayList<Card> Yellow = new ArrayList<Card>();
        ArrayList<Card> Green = new ArrayList<Card>();
        
        for(int a = 0; a < H.size(); a++)
        {
            String C = H.get(a).getString(true);
            
            if(C.equals("Red"))
            {
                Red.add(H.get(a));
            }
            else if(C.equals("Blue"))
            {
                Blue.add(H.get(a));
            }
            else if(C.equals("Yellow"))
            {
                Yellow.add(H.get(a));
            }
            else if(C.equals("Green"))
            {
                Green.add(H.get(a));
            }
            else
            {
                Wild.add(H.get(a));
            }

        }
        
        //sort them
        
        Red = SortByRank(Red);
        Blue = SortByRank(Blue);
        Yellow = SortByRank(Yellow);
        Green = SortByRank(Green);
        
        System.out.println("-----New Hand-----");
        
        for(Card red1: Red)
        {
        	html.setCardDescription(red1);
            System.out.print(red1.getCardOutput() + ", ");
        }
        for(Card blue1: Blue)
        {
        	html.setCardDescription(blue1);
            System.out.print(blue1.getCardOutput() + ", ");
        }
        for(Card yellow1: Yellow)
        {
        	html.setCardDescription(yellow1);
            System.out.print(yellow1.getCardOutput() + ", ");
        }
        for(Card green1: Green)
        {
        	html.setCardDescription(green1);
            System.out.print(green1.getCardOutput() + ", ");
        }
        for(Card wild1: Wild) {
        	html.setCardDescription(wild1);
            System.out.print(wild1.getCardOutput() + ", ");
        }
        
        System.out.println();
        
    }
    
     /**
     * Sorting method that takes in a set of cards, already sorted by color, and sorts them via value that they hold
     * and returns the sorted list from smallest to largest.
     * @param HandCol
     * @return
     */
    ArrayList<Card> SortByRank(ArrayList<Card> HandCol)
    {
        for(int j = 0; j < HandCol.size()-1; j++)
        {
		for (int i = 0; i < HandCol.size()-1; i++)
        	{
            		if (HandCol.get(i).getValue() > HandCol.get(i+1).getValue())
            		{
                		Card PlaceHolder = HandCol.get(i+1);
                		HandCol.set(i+1, HandCol.get(i));
               			HandCol.set(i, PlaceHolder);
            		}
        	}
        }
        
        return HandCol;
    }
    
    /**
     * Function takes in all the reps done per exercise in a given hand, and finds which one
     * was done the most. Essentially, finds the largest value passed in.
     * @param Push
     * @param Sit
     * @param L
     * @param Sq
     * @param B
     * @return
     */
    int GetMaxReps(int Push, int Sit, int L, int Sq, int B)
    {
        
        if(Push > Sit && Push > L && Push > Sq && Push > B)
        {
        	html.setMaxHandReps(Push, "PushUps");
            return Push;
        }
        else if(Sit > Push && Sit > L && Sit > Sq && Sit > B)
        {
        	html.setMaxHandReps(Sit, "Situps");
            return Sit;
        }
        else if(L > Push && L > Sit && L > Sq && L > B)
        {
        	html.setMaxHandReps(L, "Lunges");
            return L;
        }
        else if( Sq > Push && Sq > Sit && Sq > L && Sq > B)
        {
        	html.setMaxHandReps(Sq, "Squats");
            return Sq;
        }
        html.setMaxHandReps(B, "Burpees");
        return B;
    }
}