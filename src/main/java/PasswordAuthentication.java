public class PasswordAuthentication {
    private static final int DEFAULT_COST = 16;
    private final int cost;

    public PasswordAuthentication() {
        this(DEFAULT_COST);
    }

    public PasswordAuthentication(int cost) {
        // iterations(cost)
        this.cost = cost;
    }

    // I'm thinking to use this class both for login and for registration, to generate a hash
    // or to check the introduced password
}
