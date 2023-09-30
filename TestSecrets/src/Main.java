import java.io.IOException;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello");
		
		try {
			new SecretService().start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
