public class ProceduralGeneration2D {
  /* RULES TO IMPLEMENT
  Consider doing 2 passes? 1 for the surfaces following the spawn rules, another to add / and |
  */

  public static void main(String[] args){
    int height=20;
    int width=150;
    int[][] world = new int[width][height];

    int maxIslandLength = (int)Math.max((width/2), 1);
    double[] islandExtensionChances = new double[maxIslandLength+1];

    for(int n=0; n<maxIslandLength+1; n++){
      islandExtensionChances[n] = (1-Math.pow(100, (float)n/(maxIslandLength)-1))/0.99;
    }

    boolean spawn=false;

    double spawnChance = 0.001;
    double growChance = 0.9;//chance a tile with 4 or 5 parents also turns into a non-0 tile
    double dripChance = 0.4;//chance any applicable tile with a vertical parent also turns into a non-0 tile

    int extensionNum=0;

    boolean hasVerticalSpace;
    boolean hasHorizontalSpace;

    int lastSpawnXThisRow;
    int horizontalSpawnDistance = 5;
    int verticalSpawnDistance = 1;

    int lastSpawnY=1;
    int highVerticalDistance=3;//if it reaches this many rows since last surface, each row will run repeatMax times for increased chance at surface spawn
    
    int repeatMax=5;
    int repeatCount=0;

    int lParent=0;
    int rParent=0;
    int numParents=3;
    double val;

    // create islands
    for(int i=2; i<height; i++){
      lastSpawnXThisRow = -horizontalSpawnDistance-1;
      if(i-lastSpawnY>highVerticalDistance&&repeatCount<=repeatMax){
        i--;
        repeatCount++;
      }
      else{
        repeatCount=0;
      }

      for(int k=0; k<width; k++){
        val=(Math.random());

        hasVerticalSpace = verticalCheck(world, k, i, verticalSpawnDistance);
        hasHorizontalSpace = k-lastSpawnXThisRow>horizontalSpawnDistance?true:false;

        if(extensionNum!=0&&hasVerticalSpace){
          if(val<islandExtensionChances[extensionNum]){
            world[k][i]=1;
            lastSpawnXThisRow=k;
            lastSpawnY=i;
            extensionNum++;
          }
          else{
            extensionNum=0;
          }
        }
        else{
          lParent = Math.max(k-2, 0);
          rParent = Math.min(k+2, width-1);
          numParents=5;

          if(!spawn){
            spawn=val<spawnChance;
          }

          for(int m=lParent; m<=rParent; m++){
            if(world[m][i-1]==0){
              numParents--;
            }
          }

          if(numParents>=4&&val<growChance*(numParents/5)){
            world[k][i]=3;
            lastSpawnXThisRow=k;
            extensionNum=0;
          }
          else if(world[k][i-1]!=0&&val<dripChance&&hasHorizontalSpace){ // check for horizontal space?
            world[k][i]=4;
            lastSpawnXThisRow=k;
            extensionNum=0;
          }
          else if(spawn&&hasVerticalSpace&&hasHorizontalSpace){
            world[k][i]=1;
            lastSpawnXThisRow=k;
            lastSpawnY = i;
            spawn=false;
            extensionNum++;
          }
          else{
            extensionNum=0;
          }
        }
      }
      extensionNum=0;
    }

    printWorldArray(world);
  }

  static boolean verticalCheck(int[][] world, int startX, int startY, int distance){
    startX = Math.max(0, startX-1);
    for(int x=startX; x<=startX+2&&x<world.length; x++){
      if(!columnCheck(world[x], startY, distance)){
        return false;
      }
    }
    return true;
  }

  static boolean columnCheck(int[] column, int startY, int distance){
    int end = Math.max(startY-distance, 0);
    for(int i=startY-1; i>=end; i--){
      if(column[i]!=0){
        return false;
      }
    }
    return true;
  }

  static void printWorldArray(int[][] worldIn){
    int width=worldIn.length;
    int height=worldIn[0].length;
    for(int i=0; i<height; i++){
      for(int k=0; k<width; k++){
        switch (worldIn[k][i]) {
          case 0:
            System.out.print('.');
            break;
          case 1:
          case 2:
            System.out.print('_');
            break;
          case 3:
            System.out.print('/');
            break;
          case 4:
            System.out.print('|');
            break;
          default:
            break;
        }
      }
      System.out.println("");
    }
  } 
}
