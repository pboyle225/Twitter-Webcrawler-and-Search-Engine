package cs172part2;

public class Main {

	public static void main(String[] args) {
		TextFileIndexer t = new TextFileIndexer();
		try {
			
			if(args.length == 1)
				t.run(args[0]);
			else
				System.out.println("ERROR: ARGUMENT LIST IS WRONG!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
