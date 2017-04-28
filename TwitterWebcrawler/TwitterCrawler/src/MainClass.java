
public class MainClass
{
	public static void main(String[] args)
	{
		if(args.length <= 0)
		{
			System.out.println("Error: Missing Argument.");
			System.out.println("Please Read The README.txt On How To Run This Program.");
			System.exit(0);
		}
		else if(args.length == 2)
		{
			int numOfTweets = Integer.parseInt(args[0]);
			
			if(numOfTweets < 0)
			{
				System.out.println("Error: Argument Too Small!");
				System.out.println("Please Read The README.txt On How To Run This Program.");
			}
			else
			{
				TwitterS ts = new TwitterS(numOfTweets, args[1]);
				ts.run();
			}
		}
		else
		{
			System.out.println("Error: Too Many Arguments. Only need one.");
			System.out.println("Please Read The README.txt On How To Run This Program.");
			System.exit(0);
		}
		
	}
}

