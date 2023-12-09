import java.util.*;
class Point
{
    int x;
    int y;

    public Point(int x,int y) {
        this.x = x;
        this.y=y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Taking user input for the drone locations
        System.out.println("Enter locations for 4 drones (x y):");
        Map<Point,Integer> hm = new HashMap<>();

        Point[] drones = new Point[4];
        for(int i=0;i<4;i++)
        {
            int x= sc.nextInt();
            int y= sc.nextInt();

            if(x<0 || x>24 || y<0 || y>24)
            {
                System.out.println("Invalid input, input should be between 0 and 24");
                System.exit(0);
            }

            Point p = new Point(x,y);
            drones[i] = p;
            if(hm.containsKey(p)){
                System.out.println("Invalid Input. Two drones cannot be at the same location");
                System.exit(0);
            }
            hm.put(p,i+1);
        }

        // Taking user input for target location
        System.out.println(" Enter target location (x y):");
        int X_target = sc.nextInt();
        int Y_target = sc.nextInt();

        if(X_target<0 || X_target>24 || Y_target<0 || Y_target>24)
        {
            System.out.println("Invalid input, input should be between 0 and 24");
            System.exit(0);
        }

        Point target = new Point(X_target,Y_target);

        // Find the path of first drone to reach the target using multi-source BFS
        List<Point> path = multiSourceBFS(drones, target);

        // Print the complete path of first drone to reach the target.
        if(path!=null)
        {
            Point start = path.get(0);
            System.out.println("The first drone to reach the target is drone "+ hm.get(start) + ".");
            System.out.println("The path taken by drone "+ hm.get(start) + " to reach yhe target is:");
            for(Point p:path)
            {
                System.out.println("(" + p.x +", "+ p.y+")");
            }
        }else{
            System.out.println("No drone can reach the target");
        }
    }

    public static List<Point> multiSourceBFS(Point[] drones, Point target){
        Queue<Point> q = new LinkedList<>();

        // To prevent the drone from visiting the same cell twice and also to print the path
        Map<Point,Point> coordinates = new HashMap<>();

        // Adding all drones to the queue
        for(Point drone:drones)
        {
            q.offer(drone);
            coordinates.put(drone,null);
        }

        // To traverse in all 4 directions
        int[][] directions={{-1,0},{1,0},{0,-1},{0,1}};

        while(!q.isEmpty())
        {
            Point current = q.poll();

            // If target is reached, return the path
            if(current.x==target.x && current.y==target.y)
            {
                List<Point> path = new ArrayList<>();
                // To get the complete path. At the end, current will be null as we have entered null in the map for the source

                while(current!=null)
                {
                    path.add(0,current);
                    current = coordinates.get(current);
                }
                return path;
            }

            // To reach all the adjacent cells possible from the current cell considering the boundaries
            for(int[] direction:directions)
            {
                int new_X = current.x + direction[0];
                int new_Y = current.y + direction[1];
                Point p = new Point(new_X,new_Y);

                if(new_X>0 && new_X<25 && new_Y>0 && new_Y<25 && !coordinates.containsKey(p)) {
                    q.offer(p);
                    coordinates.put(p, current);
                }
            }
        }
        return null;
    }
}