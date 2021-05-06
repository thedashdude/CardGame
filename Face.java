
    enum Face {
        ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), JACK("J"), QUEEN("Q"), KING("K");

        private String value;

        private Face(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Face[] items = new Face[]{
            ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, JACK, QUEEN, KING
        };
    }
