package chess.game;
/* will a quantum computer solve the problem of chess for all? with that computing power
*  we could analyze the 10^120 possible games of chess.
*  --(http://vision.unipv.it/IA1/aa2009-2010/ProgrammingaComputerforPlayingChess.pdf)--
*  pasted above is a link to the short book by Claude Shannon, received from Bell Labs in 
*  on November 8, 1949].
*  
*/


/*
*I thank Logic Crazy Chess for posting the tutorial of programming this chess game 
*on youTube, it has inspired me and allowed me to touch on so many points of my
*programming skill at once, it has cleared a lot of anxiousness and doubt, and made 
*me a bit more confident with programming
*/
import static java.lang.Character.*;
import java.util.*;
import javax.swing.*;


public class ChessGame {

   // A statement of the position of all pieces on the board.
    
    /*for example
        \\
         \\
         _\\_      ======> 
         V\//
          \/
    */
    static String chessBoard[][]={
       
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"},
        
    };
    
    static int kingPositionCap, kingPositionLow;
    static int globalDepth = 4;

    public static void main(String[] args) {
    
        while (!"A".equals(chessBoard[kingPositionCap/8][kingPositionCap%8])){kingPositionCap++;}//get A location
        while (!"a".equals(chessBoard[kingPositionLow/8][kingPositionLow%8])){kingPositionLow++;}//get A location
        
        System.out.println(kingPositionCap);
        /*
        * PIECE = WHITE/colored
        * pawn =P/p
        * knight = K/k
        * bishop = B/b
        * rook = R/r
        * Queen = Q/q
        * King = A/a
        *
        * The strategy is to create an alpha-beta tree diagram which returns
        * the best outcome
        *
        * (1234b represents row1, column2 moves to row3, column4 which captured
        * b (a space represents no capture))
        */
        
        /*JFrame f; //the window for the software.
        f = new JFrame("Viotan Ultimate Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface();
        f.add(ui);
        f.setSize(500, 500);
        f.setVisible(true);
        */
        
        System.out.println(possibleMoves());
        makeMove(alphaBeta(globalDepth, 1000000, -1000000,"",0));
        
        makeMove("6050 ");
        undoMove("6050 ");
        
        for(int i=0; i<8; i++){
        System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
    
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player){
        // return in the form of 1234b#######
        //we return the move in the first five spaces then the score takes up whatever is after.
        //trees grow down not up?? wtf vick!
        
        String list = possibleMoves();
        if (depth == 0  || list.length() == 0){
        //sort later
          return move + (rating()* (player*2-1));
        }
//        list ="";
        
//        System.out.println("the number of moves are: ");
//        Scanner sc = new Scanner(System.in);
//        int temp = sc.nextInt();
//        
//        for(int i=0;i< temp; i++){
//            list+="1111b";
//        }
          player = 1-player;
          
          for(int i=0; i<list.length(); i+=5){
            makeMove(list.substring(i, i+5));
          
             flipBoard();
          String returnString = alphaBeta(depth-1, beta, alpha, list.substring(i, i+5), player);
          int value = Integer.valueOf(returnString.substring(5));
             flipBoard();
          undoMove(list.substring(i, i+5));
          
          if(player == 0){
              if(value <= beta){beta = value; if(depth == globalDepth){move = returnString.substring(0,5);}}
          }else{
              if(value > alpha){alpha = value; if(depth == globalDepth){move = returnString.substring(0,5);}}

          } if(alpha >= beta){
              if(player==0){return move+beta;} else{return move+alpha;}
          }
        }
        
    
        if(player==0){return move+beta;} else{return move+alpha;}
}
    
    public static void flipBoard(){
     
     String temp;
       for (int i =0;i<32;i++){
            
            int r=i/8, c=i%8;
            
            if(Character.isUpperCase(chessBoard[r][c].charAt(0))){
                temp=chessBoard[r][c].toLowerCase();
        }else{
            temp=chessBoard[r][c].toUpperCase();
            }
            
            if(Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))){
                chessBoard[r][c] = chessBoard[7-r][7-c].toLowerCase();
        }else{
            chessBoard[r][c] = chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]= temp;
    }
    int kingTemp = kingPositionCap;
    kingPositionCap = 63-kingPositionLow;
    kingPositionLow = 63-kingTemp;
    
    }
    
    
    
    public static void makeMove(String move){
        
      //still really need to understand what the string move is doing 
        if (move.charAt(4)!= 'P'){
            
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]= chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])){
                kingPositionCap=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        }else{
            //if pawn promo
            //column1, column2, captured-piece, new-piece, P
            chessBoard[1][Character.getNumericValue(move.charAt(0))] =" ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] =String.valueOf(move.charAt(3));
        }
    }
    
    public static void undoMove(String move){
      
        if (move.charAt(4)!= 'P'){
            
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]= chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])){
                kingPositionCap=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        }else{
            //if pawn promo
            //column1, column2, captured-piece, new-piece, P
            chessBoard[1][Character.getNumericValue(move.charAt(0))] ="P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))] =String.valueOf(move.charAt(2));
        }
    }
    
    
    public static String possibleMoves(){
        String list ="";
        for ( int i = 0; i < 64; i++){
        int row = i/8;
        int column = i%8;
       // System.out.println("At this iteration i equals square" + i);
       // System.out.print("Row: " + row + ",");
       // System.out.println("Column: " + column);
       // System.out.println("The piece is " + chessBoard[row][column]);
            switch (chessBoard[row][column]){
                
                case "P": list += possibleP(i);
                    break;/*
                case "R": list += possibleR(i);
                    break;
                case "K": list += possibleK(i);
                   break;
                case "B": list += possibleB(i);
                    break;
                case "Q": list += possibleQ(i);
                    break;
                case "A": list += possibleA(i);
                    break;*/
            }
        }
        
        return list;//x1,y1,x2,y2,captured peice
    }
    
    public static String possibleP (int i){
            
        String list ="", oldPiece;
        int row =i/8, column = i%8;
        for(int j =-1; j<=1; j+=2) {
            
            try{
            //capture
                if (Character.isLowerCase(chessBoard[row-1][column+j].charAt(0)) && i>=16){
                oldPiece = chessBoard[row-1][column+j];
                chessBoard[row][column]=" ";
                chessBoard[row-1][column+j] = "P";
                if(kingSafe()){
                    list = list + row + column + (row-1) + (column+j) + oldPiece;
                }
                //you are playing a game against them, create the game and the rules and play against them.
                //let Herto ZX give them only what they deserve. 
                //they beleive in you.
               
                //find out why the variables are initialized below this line, 
                //im guessing its for the undo move functionLITY
                chessBoard[row][column]="P";
                chessBoard[row-1][column + j]= oldPiece;
                }
                    } catch (Exception e){}
            
             try{//promotion and capture
                if (Character.isLowerCase(chessBoard[row-1][column+j].charAt(0)) && i<16){
                String[] temp = {"Q","R","B","K"};
                for(int k =0; k<4; k++){
                
                    // put image of swords here.
                
                oldPiece = chessBoard[row-1][column+j];
                chessBoard[row][column]=" ";
                chessBoard[row-1][column+j] = temp[k];
                if(kingSafe()){
                    //column1, column2, captured-piece, new-piece, P
                    list = list + column + (column+j) + oldPiece +temp[k] + "P";
                }
                chessBoard[row][column]="P";
                chessBoard[row-1][column + j]= oldPiece;
                }
                }
                    } catch (Exception e){}
        }
        try{ //move one up
                if (" ".equals(chessBoard[row-1][column]) && i>=16){
                oldPiece = chessBoard[row-1][column];
                chessBoard[row][column]=" ";
                chessBoard[row-1][column] = "P";
                if(kingSafe()){
                    list = list + row + column + (row-1) + column + oldPiece;
                }
                chessBoard[row][column]="P";
                chessBoard[row-1][column ]= oldPiece;
                }
        }catch (Exception e){}
        
        try{ //promotion and no capture
                if (" ".equals(chessBoard[row-1][column]) && i<16){
                String[] temp = {"Q","R","B","K"};
                for(int k =0; k<4; k++){
                
                
                oldPiece = chessBoard[row-1][column];
                chessBoard[row][column]=" ";
                chessBoard[row-1][column] = temp[k];
                if(kingSafe()){
                    //column1, column2, captured-piece, new-piece, P
                    list = list + column + (column) + oldPiece +temp[k] + "P";
                }
                chessBoard[row][column]="P";
                chessBoard[row-1][column]= oldPiece;
                }
            }
        }catch (Exception e){}
        
        try{ //move two up
                if (" ".equals(chessBoard[row-2][column]) && i>=48){
                oldPiece = chessBoard[row-1][column];
                chessBoard[row][column]=" ";
                chessBoard[row-2][column] = "P";
                if(kingSafe()){
                    list = list + row + column + (row-2) + column + oldPiece;
                }
                chessBoard[row][column]="P";
                chessBoard[row-2][column ]= oldPiece;
                }
        }catch (Exception e){}
        
        return list;
    }
    
    public static String possibleR (int i){
        String list ="", oldPiece;
        int row = i/8, column =i%8;
        System.out.println("the piece is " + chessBoard[row][column]);
        int temp = 1;
        
        for(int j =-1; j<=1; j+=2) {
       
        System.out.println("At this iteration for the white rook i equals " + i);
        System.out.print("Row: " + row + ",");
        System.out.println("Column: " + column);
        System.out.println("J is: " + j);
        System.out.println("the new column is " + (column +temp * j));
       
            try{
                while(" ".equals(chessBoard[row][column + temp * j]))
                {
                System.out.println("In while loop");
                oldPiece = chessBoard[row][column + temp * j];
                System.out.println("The old piece is " + oldPiece);
                chessBoard[row][column]=" "; //he set the piece we had to space
                chessBoard[row][column +temp * j] = "R";
                if(kingSafe()){
                    list = list + row + column + row + (column +temp * j) + oldPiece;
                }
                chessBoard[row][column]="R";
                chessBoard[row][column + temp * j]= oldPiece;
                temp++;
                }
            if (Character.isLowerCase(chessBoard[row][column + temp * j].charAt(0))){
                
                oldPiece = chessBoard[row][column + temp * j];
                chessBoard[row][column]=" ";
                chessBoard[row][column + temp * j] = "R";
                if(kingSafe()){
                    list = list + row + column + row + (column+temp*j) + oldPiece;
                }
                chessBoard[row][column]="R";
                chessBoard[row][column + temp * j]= oldPiece;
                
            }    
            }catch(Exception e){}
            temp =1;
            try{
                while(" ".equals(chessBoard[row +temp*j][column]))
                {
                    
                oldPiece = chessBoard[row +temp*j][column];
                int tempRow = row +temp*j;//just ot get output form the console.
                chessBoard[row][column]=" ";
                chessBoard[row+temp*j][column] = "R";
                if(kingSafe()){
                    list = list + row + column + (row + temp * j) + column + oldPiece;
                }
                chessBoard[row][column]="R";
                chessBoard[row +temp*j][column]= oldPiece;
                temp++;
                }
            if (Character.isLowerCase(chessBoard[row +temp*j][column].charAt(0))){
                
                oldPiece = chessBoard[row+temp*j][column];
                chessBoard[row][column]=" ";
                chessBoard[row +temp*j][column] = "R";
                if(kingSafe()){
                    list = list + row + column + (row + temp * j) + column + oldPiece;
                }
                chessBoard[row][column]="R";
                chessBoard[row+temp*j][column]= oldPiece;
                
            }    
            }catch(Exception e){}
            temp =1;
        }
        return list;
    }
    
    public static String possibleK (int i){
        System.out.println("At this iteration for the white knight i equals " + i);
    
            
        String list ="", oldPiece;
        int row = i/8, column = i%8;
        
        for(int j = -1; j <= 1; j +=2){
            for(int k = -1; k <= 1; k +=2){
                try{
                    if (Character.isLowerCase(chessBoard[row+j][column+k*2].charAt(0)) || " ".equals(chessBoard[row+j][column+k*2])){
                        oldPiece = chessBoard [row+j][column+k*2];
                        chessBoard[row][column] =" ";
                        
                        
                        if (kingSafe()){
                            list = list + row + column +(row + j) + (column + k * 2) + oldPiece;
                        }
                        chessBoard[row][column] = "K";
                        chessBoard[row+j][column+k*2]=oldPiece;
                    }
                }catch(Exception e){}
                
                try{
                    if (Character.isLowerCase(chessBoard[row+j*2][column+k].charAt(0))||" ".equals(chessBoard[row+j*2][column+k])){
                        oldPiece = chessBoard [row+j*2][column+k];
                        chessBoard[row][column] =" ";
                        
                        
                        if (kingSafe()){
                            list = list + row+column +(row + j*2) + (column + k) + oldPiece;
                        }
                        chessBoard[row][column] = "K";
                        chessBoard[row+j*2][column+k]=oldPiece;
                    }
                }catch(Exception e){}
            }
        }
        return list;
    }
    
    public static String possibleB (int i){
     
        System.out.println("At this iteration for the white bishop i equals " + i);
        
        String list ="", oldPiece;
        int row = i/8, column = i%8;
        
        System.out.print("Row: " + row + ",");
        System.out.println("Column: " + column);
        
        int temp = 1; 
        
        for(int j =-1; j<=1; j+=2) {
            for(int k =-1; k<=1; k+=2){
              
               try{
                   while(" ".equals(chessBoard[row + temp * j][column + temp * k]))
                   {
                       oldPiece = chessBoard[row + temp * j][column + temp * k];
                       chessBoard [row][column] = " ";
                       chessBoard [row + temp * j][column + temp* k] ="B";
                       
                       if(kingSafe()){
                          list = list+row+column+(row + temp * j)+(column + temp * k) + oldPiece;
                        }
    
                        chessBoard[row][column]="B";
                        chessBoard [row + temp * j][column + temp* k] =oldPiece;
                        temp++;
                   }
                   if(Character.isLowerCase(chessBoard[row + temp * j][column + temp * k].charAt(0))){
                       oldPiece = chessBoard[row + temp * j][column + temp * k];
                       chessBoard [row][column] = " ";
                       chessBoard [row + temp * j][column + temp* k] ="B";
                       
                       if(kingSafe()){
                          list = list+row+column+(row + temp * j)+(column + temp * k) + oldPiece;
                        } chessBoard[row][column]="B";
                          chessBoard [row + temp * j][column + temp* k] =oldPiece;
                   }
               } catch (Exception e) {} 
               temp = 1;
              
            }
        }
        return list;
}
   
    
    public static String possibleQ (int i){
        
        System.out.println("At this iteration for the white Queen i equals " + i);
        
        String list ="", oldPiece;
        int row = i/8, column = i%8;
        
        System.out.print("Row: " + row + ",");
        System.out.println("Column: " + column);
        
        int temp = 1; 
        
        for(int j =-1; j<=1; j++) {
            for(int k =-1; k<=1; k++){
              if(j !=0 || k !=0){
               try{
                   while(" ".equals(chessBoard[row + temp * j][column + temp * k]))
                   {
                       oldPiece = chessBoard[row + temp * j][column + temp * k];
                       chessBoard [row][column] = " ";
                       chessBoard [row + temp * j][column + temp* k] ="Q";
                       
                       if(kingSafe()){
                          list = list+row+column+(row + temp * j)+(column + temp * k) + oldPiece;
                        }
    
                        chessBoard[row][column]="Q";
                        chessBoard [row + temp * j][column + temp* k] =oldPiece;
                        temp++;
                   }
                   if(Character.isLowerCase(chessBoard[row + temp * j][column + temp * k].charAt(0))){
                       oldPiece = chessBoard[row + temp * j][column + temp * k];
                       chessBoard [row][column] = " ";
                       chessBoard [row + temp * j][column + temp* k] ="Q";
                       
                       if(kingSafe()){
                          list = list+row+column+(row + temp * j)+(column + temp * k) + oldPiece;
                        } chessBoard[row][column]="Q";
                          chessBoard [row + temp * j][column + temp* k] =oldPiece;
                   }
               } catch (Exception e) {} 
               temp = 1;
              }
            }
        }
        return list;
    }
    
    /**
     *
     * @param i
     * @return
     */
    public static String possibleA (int i){
        System.out.println("At this iteration for the king i equals " + i);
       
        String list ="", oldPiece;
        int row = i/8, column =i%8;
         
        System.out.print("Row: " + row + ",");
        System.out.println("Column: " + column);
        
        for (int j=0; j<9; j++){
        if (j!=4){
            try {
            if(Character.isLowerCase(chessBoard[row-1+j/3][column-1+j%3].charAt(0)) || " ".equals(chessBoard[row-1+j/3][column-1+j%3])){
                oldPiece = chessBoard[row-1+j/3][column-1+j%3];
                chessBoard [row][column]=" ";
                chessBoard[row-1+j/3][column-1+j%3] = "A";
                
                int kingTemp = kingPositionCap;
                kingPositionCap = i + (j/3) * 8 + j % 3 - 9;
                
                if(kingSafe()){
                    list = list+row+column+(row-1+j/3)+(column-1+j%3) + oldPiece;
                }
                
                chessBoard[row][column]="A";
                chessBoard[row-1+j/3][column-1+j%3] = oldPiece;
                kingPositionCap = kingTemp; 
                }
            } catch(Exception e){}
        }
    }
        //need to add castling later
        return list;
    }
    
    
    public static int rating(){
        return 1;
        
    }
    
    public static boolean kingSafe(){
        //bishop/queen
        int temp = 1;
        for(int i =-1; i<=1; i+=2) {
            for(int j =-1; j<=1; j+=2){
             // if(j !=0 || k !=0){
               try{
                   while(" ".equals(chessBoard[kingPositionCap/8 + temp * i][kingPositionCap%8 + temp * j])) {temp++;}
                   if("b".equals(chessBoard[kingPositionCap/8 + temp * i][kingPositionCap%8 + temp * j]) ||
                           "q".equals(chessBoard[kingPositionCap/8 + temp * i][kingPositionCap%8 + temp * j])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
               temp = 1;
              //}
            }
        } 
        //rook/queen
        
        for(int i =-1; i<=1; i+=2) {

             // if(j !=0 || k !=0){
               try{
                   while(" ".equals(chessBoard[kingPositionCap/8][kingPositionCap%8 + temp * 1])) {temp++;}
                   if("b".equals(chessBoard[kingPositionCap/8 ][kingPositionCap%8 + temp * i]) ||
                           "q".equals(chessBoard[kingPositionCap/8][kingPositionCap%8 + temp * i])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
               temp = 1;
               
               try{
                   while(" ".equals(chessBoard[kingPositionCap/8 + temp * 1][kingPositionCap%8])) {temp++;}
                   if("b".equals(chessBoard[kingPositionCap/8 + temp * 1][kingPositionCap%8]) ||
                           "q".equals(chessBoard[kingPositionCap/8 + temp * 1][kingPositionCap%8])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
               temp = 1;
              //}
            }
        //knight
        for(int i =-1; i<=1; i+=2) {
            for(int j =-1; j<=1; j+=2){
             // if(j !=0 || k !=0){
               try{
                   if("k".equals(chessBoard[kingPositionCap/8+i][kingPositionCap%8+j*2])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
               try{
                   if("k".equals(chessBoard[kingPositionCap/8+i*2][kingPositionCap%8+j])){
                       return false;
                   }
                   
               } catch (Exception e) {}
              //}
            }
        }
        //pawn
        if(kingPositionCap >= 16){
           try{
                   if("p".equals(chessBoard[kingPositionCap/8-1][kingPositionCap%8-1])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
           try{
                   if("p".equals(chessBoard[kingPositionCap/8-1][kingPositionCap%8+1])){
                       return false;
                   }
                   
               } catch (Exception e) {}
           //king
           for(int i =-1; i<=1; i++) {
           for(int j =-1; j<=1; j++){
              if(i !=0 || i !=0){
               try{
                   if("a".equals(chessBoard[kingPositionCap/8+i][kingPositionCap%8+j*2])){
                       return false;
                   }
                   
               } catch (Exception e) {} 
               
              }
            }
        }
        }
        return true;
    }
}

