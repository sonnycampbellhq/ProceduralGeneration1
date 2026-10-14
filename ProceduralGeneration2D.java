import java.lang.reflect.Array;
import java.util.Arrays;

public class ProceduralGeneration2D {
  /* RULES TO IMPLEMENT
  Island max height (can't be on top x layers? (just initialise i to x)) - done
  
  Length maximum (array of decreasing chances), relative to the width of the world, resets on new line - done

  Shape the islands (check the 5 pixels above?) - done

  Min distance between island directly above - when spawning any island node, make sure 2 tiles above are empty, or don't spawn and reset reset extension - done

  Min distance between island horizontally (check left and right when spawning node, don't check own island...) - done


  DO::
  Max height and width between islands (record the k,i position of the current lowest (top of an) island. If it has been x rows since lowest island, guarantee spawn an island this row)
  Also make the vertical check check from 1 before and 1 after any island surface too

  Consider doing 2 passes? 1 for the surfaces following the spawn rules, another to add / and |

  Change it back to an int array (easier interpretation in unity), but the print function interprets it to chars
  */

  public static void main(String[] args){
    int height=40;
    int width=150;
    int[][] world = new int[width][height];

    int maxIslandLength = (int)Math.max((width/2), 1);
    double[] islandExtensionChances = new double[maxIslandLength+1];

    for(int n=0; n<maxIslandLength+1; n++){
      islandExtensionChances[n] = (1-Math.pow(100, (float)n/(maxIslandLength)-1))/0.99;
    }

    double spawnChance = 0.02;
    double growChance = 0.9;//chance a tile with 4 or 5 parents also turns into a non-0 tile
    double dripChance = 0.4;//chance any applicable tile with a vertical parent also turns into a non-0 tile
    int extensionNum=0;

    boolean hasVerticalSpace;
    boolean hasHorizontalSpace;

    int lastSpawn;
    int horizontalSpawnDistance = 4;

    int lParent=0;
    int rParent=0;
    int numParents=3;
    double val;

    // create islands
    for(int i=2; i<height; i++){
      lastSpawn = -horizontalSpawnDistance-1;
      for(int k=0; k<width; k++){
        val=(Math.random());

        hasVerticalSpace = verticalCheck(world[k][i-1], world[k][i-2]);
        hasHorizontalSpace = k-lastSpawn>horizontalSpawnDistance?true:false;

        if(extensionNum!=0&&hasVerticalSpace){
          if(val<islandExtensionChances[extensionNum]){
            world[k][i]=1;
            lastSpawn=k;
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

          for(int m=lParent; m<=rParent; m++){
            if(world[m][i-1]==0){
              numParents--;
            }
          }

          if(numParents>=4&&val<growChance*(numParents/5)){
            world[k][i]=3;
            lastSpawn=k;
            extensionNum=0;
          }
          else if(world[k][i-1]!=0&&val<dripChance&&hasHorizontalSpace){ // check for horizontal space?
            world[k][i]=4;
            lastSpawn=k;
            extensionNum=0;
          }
          else if(val<spawnChance&&hasVerticalSpace&&hasHorizontalSpace){
            world[k][i]=1;
            lastSpawn=k;
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

  static boolean verticalCheck(int a, int b){
    if(a==0&&b==0){
      return true;
    }
    return false;
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
