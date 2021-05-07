//Holds the values of the face of a card, as well as a handy way to convert that to a character
    enum Face {
        ACE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7), NINE(8), JACK(9), QUEEN(10), KING(11);

        private int value;

        private Face(int value) {
            this.value = value;
        }

        public String getValue() {
            String[] vals = {"A","2","3","4","5","6","7","8","9","J","Q","K"}; 
            return vals[value];
        }

        public int getInt() {
            return value;
        }

        public static Face[] items = new Face[]{
            ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, JACK, QUEEN, KING
        };
    }
